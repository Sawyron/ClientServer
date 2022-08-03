package com.poultryfarm.controllers;

import com.poultryfarm.clients.Client;

public interface NetworkEntityController {
    void sendEntitiesToServer();

    void receiveEntitiesFromServer();

    void getEntityFromServerByIndex(int index);

    void removeEntityFromServerByIndex(int index);

    void countEntitiesOnServer();

    void onServerChanged(String serverName);

    void addServerClient(Client client, String name);
}
