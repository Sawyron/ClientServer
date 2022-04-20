package com.poultryfarm.controllers;

import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.domain.TransferEntity;
import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.services.RunnableWorker;
import com.poultryfarm.services.entityserializers.EntitySerializer;
import com.poultryfarm.services.entityserializers.FIleEntitySerializer;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.poultryfarm.ui.habitat.FileTypeFilter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityController {
    private final HabitatModel model;
    private final GraphicEntityView view;
    private final MessageService messageService;
    private final List<EntitySpawn> entitySpawns = new LinkedList<>();

    private final List<RunnableWorker> workers = new LinkedList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private long viewUpdatePeriodInMs = 20;
    private long movementPeriodInMs = 50;
    private long checkDeadPeriod = 500;
    private long pauseTime;
    private List<FIleEntitySerializer> fIleEntitySerializers = new LinkedList<>();

    public EntityController(HabitatModel model, GraphicEntityView view, MessageService messageService) {
        this.model = model;
        this.view = view;
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
        view.addWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                shutdown();
                System.exit(0);
            }
        });
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
        FIleEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showMessage("No serializer for this extension");
            return;
        }
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
                    }).toList();
        }
        serializer.saveEntities(transferEntities, file);
    }

    private void loadEntities(File file) {
        FIleEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showMessage("No serializer for this extension");
            return;
        }
        List<TransferEntity> transferEntities = serializer.loadEntities(file);
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

    public void addFileEntitySerializer(String description, String extension, EntitySerializer serializer) {
        view.addFileFilter(new FileTypeFilter(description, extension));
        fIleEntitySerializers.add(new FIleEntitySerializer(serializer, description, extension));
    }

    public void run() {
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
}
