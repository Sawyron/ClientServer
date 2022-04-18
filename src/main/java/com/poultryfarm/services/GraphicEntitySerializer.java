package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;

import java.util.Collection;
import java.util.List;

public interface GraphicEntitySerializer {
    void saveEntities(Collection<GraphicEntity> entities, String path);

    List<GraphicEntity> loadEntities(String path);
}
