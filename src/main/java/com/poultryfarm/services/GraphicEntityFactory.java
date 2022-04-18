package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;

public interface GraphicEntityFactory {
    GraphicEntity createEntity(int x, int y);

    GraphicEntity createEntity(int x, int y, int dx, int dy);
    long getEntityLifeTimeInMs();
}
