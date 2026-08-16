package com.snl.swing.game2026;

public final class Time {

    private  long initialed;
    private long current;
    private final long gap;

    public Time(long gap) {
        this.gap = gap;
        init();
    }

    private void init() {
        initialed = System.currentTimeMillis();
        current = initialed;
    }

    public  long getInitialed() {
        return initialed;
    }

    public  long getCurrent() {
        return current;
    }

    public  void  reCalculate() {
        current = System.currentTimeMillis();
    }

    public  boolean isBeyond() {
        return getCurrent() - getInitialed() >= gap;
    }

    public void setInitialed(long initialed) {
        this.initialed = initialed;
    }
}
