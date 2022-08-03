package com.poultryfarm.controllers;

public interface EntityHabitatController {
    void removeEntityById(String id);

    void changeEntityMovingState(String id);

    void removeDeadEntities();
}
