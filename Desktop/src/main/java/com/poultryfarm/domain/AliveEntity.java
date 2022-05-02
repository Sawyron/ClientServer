package com.poultryfarm.domain;

public class AliveEntity {
    private String id;
    private String type;
    private long lifeTimeInMs;

    public AliveEntity(String id, String type, long lifeTime) {
        this.id = id;
        this.lifeTimeInMs = lifeTime;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
