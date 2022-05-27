package com.poultryfarm.server;

@FunctionalInterface
public interface RequestDataHandler {
    void handle(String data);
}
