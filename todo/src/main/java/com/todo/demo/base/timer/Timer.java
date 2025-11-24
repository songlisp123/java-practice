package com.todo.demo.base.timer;

import java.util.concurrent.ArrayBlockingQueue;

public class Timer<T> {

    private final ArrayBlockingQueue<T> actionQueue;

    public Timer(int number) {
        this.actionQueue = new ArrayBlockingQueue<>(number);
    }

    public void addAction(T t) throws InterruptedException {
        if (actionQueue.remainingCapacity()<1) {
            actionQueue.take();
        }
        actionQueue.put(t);
    }

    public ArrayBlockingQueue<T> get() {
        return actionQueue;
    }

}
