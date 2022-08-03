package com.poultryfarm.controllers;

import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.domain.TransferEntity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferEntityControllerImpl implements TransferEntityController {

    private final GraphicEntityView view;
    private final HabitatModel model;
    private final EntitySpawnController spawnController;


    public TransferEntityControllerImpl(GraphicEntityView view, HabitatModel model, EntitySpawnController spawnController, TaskExecutor executor) {
        this.view = view;
        this.model = model;
        this.spawnController = spawnController;
    }

    @Override
    public void addTransferEntities(Collection<TransferEntity> transferEntities) {
        for (TransferEntity transferEntity : transferEntities) {
            spawnController.spawnEntity(
                    transferEntity.getX(),
                    transferEntity.getY(),
                    transferEntity.getDx(),
                    transferEntity.getDy(),
                    transferEntity.getType()
            );
        }
    }

    @Override
    public List<TransferEntity> getTransferEntities() {
        Collection<AliveEntity> aliveEntities = model.getEntities();
        return aliveEntities.stream()
                .map(aliveEntity -> {
                    GraphicEntity graphicEntity = view.getEntityById(aliveEntity.getId());
                    return new TransferEntity(
                            graphicEntity.getX(),
                            graphicEntity.getY(),
                            graphicEntity.getDx(),
                            graphicEntity.getDy(),
                            aliveEntity.getType()
                    );
                }).collect(Collectors.toCollection(LinkedList::new));
    }
}
