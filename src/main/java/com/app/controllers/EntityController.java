package com.app.controllers;

import com.app.domain.AliveEntity;
import com.app.domain.GraphicEntity;
import com.app.domain.HabitatModel;
import com.app.services.GraphicEntityFactory;
import com.app.services.RunnableWorker;
import com.app.ui.graphicentity.GraphicEntityView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityController {
    private final HabitatModel model;
    private final GraphicEntityView view;
    private final Map<GraphicEntityFactory, Long> entityFactoryMap = new HashMap<>();

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
        view.addAreaPointLeftButtonClickedListener((x, y) -> {
            List<GraphicEntityFactory> factories = new ArrayList<>(entityFactoryMap.keySet());
            Random random = new Random();
            GraphicEntityFactory factory = factories.get(random.nextInt(factories.size()));
            spawnEntity(x, y, factory);
        });
        view.addEntityLeftButtonClickedListener(id -> {
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
        });


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

    public void addAliveEntityType(GraphicEntityFactory factory, long spawnPeriod) {
        entityFactoryMap.put(factory, spawnPeriod);
        checkDeadPeriod = Math.max(spawnPeriod, checkDeadPeriod);
    }

    private void spawnEntity(int x, int y, GraphicEntityFactory factory) {
        String id = UUID.randomUUID().toString();
        synchronized (model) {
            model.addEntity(new AliveEntity(id, factory.getEntityLifeTimeInMs()));
        }
        Random random = new Random();
        synchronized (view) {
            view.addEntity(
                    factory.createEntity(x, y, random.nextInt(10) - 5, random.nextInt(10) - 5),
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
        for (Map.Entry<GraphicEntityFactory, Long> entry : entityFactoryMap.entrySet()) {
            workers.add(new RunnableWorker() {
                @Override
                protected void doUnitOfWork() {
                    Random random = new Random();
                    spawnEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight()), entry.getKey());
                    try {
                        Thread.sleep(entry.getValue());
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
}
