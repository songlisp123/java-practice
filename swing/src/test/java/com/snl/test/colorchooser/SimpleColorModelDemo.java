package com.snl.test.colorchooser;

import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleColorModelDemo implements ColorSelectionModel {

    protected Color color;
    protected final List<ChangeListener> listeners = new ArrayList<>();


    @Override
    public Color getSelectedColor() {
        return color;
    }

    @Override
    public void setSelectedColor(Color color) {
        System.out.println("设置颜色："+color);
        this.color = color;
        fireStateChange();

    }

    private void fireStateChange() {
        System.out.println("触发事件");
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(event);
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
}
