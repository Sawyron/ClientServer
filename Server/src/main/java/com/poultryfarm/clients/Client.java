package com.poultryfarm.clients;

public interface Client {
    void sendData(String data);

    String receiveString(String request);
}
