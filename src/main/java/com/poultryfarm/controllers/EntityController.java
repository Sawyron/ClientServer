package com.poultryfarm.controllers;

import com.poultryfarm.domain.AliveEntity;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.EntitySpawn;
import com.poultryfarm.services.GraphicEntityFactory;
import com.poultryfarm.services.RunnableWorker;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityController {
    private final HabitatModel model;
    private final GraphicEntityView view;
    private final List<EntitySpawn> entitySpawns = new LinkedList<>();

    private final List<RunnableWorker> workers = new LinkedList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private long viewUpdatePeriodInMs = 20;
    private long movementPeriodInMs = 50;
    private long checkDeadPeriod = 500;
    private long pauseTime;

    public EntityController(HabitatModel model, GraphicEntityView view) {
        this.model = model;
        this.view = view;

        view.addStartActionListener((e) -> {
            resume();
            synchronized (model) {
                model.clear();
            }
            synchronized (view) {
                view.clearEntities();
            }
        });
        view.addStopActionListener((e) -> pause());
        view.addPauseActionListener((e) -> pause());
        view.addResumeActionLister((e) -> resume());
        view.addEntityRightButtonClickedListener(this::removeEntityById);
        view.addAreaPointLeftButtonClickedListener(this::spawnRandomEntityAt);
        view.addEntityLeftButtonClickedListener(this::changeEntityMovingState);
        view.addWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                shutdown();
                System.exit(0);
            }
        });
        workers.add(new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                synchronized (view) {
                    view.moveEntities();
                }
                try {
                    Thread.sleep(movementPeriodInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new ControllerException(e.getMessage(), e);
                }
            }
        });
        workers.add(new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                try {
                    Thread.sleep(checkDeadPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new ControllerException(e.getMessage(), e);
                }
                removeDeadEntities();
            }
        });
    }

    private void changeEntityMovingState(String id) {
        synchronized (view) {
            GraphicEntity entity = view.getEntityById(id);
            if (entity.getDx() == 0 && entity.getDy() == 0) {
                Random random = new Random();
                entity.setDx(random.nextInt(10) - 5);
                entity.setDy(random.nextInt(10) - 5);
            } else {
                entity.setDy(0);
                entity.setDx(0);
            }
        }
    }

    private void spawnRandomEntityAt(int x, int y) {
        Random random = new Random();
        EntitySpawn spawn = entitySpawns.get(random.nextInt(entitySpawns.size()));
        spawnEntity(x, y, spawn);
    }

    public void addEntitySpawn(EntitySpawn entitySpawn) {
        entitySpawns.add(entitySpawn);
    }

    private void spawnEntity(int x, int y, EntitySpawn entitySpawn) {
        String id = UUID.randomUUID().toString();
        synchronized (model) {
            model.addEntity(new AliveEntity(id, entitySpawn.getLifeTimeInMs()));
        }
        Random random = new Random();
        synchronized (view) {
            view.addEntity(
                    entitySpawn.createEntity(x, y, random.nextInt(10) - 5, random.nextInt(10) - 5),
                    id
            );
        }
    }

    private void removeEntityById(String id) {
        synchronized (view) {
            view.removeEntity(id);
        }
        synchronized (model) {
            model.removeEntity(id);
        }
    }

    private void removeDeadEntities() {
        List<String> deadEntitiesIds = List.copyOf(model.getDeadEntitiesIds(System.currentTimeMillis()));
        for (String id : deadEntitiesIds) {
            removeEntityById(id);
        }
    }

    private void shutdown() {
        for (RunnableWorker worker : workers) {
            worker.finish();
        }
        executorService.shutdown();
    }

    private void pause() {
        for (RunnableWorker worker : workers) {
            worker.pause();
        }
        pauseTime = System.currentTimeMillis();
    }

    private void resume() {
        for (RunnableWorker worker : workers) {
            worker.resume();
        }
        synchronized (model) {
            model.increaseLifeTime(System.currentTimeMillis() - pauseTime);
        }
    }

    public void run() {
        for (EntitySpawn entitySpawn : entitySpawns) {
            workers.add(new RunnableWorker() {
                @Override
                protected void doUnitOfWork() {
                    Random random = new Random();
                    spawnEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight()), entitySpawn);
                    try {
                        Thread.sleep(entitySpawn.getSpawnPeriodInMs());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new ControllerException(e.getMessage(), e);
                    }
                }
            });
        }
        view.run();
        for (RunnableWorker worker : workers) {
            worker.pause();
            executorService.submit(worker);
        }
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
}
