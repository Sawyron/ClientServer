package com.poultryfarm.ui.controllers;

public interface EntityViewController {
    void run();

    void start();

    void stop();

    void pause();

    void resume();

    void onEntityRightClick(String id);

    void onEntityLeftClick(String id);

    void onAreaRightClick(int x, int y);

    void onAreaLeftClick(int x, int y);

    void onExit();
}
