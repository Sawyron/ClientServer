package com.poultryfarm.controllers;

import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;

public class EntityViewControllerImpl implements EntityViewController {

    private final GraphicEntityView view;
    private final HabitatModel model;
    private final EntityHabitatController habitatController;
    private final EntitySpawnController spawnController;
    private final TaskExecutor executor;
    private long viewUpdatePeriodInMs = 20;
    private long movementPeriodInMs = 50;
    private long checkDeadPeriod = 500;
    private long pauseTime;

    public EntityViewControllerImpl(GraphicEntityView view,
                                    HabitatModel model,
                                    EntityHabitatController habitatController,
                                    EntitySpawnController spawnController,
                                    TaskExecutor executor) {
        this.view = view;
        this.model = model;
        this.habitatController = habitatController;
        this.spawnController = spawnController;
        this.executor = executor;

    }

    public long getViewUpdatePeriodInMs() {
        return viewUpdatePeriodInMs;
    }

    public void setViewUpdatePeriodInMs(long viewUpdatePeriodInMs) {
        this.viewUpdatePeriodInMs = viewUpdatePeriodInMs;
    }

    public long getMovementPeriodInMs() {
        return movementPeriodInMs;
    }

    public void setMovementPeriodInMs(long movementPeriodInMs) {
        this.movementPeriodInMs = movementPeriodInMs;
    }

    public long getCheckDeadPeriod() {
        return checkDeadPeriod;
    }

    public void setCheckDeadPeriod(long checkDeadPeriod) {
        this.checkDeadPeriod = checkDeadPeriod;
    }

    @Override
    public void run() {
        executor.addRepeatedTask(view::moveEntities, viewUpdatePeriodInMs);
        executor.addRepeatedTask(habitatController::removeDeadEntities, movementPeriodInMs);
        spawnController.addSpawnTasksToExecutor(executor);
        view.run();
    }

    @Override
    public void start() {
        view.setActiveState();
        executor.runRepeatedTasks();
        resume();
        synchronized (model) {
            model.clear();
        }
        synchronized (view) {
            view.clearEntities();
        }
    }

    @Override
    public void stop() {
        pause();
        view.setStoppedState();
    }

    @Override
    public void pause() {
        executor.pauseRepeatedTasks();
        view.setPausedState();
        pauseTime = System.currentTimeMillis();
    }

    @Override
    public void resume() {
        executor.resumeRepeatedTasks();
        view.setResumedState();
        synchronized (model) {
            model.increaseLifeTime(System.currentTimeMillis() - pauseTime);
        }
    }

    @Override
    public void onEntityRightClick(String id) {
        habitatController.removeEntityById(id);
    }

    @Override
    public void onEntityLeftClick(String id) {
        habitatController.changeEntityMovingState(id);
    }

    @Override
    public void onAreaRightClick(int x, int y) {

    }

    @Override
    public void onAreaLeftClick(int x, int y) {
        spawnController.spawnRandomEntity(x, y);
    }

    @Override
    public void onExit() {
        executor.shutdown();
    }
}
