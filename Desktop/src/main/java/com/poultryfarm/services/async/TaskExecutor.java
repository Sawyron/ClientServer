package com.poultryfarm.services.async;

import com.poultryfarm.services.ExceptionHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<RunnableWorker> repeatedTaskWorkers = new LinkedList<>();

    private final ExceptionHandler exceptionHandler;

    public TaskExecutor(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void executeTask(Runnable task) {
        executorService.submit(task);
    }

    public void addRepeatedTask(Runnable task, long period) {
        RunnableWorker worker = new RunnableWorker() {
            @Override
            protected void doUnitOfWork() {
                try {
                    Thread.sleep(period);
                } catch (InterruptedException e) {
                    exceptionHandler.handle(e);
                }
                task.run();
            }
        };
        repeatedTaskWorkers.add(worker);
    }

    public void runRepeatedTasks() {
        for (RunnableWorker worker : repeatedTaskWorkers) {
            executorService.submit(worker);
        }
    }

    public void pauseRepeatedTasks() {
        for (RunnableWorker worker : repeatedTaskWorkers) {
            worker.pause();
        }
    }

    public void resumeRepeatedTasks() {
        for (RunnableWorker worker : repeatedTaskWorkers) {
            worker.resume();
        }
    }

    public void shutdown() {
        for (RunnableWorker worker : repeatedTaskWorkers) {
            worker.finish();
        }
        executorService.shutdown();
    }
}
