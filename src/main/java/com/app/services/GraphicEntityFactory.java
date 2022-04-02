package com.app.services;

import com.app.domain.models.GraphicEntity;

public interface GraphicEntityFactory {
    GraphicEntity createEntity(int x, int y);

    GraphicEntity createEntity(int x, int y, int dx, int dy);
    long getEntityLifeTimeInMs();
}
