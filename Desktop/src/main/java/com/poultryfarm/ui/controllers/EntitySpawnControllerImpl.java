package com.poultryfarm.ui.controllers;

import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.ExceptionHandler;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntitySpawnControllerImpl implements EntitySpawnController {

    private final GraphicEntityView view;
    private final HabitatModel model;
    private final List<EntitySpawn> entitySpawns = new LinkedList<>();

    public EntitySpawnControllerImpl(GraphicEntityView view, HabitatModel model, ExceptionHandler exceptionHandler) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void spawnEntity(int x, int y, int dx, int dy, String entityType) {
        EntitySpawn spawn = entitySpawns.stream().
                filter(entitySpawn ->
                        entitySpawn.getType().equals(entityType))
                .findFirst().orElse(null);
        if (spawn == null) {
            throw new ControllerException("no matching entity spawn");
        }
        String id = UUID.randomUUID().toString();
        model.addEntity(new AliveEntity(id, spawn.getType(), spawn.getLifeTimeInMs()));
        view.addEntity(
                spawn.createEntity(x, y, dx, dy),
                id);
    }

    @Override
    public void spawnEntity(int x, int y, String entityType) {
        String id = UUID.randomUUID().toString();
        Random random = new Random();
        spawnEntity(x, y, random.nextInt(10) - 5, random.nextInt(10) - 5, entityType);
    }

    @Override
    public void spawnRandomEntity(int x, int y) {
        Random random = new Random();
        EntitySpawn spawn = entitySpawns.get(random.nextInt(entitySpawns.size()));
        spawnEntity(x, y, spawn.getType());
    }

    @Override
    public void addEntitySpawn(EntitySpawn entitySpawn) {
        entitySpawns.add(entitySpawn);
    }

    @Override
    public void addSpawnTasksToExecutor(TaskExecutor executor) {
        for (EntitySpawn spawn : entitySpawns) {
            executor.addRepeatedTask(() -> {
                Random random = new Random();
                spawnEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight()), spawn.getType());
            }, spawn.getSpawnPeriodInMs());
        }
    }
}
