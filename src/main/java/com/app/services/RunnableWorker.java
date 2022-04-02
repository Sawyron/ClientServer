package com.app.services;

public abstract class RunnableWorker implements Runnable {
    private volatile boolean stopped;
    private boolean done;

    protected abstract void doUnitOfWork();

    public void pause() {
        stopped = true;
    }

    public synchronized void resume() {
        stopped = false;
        notifyAll();
    }

    public void finish() {
        done = true;
    }

    @Override
    public final void run() {
        while (!done) {
            if (!stopped) {
                doUnitOfWork();
            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isStopped() {
        return stopped;
    }
}
