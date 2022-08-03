package com.poultryfarm.ui.controllers;

public interface EntityHabitatController {
    void removeEntityById(String id);

    void changeEntityMovingState(String id);

    void removeDeadEntities();
}
