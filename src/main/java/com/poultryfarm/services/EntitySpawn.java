package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;

public interface EntitySpawn {
    GraphicEntity createEntity(int x, int y);

    GraphicEntity createEntity(int x, int y, int dx, int dy);

    long getLifeTimeInMs();

    void setLifeTime(long lifeTime);

    long getSpawnPeriodInMs();

    void setSpawnPeriod(long spawnPeriod);

    String getType();
}
