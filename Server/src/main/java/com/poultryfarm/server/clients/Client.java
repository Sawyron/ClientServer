package com.poultryfarm.server.clients;

public interface Client {
    void sendData(String data);

    String receiveString(String request);
}
