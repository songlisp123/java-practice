package com.snl.test.music.processBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class RangeBoundModelModelImplement implements BoundedRangeModel {

    protected int min;
    protected int max;
    protected int extent;
    protected final int DEFAULT_STEP = 100;
    protected int currentValue;
    protected boolean isAdjusting;

    protected final List<ChangeListener> listeners =
            new ArrayList<>();

    public RangeBoundModelModelImplement() {
        extent = 0;
        this.min = 0;
        this.max = this.min + DEFAULT_STEP;
    }

    public RangeBoundModelModelImplement(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public RangeBoundModelModelImplement(int min) {
        this.min = min;
        this.max = min + DEFAULT_STEP;
    }

    @Override
    public int getMinimum() {
        return min;
    }

    @Override
    public void setMinimum(int newMinimum) {
        this.min = newMinimum;
    }

    @Override
    public int getMaximum() {
        return max;
    }

    @Override
    public void setMaximum(int newMaximum) {
        this.max = newMaximum;
    }

    @Override
    public int getValue() {
        return currentValue;
    }

    @Override
    public void setValue(int newValue) {
        if (newValue + extent > max ||
                newValue + extent < min) {
            System.err.println("参数错误，请重新赋值");
        }else {
            this.currentValue = newValue;
            fireChangeEvent();
        }
    }

    private void fireChangeEvent() {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(changeEvent);
        }
    }

    @Override
    public void setValueIsAdjusting(boolean b) {
        isAdjusting = b;
    }

    @Override
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    @Override
    public int getExtent() {
        return extent;
    }

    @Override
    public void setExtent(int newExtent) {
        //判断逻辑
        this.extent = newExtent;
    }

    @Override
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {

    }

    @Override
    public void addChangeListener(ChangeListener x) {
        listeners.add(x);
    }

    @Override
    public void removeChangeListener(ChangeListener x) {
        listeners.remove(x);
    }

    @Override
    public String toString() {
        return "SimpleRangeBoundModelModel{" +
                "min=" + min +
                ", max=" + max +
                ", extent=" + extent +
                ", DEFAULT_STEP=" + DEFAULT_STEP +
                ", currentValue=" + currentValue +
                ", isAdjusting=" + isAdjusting +
                ", listeners=" + listeners +
                '}';
    }
}
