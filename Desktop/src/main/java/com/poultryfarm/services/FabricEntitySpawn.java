package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;

public class FabricEntitySpawn implements EntitySpawn {
    private final String type;
    private final long lifeTime;
    private final long spawnPeriod;
    private final GraphicEntityFactory factory;

    public FabricEntitySpawn(String type, long lifeTime, long spawnPeriod, GraphicEntityFactory factory) {
        this.type = type;
        this.lifeTime = lifeTime;
        this.spawnPeriod = spawnPeriod;
        this.factory = factory;
    }

    @Override
    public GraphicEntity createEntity(int x, int y) {
        return factory.createEntity(x, y);
    }

    @Override
    public GraphicEntity createEntity(int x, int y, int dx, int dy) {
        return factory.createEntity(x, y, dx, dy);
    }

    @Override
    public long getLifeTimeInMs() {
        return lifeTime;
    }

    @Override
    public long getSpawnPeriodInMs() {
        return spawnPeriod;
    }

    @Override
    public String getType() {
        return type;
    }
}
