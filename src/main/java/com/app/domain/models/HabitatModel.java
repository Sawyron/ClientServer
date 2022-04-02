package com.app.domain.models;

import java.util.*;

public class HabitatModel {
    private final Map<AliveEntity, Long> entityBirthTime = new HashMap<>();

    public Collection<String> getDeadEntitiesIds(long time) {
        List<String> ids = new LinkedList<>();
        for (Map.Entry<AliveEntity, Long> pair : entityBirthTime.entrySet()) {
            if (time - pair.getValue() >= pair.getKey().getLifeTimeInMs()) {
                ids.add(pair.getKey().getId());
            }
        }
        return ids;
    }

    public void addEntity(AliveEntity entity, long time) {
        entityBirthTime.put(entity, time);
    }

    public void addEntity(AliveEntity entity) {
        addEntity(entity, System.currentTimeMillis());
    }

    public void clear(){
        entityBirthTime.clear();
    }

    public void increaseLifeTime(long time) {
        entityBirthTime.replaceAll((e, v) -> entityBirthTime.get(e) + time);
    }
}
