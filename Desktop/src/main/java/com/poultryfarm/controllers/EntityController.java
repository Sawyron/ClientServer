package com.poultryfarm.controllers;

import com.poultryfarm.clients.Client;
import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.services.network.EntityClient;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.ExtensionFileEntitySerializer;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class EntityController extends AbstractEntityController {

    private final HabitatModel model;
    private final MessageService messageService;
    private final EntityClient entityClient;
    private final Map<String, Client> clientMap = new HashMap<>();
    private final List<EntitySpawn> entitySpawns = new LinkedList<>();
    private final List<ExtensionFileEntitySerializer> fIleEntitySerializers = new LinkedList<>();
    private final TaskExecutor executor;
    private long viewUpdatePeriodInMs = 20;
    private long movementPeriodInMs = 50;
    private long checkDeadPeriod = 500;
    private long pauseTime;

    public EntityController(HabitatModel model, GraphicEntityView view, MessageService messageService, EntityClient entityClient) {
        super(view);
        this.model = model;
        this.messageService = messageService;
        this.entityClient = entityClient;
        executor = new TaskExecutor((e) -> {
            messageService.showError(e.getMessage());
            onExit();
        });
    }

    @Override
    public void run() {
        executor.addRepeatedTask(view::moveEntities, viewUpdatePeriodInMs);
        executor.addRepeatedTask(this::removeDeadEntities, movementPeriodInMs);
        for (EntitySpawn entitySpawn : entitySpawns) {
            executor.addRepeatedTask(() -> {
                Random random = new Random();
                spawnEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight()), entitySpawn);
            }, entitySpawn.getSpawnPeriodInMs());
        }
        executor.pauseRepeatedTasks();
        executor.runRepeatedTasks();
        super.run();
    }

    @Override
    public void start() {
        view.setActiveState();
        resume();
        synchronized (model) {
            model.clear();
        }
        synchronized (view) {
            view.clearEntities();
        }

    }

    @Override
    public void stop() {
        pause();
        view.setStoppedState();
    }

    @Override
    public void pause() {
        executor.pauseRepeatedTasks();
        view.setPausedState();
        pauseTime = System.currentTimeMillis();
    }

    @Override
    public void resume() {
        executor.resumeRepeatedTasks();
        view.setResumedState();
        synchronized (model) {
            model.increaseLifeTime(System.currentTimeMillis() - pauseTime);
        }
    }

    @Override
    public void onEntityRightClick(String id) {
        removeEntityById(id);
    }

    @Override
    public void onEntityLeftClick(String id) {
        changeEntityMovingState(id);
    }

    @Override
    public void onAreaLeftClick(int x, int y) {
        spawnRandomEntityAt(x, y);
    }

    @Override
    public void loadEntities(File file) {
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

    @Override
    public void saveEntities(File file) {
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

    @Override
    public void sendEntitiesToServer() {
        executor.executeTask(() -> {
            List<TransferEntity> transferEntities = getAllTransferEntities();
            try {
                entityClient.sendEntities(transferEntities);
            } catch (Exception exception) {
                messageService.showError(exception.getMessage());
            }
        });
    }

    @Override
    public void receiveEntitiesFromServer() {
        executor.executeTask(() -> {
            try {
                List<TransferEntity> transferEntities = entityClient.receiveEntities();
                addTransferEntities(transferEntities);
            } catch (Exception exception) {
                messageService.showError(exception.getMessage());
            }
        });
    }

    @Override
    public void getEntityFromServerByIndex(int index) {
        executor.executeTask(() -> {
            try {
                TransferEntity transferEntity = entityClient.getEntityAt(index);
                if (transferEntity.getType().equalsIgnoreCase("null")) {
                    return;
                }
                addTransferEntities(List.of(transferEntity));
            } catch (Exception e) {
                messageService.showError(e.getMessage());
            }
        });
    }

    @Override
    public void removeEntityFromServerByIndex(int index) {
        executor.executeTask(() -> {
            try {
                entityClient.removeAt(index);
            } catch (Exception e) {
                messageService.showError(e.getMessage());
            }
        });
    }

    @Override
    public void countEntitiesOnServer() {
        executor.executeTask(() -> {
            try {
                messageService.showMessage("Entities at server: " + entityClient.getEntitiesCount());
            } catch (Exception ex) {
                messageService.showError(ex.getMessage());
            }
        });
    }

    @Override
    public void onServerChanged(String serverName) {
        synchronized (clientMap) {
            Client client = clientMap.get(serverName);
            if (client == null) {
                return;
            }
            synchronized (entityClient) {
                entityClient.setClient(client);
            }
        }
    }

    @Override
    public void onExit() {
        shutdown();
        System.exit(0);
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

    public void addEntitySpawn(EntitySpawn entitySpawn) {
        entitySpawns.add(entitySpawn);
    }

    public void addFileEntitySerializer(String description, String extension, EntitySerializer serializer) {
        view.addFileFilter(new FileNameExtensionFilter(description, extension), extension);
        fIleEntitySerializers.add(new ExtensionFileEntitySerializer(serializer, description, extension));
    }

    public void addServerClient(Client client, String name) {
        clientMap.put(name, client);
        view.addServerName(name);
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

    private void spawnRandomEntityAt(int x, int y) {
        Random random = new Random();
        EntitySpawn spawn = entitySpawns.get(random.nextInt(entitySpawns.size()));
        spawnEntity(x, y, spawn);
    }

    private void removeEntityById(String id) {
        synchronized (view) {
            view.removeEntity(id);
        }
        synchronized (model) {
            model.removeEntity(id);
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

    private void removeDeadEntities() {
        List<String> deadEntitiesIds = List.copyOf(model.getDeadEntitiesIds(System.currentTimeMillis()));
        for (String id : deadEntitiesIds) {
            removeEntityById(id);
        }
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

    private void shutdown() {
        view.setStoppedState();
        executor.shutdown();
    }
}
