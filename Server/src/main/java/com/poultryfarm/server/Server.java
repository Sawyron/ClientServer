package com.poultryfarm.server;

public interface Server {
    void start();

    void close();

    void addGetHandler(String handlerName, GetHandler handler);

    void addPostHandler(String handlerName, PostHandler handler);
}
