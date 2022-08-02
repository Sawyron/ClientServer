package com.poultryfarm.controllers;

import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public abstract class AbstractEntityController implements Controller {
    protected final GraphicEntityView view;

    public AbstractEntityController(GraphicEntityView view) {
        this.view = view;
        connectObservers();
    }

    private void connectObservers() {
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
        view.run();
    }

    public void start() {
    }

    public void stop() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void onEntityRightClick(String id) {
    }

    public void onEntityLeftClick(String id) {
    }

    public void onAreaRightClick(int x, int y) {
    }

    public void onAreaLeftClick(int x, int y) {
    }

    public void loadEntities(File file) {
    }

    public void saveEntities(File file) {
    }

    public void sendEntitiesToServer() {
    }

    public void receiveEntitiesFromServer() {
    }

    public void getEntityFromServerByIndex(int index) {
    }

    public void removeEntityFromServerByIndex(int index) {
    }

    public void countEntitiesOnServer() {
    }

    public void onServerChanged(String serverName) {
    }

    public void onExit() {
    }
}
