package com.poultryfarm.controllers;

import com.poultryfarm.clients.Client;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.serializers.EntitySerializer;

import java.io.File;

public class MainController extends AbstractEntityController {

    private final EntityViewController viewController;
    private final EntityHabitatController habitatController;
    private final FileTransferEntityController fileController;
    private final NetworkEntityController networkController;

    public MainController(GraphicEntityView view,
                          EntityViewController viewController,
                          EntityHabitatController habitatController,
                          FileTransferEntityController fileController,
                          NetworkEntityController networkController) {
        super(view);
        this.viewController = viewController;
        this.habitatController = habitatController;
        this.fileController = fileController;
        this.networkController = networkController;
    }

    @Override
    public void addFileEntitySerializer(String description, String extension, EntitySerializer serializer) {
        fileController.addFileEntitySerializer(description, extension, serializer);
    }

    @Override
    public void addServerClient(Client client, String name) {
        networkController.addServerClient(client, name);
    }

    @Override
    public void run() {
        viewController.run();
    }

    @Override
    public void start() {
        viewController.start();
    }

    @Override
    public void stop() {
        viewController.stop();
    }

    @Override
    public void pause() {
        viewController.pause();
    }

    @Override
    public void resume() {
        viewController.resume();
    }

    @Override
    public void onEntityRightClick(String id) {
        habitatController.removeEntityById(id);
    }

    @Override
    public void onEntityLeftClick(String id) {
        habitatController.changeEntityMovingState(id);
    }

    @Override
    public void onAreaRightClick(int x, int y) {
        super.onAreaRightClick(x, y);
    }

    @Override
    public void onAreaLeftClick(int x, int y) {
        viewController.onAreaLeftClick(x, y);
    }

    @Override
    public void loadEntities(File file) {
        fileController.loadEntities(file);
    }

    @Override
    public void saveEntities(File file) {
        fileController.saveEntities(file);
    }

    @Override
    public void sendEntitiesToServer() {
        networkController.sendEntitiesToServer();
    }

    @Override
    public void receiveEntitiesFromServer() {
        networkController.receiveEntitiesFromServer();
    }

    @Override
    public void getEntityFromServerByIndex(int index) {
        networkController.getEntityFromServerByIndex(index);
    }

    @Override
    public void removeEntityFromServerByIndex(int index) {
        networkController.removeEntityFromServerByIndex(index);
    }

    @Override
    public void countEntitiesOnServer() {
        networkController.countEntitiesOnServer();
    }

    @Override
    public void onServerChanged(String serverName) {
        networkController.onServerChanged(serverName);
    }

    @Override
    public void onExit() {
        viewController.onExit();
    }
}
