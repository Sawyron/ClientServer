package com.poultryfarm.server;

@FunctionalInterface
public interface PostHandler {
    void handle(String data);
}
