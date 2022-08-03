package com.poultryfarm.controllers;

import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.async.TaskExecutor;

public interface EntitySpawnController {
    void spawnEntity(int x, int y, int dx, int dy, String entityType);

    void spawnEntity(int x, int y, String entityType);

    void spawnRandomEntity(int x, int y);

    void addEntitySpawn(EntitySpawn entitySpawn);

    void addSpawnTasksToExecutor(TaskExecutor executor);
}
