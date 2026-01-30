package com.snl.swing.TwoDimensionDemo;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomComboBoxModel implements ComboBoxModel<ShapeMaker> {

    protected Object currentItem;
    protected final List<ShapeMaker> items = new ArrayList<>();
    protected final List<ListDataListener> listeners =
            new ArrayList<>();
    protected int index0 = -1;
    protected int index1 = -1;

    protected final ShapeMaker DEFAULT = new LineMaker();

    public CustomComboBoxModel() {
        items.add(DEFAULT);
        items.add(new RectangleMaker());
        items.add(new EliipseMaker());
        items.add(new RoundRectangleMaker());
        items.add(new PolygonMaker());
        items.add(new QuardCurveMaker());
        items.add(new CubicCurveMaker());
        items.add(new ArcMaker());
        setSelectedItem(DEFAULT);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        Object old = currentItem;
        if (old != null)
            index0 = getIndex(old);
        if (anItem != null)
            index1 = getIndex(anItem);
        currentItem = anItem;
        //TODO 触发事件
        fireChangeData();
    }

    private int getIndex(Object old) {
        for (ShapeMaker item : items) {
            if (Objects.equals(old,item)) {
                return items.indexOf(item);
            }
        }
        throw new IllegalArgumentException("未找到条目！");
    }

    private void fireChangeData() {
        ListDataEvent dataEvent =
                new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(dataEvent);
        }
    }

    @Override
    public Object getSelectedItem() {
        return currentItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public ShapeMaker getElementAt(int index) {
        if (index < 0 || index > getSize())
            throw new IllegalArgumentException("非法参数异常");
        return items.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
