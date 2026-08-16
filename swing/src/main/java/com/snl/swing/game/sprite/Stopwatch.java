package com.snl.swing.game.sprite;

public class Stopwatch extends Thread {

    static private long _defaultTickInterval = 50;

    private long            start, now;
    double elapsed;
    private long            tickInterval;

    public Stopwatch() {
        this(_defaultTickInterval);
    }

    public Stopwatch(long interval) {
        this.tickInterval = interval;
        start = System.nanoTime();
    }


    public void setTickInterval(long tickInterval) {
        this.tickInterval = tickInterval;
    }

    public void update() {
        now = System.nanoTime();
        elapsed = (now - start) / 1.0E6;
    }

    public double elapsedTime() {
        update();
        return elapsed;
    }

    public void reset() {
        start = System.nanoTime();
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                Thread.sleep(tickInterval,0);
                update();
                System.out.println("elapsed = " + elapsed);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
