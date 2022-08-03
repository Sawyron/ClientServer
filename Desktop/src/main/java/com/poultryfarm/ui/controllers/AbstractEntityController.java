package com.poultryfarm.ui.controllers;

import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public abstract class AbstractEntityController implements EntityViewController, FileTransferEntityController, NetworkEntityController {

    public AbstractEntityController(GraphicEntityView view) {
        connectObservers(view);
    }

    private void connectObservers(GraphicEntityView view) {
        view.addStartActionListener(e -> start());
        view.addStopActionListener(e -> stop());
        view.addPauseActionListener(e -> pause());
        view.addResumeActionLister(e -> resume());
        view.addEntityRightButtonClickedListener(this::onEntityRightClick);
        view.addEntityLeftButtonClickedListener(this::onEntityLeftClick);
        view.addAreaPointRightButtonClickedListener(this::onAreaRightClick);
        view.addAreaPointLeftButtonClickedListener(this::onAreaLeftClick);
        view.addLoadEntityListener(this::loadEntities);
        view.addSaveEntityListener(this::saveEntities);
        view.addSendEntitiesActionListener(e -> sendEntitiesToServer());
        view.addReceiveEntitiesActionListener(e -> receiveEntitiesFromServer());
        view.addGetEntityIndexListener(this::getEntityFromServerByIndex);
        view.addRemovingEntityIndexListener(this::removeEntityFromServerByIndex);
        view.addCountButtonActionListener(e -> countEntitiesOnServer());
        view.addServerNameListener(this::onServerChanged);
        view.addWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onExit();
            }
        });
    }

    @Override
    public void run() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void onEntityRightClick(String id) {
    }

    @Override
    public void onEntityLeftClick(String id) {
    }

    @Override
    public void onAreaRightClick(int x, int y) {
    }

    @Override
    public void onAreaLeftClick(int x, int y) {
    }

    @Override
    public void loadEntities(File file) {
    }

    @Override
    public void saveEntities(File file) {
    }

    @Override
    public void sendEntitiesToServer() {
    }

    @Override
    public void receiveEntitiesFromServer() {
    }

    @Override
    public void getEntityFromServerByIndex(int index) {
    }

    @Override
    public void removeEntityFromServerByIndex(int index) {
    }

    @Override
    public void countEntitiesOnServer() {
    }

    @Override
    public void onServerChanged(String serverName) {
    }

    @Override
    public void onExit() {
    }
}
