package com.app.domain.models;

public class AliveEntity {
    private String id;
    private long lifeTimeInMs;

    public AliveEntity(String id, long lifeTime) {
        this.id = id;
        this.lifeTimeInMs = lifeTime;
    }

    public long getLifeTimeInMs() {
        return lifeTimeInMs;
    }

    public void setLifeTimeInMs(long lifeTimeInMs) {
        this.lifeTimeInMs = lifeTimeInMs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
