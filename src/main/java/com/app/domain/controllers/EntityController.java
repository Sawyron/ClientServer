package com.app.domain.controllers;

import com.app.domain.models.AliveEntity;
import com.app.domain.models.HabitatModel;
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
    private long checkDeadPeriod = 500;
    private long pauseTime;

    public EntityController(HabitatModel model, GraphicEntityView view) {
        this.model = model;
        this.view = view;
        RunnableWorker creator = new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                createEntity();
            }
        };
        workers.add(creator);
        workers.add(new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                removeDeadEntities();
                try {
                    Thread.sleep(checkDeadPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        view.addStartActionListener((e) -> {
            resume();
            model.clear();
            view.clearEntities();
        });
        view.addStopActionListener((e) -> pause());
        view.addPauseActionListener((e) -> pause());
        view.addResumeActionLister((e) -> resume());

        view.addWindowAction(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                shutdown();
                System.exit(0);
            }
        });

        start();
    }

    public void addAliveEntityType(GraphicEntityFactory factory, long spawnPeriod) {
        entityFactoryMap.put(factory, spawnPeriod);
        checkDeadPeriod = Math.max(spawnPeriod, checkDeadPeriod);
    }

    private void createEntity() {
        for (Map.Entry<GraphicEntityFactory, Long> pair : entityFactoryMap.entrySet()) {
            String id = UUID.randomUUID().toString();
            long spawnPeriod = pair.getValue();
            GraphicEntityFactory factory = pair.getKey();
            model.addEntity(new AliveEntity(id, factory.getEntityLifeTimeInMs()));
            Random random = new Random();
            view.addEntity(
                    factory.createEntity(random.nextInt(view.getWidth()), random.nextInt(view.getHeight())),
                    id
            );
            try {
                Thread.sleep(spawnPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeDeadEntities() {
        List<String> deadEntitiesIds = List.copyOf(model.getDeadEntitiesIds(System.currentTimeMillis()));
        synchronized (view) {
            for (String id : deadEntitiesIds) {
                view.removeEntity(id);
            }
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

    private void start() {
        model.clear();
        view.clearEntities();
        for (RunnableWorker worker : workers) {
            worker.pause();
            executorService.submit(worker);
        }
    }

    private void resume() {
        for (RunnableWorker worker : workers) {
            worker.resume();
        }
        model.increaseLifeTime(System.currentTimeMillis() - pauseTime);
    }

    public void run() {
        view.run();
    }
}
