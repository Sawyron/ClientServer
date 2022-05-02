package com.poultryfarm.domain;

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

    public AliveEntity getEntityById(String id) {
        return entityBirthTime.keySet().stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Collection<AliveEntity> getEntities() {
        return List.copyOf(entityBirthTime.keySet());
    }

    public boolean removeEntity(String id) {
        return entityBirthTime.entrySet().removeIf(e -> e.getKey().getId().equals(id));
    }

    public void clear() {
        entityBirthTime.clear();
    }

    public void removeEntitiesByIds(Collection<String> ids) {
        entityBirthTime.entrySet().removeIf(e -> ids.contains(e.getKey().getId()));
    }

    public void increaseLifeTime(long time) {
        entityBirthTime.replaceAll((e, v) -> entityBirthTime.get(e) + time);
    }
}
