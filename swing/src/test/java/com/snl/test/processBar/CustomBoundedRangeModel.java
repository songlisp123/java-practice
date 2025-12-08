package com.snl.test.processBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CustomBoundedRangeModel implements BoundedRangeModel , PropertyChangeListener {

    protected int minimum;
    protected int maximum;
    protected int value;
    protected int extent;
    protected boolean flag;
    /**
     * 默认步长
     */
    protected static final int DEFAULT_STEP = 100;

    protected final List<ChangeListener> listeners = new ArrayList<>();


    /**
     *
     * @param minimum 边界范围左区间
     */
    public CustomBoundedRangeModel(int minimum) {
        this.minimum = minimum;
        this.maximum = minimum + DEFAULT_STEP;
        this.extent = 0;
    }

    /**
     *
     * @param minimum 边界范围左区间
     * @param maximum 边界范围右区间
     */
    public CustomBoundedRangeModel(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.extent = 0;
    }

    /**
     *
     * @param minimum 边界范围左区间
     * @param maximum 边界范围右区间
     * @param extent 扩展值
     */
    public CustomBoundedRangeModel(int minimum, int maximum, int extent) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.extent = extent;
    }

    @Override
    public int getMinimum() {
        return minimum;
    }

    @Override
    public void setMinimum(int newMinimum) {
        this.minimum = newMinimum;
    }

    @Override
    public int getMaximum() {
        return maximum;
    }

    @Override
    public void setMaximum(int newMaximum) {
        this.maximum = newMaximum;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int newValue) {
        this.value = newValue;
        //手动调用事件
        System.out.println("value="+this.value);
        System.out.println("开始调用事件");
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(changeEvent);
        }
    }

    @Override
    public void setValueIsAdjusting(boolean b) {
        flag = b;
    }

    @Override
    public boolean getValueIsAdjusting() {
        return true;
    }

    @Override
    public int getExtent() {
        return extent;
    }

    @Override
    public void setExtent(int newExtent) {
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
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
