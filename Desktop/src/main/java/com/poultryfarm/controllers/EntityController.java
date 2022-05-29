package com.poultryfarm.controllers;

import com.poultryfarm.clients.Client;
import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.services.RunnableWorker;
import com.poultryfarm.services.network.EntityClient;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.ExtensionFileEntitySerializer;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class EntityController {
    private final HabitatModel model;
    private final GraphicEntityView view;
    private final MessageService messageService;
    private final EntityClient entityClient;
    private final Map<String, Client> clientMap = new HashMap<>();
    private final List<EntitySpawn> entitySpawns = new LinkedList<>();

    private final List<RunnableWorker> workers = new LinkedList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private long viewUpdatePeriodInMs = 20;
    private long movementPeriodInMs = 50;
    private long checkDeadPeriod = 500;
    private long pauseTime;
    private final List<ExtensionFileEntitySerializer> fIleEntitySerializers = new LinkedList<>();

    public EntityController(HabitatModel model, GraphicEntityView view, MessageService messageService, EntityClient entityClient) {
        this.model = model;
        this.view = view;
        this.entityClient = entityClient;
        this.messageService = messageService;
        view.addStartActionListener((e) -> {
            view.setActiveState();
            resume();
            synchronized (model) {
                model.clear();
            }
            synchronized (view) {
                view.clearEntities();
            }
        });
        view.addStopActionListener((e) -> {
            pause();
            view.setStoppedState();
        });
        view.addPauseActionListener((e) -> pause());
        view.addResumeActionLister((e) -> resume());
        view.addEntityRightButtonClickedListener(this::removeEntityById);
        view.addAreaPointLeftButtonClickedListener(this::spawnRandomEntityAt);
        view.addEntityLeftButtonClickedListener(this::changeEntityMovingState);
        view.addLoadEntityListener(this::loadEntities);
        view.addSaveEntityListener(this::saveEntities);
        view.addSendEntitiesActionListener((e) -> {
            executorService.submit(() -> {
                List<TransferEntity> transferEntities = getAllTransferEntities();
                entityClient.sendEntities(transferEntities);
            });
        });
        view.addReceiveEntitiesActionListener((e) -> {
            executorService.submit(() -> {
                List<TransferEntity> transferEntities = entityClient.receiveEntities();
                addTransferEntities(transferEntities);
            });
        });
        view.addGetEntityIndexListener((index) -> {
            executorService.submit(() -> {
                TransferEntity transferEntity = entityClient.getEntityAt(index);
                if (transferEntity.getType().equalsIgnoreCase("null")) {
                    return;
                }
                addTransferEntities(List.of(transferEntity));
            });
        });
        view.addServerNameListener((value) -> {
            executorService.submit(() -> {
                synchronized (clientMap) {
                    Client client = clientMap.get(value);
                    if (client == null) {
                        return;
                    }
                    synchronized (client) {
                        entityClient.setClient(client);
                    }
                }
            });
        });
        view.addRemovingEntityIndexListener((entityClient::removeAt));
        view.addCountButtonActionListener((e) -> {
            messageService.showMessage("Entities at server: " + entityClient.getEntitiesCount());
        });
        view.addWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                shutdown();
                System.exit(0);
            }
        });

    }

    public void addFileEntitySerializer(String description, String extension, EntitySerializer serializer) {
        view.addFileFilter(new FileNameExtensionFilter(description, extension), extension);
        fIleEntitySerializers.add(new ExtensionFileEntitySerializer(serializer, description, extension));
    }

    public void addServerClient(Client client, String name) {
        clientMap.put(name, client);
    }

    public void run() {
        setupWorkers();
        for (String server : clientMap.keySet()) {
            view.addServerName(server);
        }
        view.run();
        for (RunnableWorker worker : workers) {
            worker.pause();
            executorService.submit(worker);
        }
    }

    public long getViewUpdatePeriodInMs() {
        return viewUpdatePeriodInMs;
    }

    public void setViewUpdatePeriodInMs(long viewUpdatePeriodInMs) {
        this.viewUpdatePeriodInMs = viewUpdatePeriodInMs;
    }

    public long getMovementPeriodInMs() {
        return movementPeriodInMs;
    }

    public void setMovementPeriodInMs(long movementPeriodInMs) {
        this.movementPeriodInMs = movementPeriodInMs;
    }

    public long getCheckDeadPeriod() {
        return checkDeadPeriod;
    }

    public void setCheckDeadPeriod(long checkDeadPeriod) {
        this.checkDeadPeriod = checkDeadPeriod;
    }

    private void setupWorkers() {
        workers.add(new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                synchronized (view) {
                    view.moveEntities();
                }
                try {
                    Thread.sleep(movementPeriodInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new ControllerException(e.getMessage(), e);
                }
            }
        });
        workers.add(new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                try {
                    Thread.sleep(checkDeadPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new ControllerException(e.getMessage(), e);
                }
                removeDeadEntities();
            }
        });
        for (EntitySpawn entitySpawn : entitySpawns) {
            workers.add(new RunnableWorker() {
                @Override
                protected void doUnitOfWork() {
                    Random random = new Random();
                    spawnEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight()), entitySpawn);
                    try {
                        Thread.sleep(entitySpawn.getSpawnPeriodInMs());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new ControllerException(e.getMessage(), e);
                    }
                }
            });
        }
    }

    private void changeEntityMovingState(String id) {
        synchronized (view) {
            GraphicEntity entity = view.getEntityById(id);
            if (entity.getDx() == 0 && entity.getDy() == 0) {
                Random random = new Random();
                entity.setDx(random.nextInt(10) - 5);
                entity.setDy(random.nextInt(10) - 5);
            } else {
                entity.setDy(0);
                entity.setDx(0);
            }
        }
    }

    private void spawnRandomEntityAt(int x, int y) {
        Random random = new Random();
        EntitySpawn spawn = entitySpawns.get(random.nextInt(entitySpawns.size()));
        spawnEntity(x, y, spawn);
    }

    public void addEntitySpawn(EntitySpawn entitySpawn) {
        entitySpawns.add(entitySpawn);
    }

    private void spawnEntity(int x, int y, int dx, int dy, EntitySpawn entitySpawn) {
        String id = UUID.randomUUID().toString();
        synchronized (model) {
            model.addEntity(new AliveEntity(id, entitySpawn.getType(), entitySpawn.getLifeTimeInMs()));
        }
        synchronized (view) {
            view.addEntity(
                    entitySpawn.createEntity(x, y, dx, dy),
                    id
            );
        }
    }

    private void spawnEntity(int x, int y, EntitySpawn entitySpawn) {
        String id = UUID.randomUUID().toString();
        Random random = new Random();
        spawnEntity(x, y, random.nextInt(10) - 5, random.nextInt(10) - 5, entitySpawn);
    }

    private void removeEntityById(String id) {
        synchronized (view) {
            view.removeEntity(id);
        }
        synchronized (model) {
            model.removeEntity(id);
        }
    }

    private void removeDeadEntities() {
        List<String> deadEntitiesIds = List.copyOf(model.getDeadEntitiesIds(System.currentTimeMillis()));
        for (String id : deadEntitiesIds) {
            removeEntityById(id);
        }
    }

    private void shutdown() {
        for (RunnableWorker worker : workers) {
            worker.finish();
        }
        view.setStoppedState();
        executorService.shutdown();
    }

    private void pause() {
        for (RunnableWorker worker : workers) {
            worker.pause();
        }
        view.setPausedState();
        pauseTime = System.currentTimeMillis();
    }

    private void resume() {
        for (RunnableWorker worker : workers) {
            worker.resume();
        }
        view.setResumedState();
        synchronized (model) {
            model.increaseLifeTime(System.currentTimeMillis() - pauseTime);
        }
    }

    private void saveEntities(File file) {
        ExtensionFileEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showMessage("No serializer for this extension");
            return;
        }
        List<TransferEntity> transferEntities = getAllTransferEntities();
        serializer.saveEntities(transferEntities, file);
    }

    private List<TransferEntity> getAllTransferEntities() {
        Collection<AliveEntity> aliveEntities;
        synchronized (model) {
            aliveEntities = model.getEntities();
        }
        List<TransferEntity> transferEntities;
        synchronized (view) {
            transferEntities = aliveEntities.stream()
                    .map(aliveEntity -> {
                        GraphicEntity graphicEntity = view.getEntityById(aliveEntity.getId());
                        return new TransferEntity(
                                graphicEntity.getX(),
                                graphicEntity.getY(),
                                graphicEntity.getDx(),
                                graphicEntity.getDy(),
                                aliveEntity.getType()
                        );
                    }).collect(Collectors.toCollection(LinkedList::new));
        }
        return transferEntities;
    }

    private void loadEntities(File file) {
        ExtensionFileEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showMessage("No serializer for this extension");
            return;
        }
        List<TransferEntity> transferEntities = serializer.loadEntities(file);
        addTransferEntities(transferEntities);
    }

    private void addTransferEntities(Collection<TransferEntity> transferEntities) {
        for (TransferEntity transferEntity : transferEntities) {
            entitySpawns.stream()
                    .filter(s -> s.getType().equals(transferEntity.getType()))
                    .findFirst().ifPresent(spawn -> spawnEntity(
                            transferEntity.getX(),
                            transferEntity.getY(),
                            transferEntity.getDx(),
                            transferEntity.getDy(),
                            spawn
                    ));
        }
    }

}
