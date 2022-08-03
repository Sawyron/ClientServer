package com.poultryfarm.ui.controllers;

import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import java.util.List;
import java.util.Random;

public class EntityHabitatControllerImpl implements EntityHabitatController {

    private final GraphicEntityView view;
    private final HabitatModel model;

    public EntityHabitatControllerImpl(GraphicEntityView view, HabitatModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void removeEntityById(String id) {
        view.removeEntity(id);
        model.removeEntity(id);
    }

    @Override
    public void changeEntityMovingState(String id) {
        GraphicEntity entity = view.getEntityById(id);
        if (entity.getDx() == 0 && entity.getDy() == 0) {
            Random random = new Random();
            entity.setDx(random.nextInt(10) - 5);
            entity.setDy(random.nextInt(10) - 5);
        } else {
            entity.setDy(0);
            entity.setDx(0);
        }
    }

    @Override
    public void removeDeadEntities() {
        List<String> deadEntitiesIds = List.copyOf(model.getDeadEntitiesIds(System.currentTimeMillis()));
        for (String id : deadEntitiesIds) {
            removeEntityById(id);
        }
    }
}
