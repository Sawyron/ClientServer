package com.poultryfarm.controllers;

import com.poultryfarm.clients.Client;
import com.poultryfarm.services.ExceptionHandler;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.services.network.EntityClient;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.domain.TransferEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkEntityControllerImpl implements NetworkEntityController {

    private final GraphicEntityView view;
    private final EntityClient entityClient;
    private final TransferEntityController transferController;
    private final TaskExecutor executor;

    private final MessageService messageService;
    private final ExceptionHandler exceptionHandler;
    private final Map<String, Client> clientMap = new HashMap<>();

    public NetworkEntityControllerImpl(GraphicEntityView view,
                                       EntityClient entityClient,
                                       TransferEntityController transferController,
                                       TaskExecutor executor,
                                       MessageService messageService,
                                       ExceptionHandler exceptionHandler) {
        this.view = view;
        this.entityClient = entityClient;
        this.transferController = transferController;
        this.executor = executor;
        this.messageService = messageService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void sendEntitiesToServer() {
        executor.executeTask(() -> {
            try {
                List<TransferEntity> transferEntities = transferController.getTransferEntities();
                entityClient.sendEntities(transferEntities);
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        });
    }

    @Override
    public void receiveEntitiesFromServer() {
        executor.executeTask(() -> {
            try {
                List<TransferEntity> transferEntities = entityClient.receiveEntities();
                transferController.addTransferEntities(transferEntities);
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        });
    }

    @Override
    public void getEntityFromServerByIndex(int index) {
        executor.executeTask(() -> {
            try {
                TransferEntity transferEntity = entityClient.getEntityAt(index);
                if (transferEntity.getType().equalsIgnoreCase("null")) {
                    return;
                }
                transferController.addTransferEntities(List.of(transferEntity));
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        });
    }

    @Override
    public void removeEntityFromServerByIndex(int index) {
        executor.executeTask(() -> {
            try {
                entityClient.removeAt(index);
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        });
    }

    @Override
    public void countEntitiesOnServer() {
        executor.executeTask(() -> {
            try {
                messageService.showMessage("Entities at server: " + entityClient.getEntitiesCount());
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        });
    }

    @Override
    public void onServerChanged(String serverName) {
        Client client = clientMap.get(serverName);
        if (client != null) {
            entityClient.setClient(client);
        }
    }

    @Override
    public void addServerClient(Client client, String name) {
        clientMap.put(name, client);
        view.addServerName(name);
    }
}
