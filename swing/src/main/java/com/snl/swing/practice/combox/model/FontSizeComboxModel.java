package com.snl.swing.practice.combox.model;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FontSizeComboxModel implements ComboBoxModel<String> {

    protected final List<String> data =
            new ArrayList<>();

    protected Object currentItem;
    protected final List<ListDataListener> listeners =
            new ArrayList<>();

    protected int index0;
    protected int index1;

    public FontSizeComboxModel() {
        for (int i = 15;i<=100;i++) {
            data.add(i+"");
        }
        setSelectedItem("20");
    }

    @Override
    public void setSelectedItem(Object anItem) {
        //TODO 手动触发事件
        Object oldSelected = currentItem;
        if (oldSelected != null)
            index0 = getIndex(oldSelected);
        index1 = getIndex(anItem);
        currentItem = anItem;

        fireDateEvent();
    }

    private void fireDateEvent() {
        ListDataEvent listDataEvent =
                new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(listDataEvent);
        }
    }

    private int getIndex(Object item) {
        for (String i : data) {
            if (Objects.equals(i,item)) return data.indexOf(item);
        }
        throw new IllegalArgumentException("暂未找到该选项");
    }

    @Override
    public Object getSelectedItem() {
        return currentItem;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        if (index > getSize()) {
            throw new IllegalArgumentException("超出索引边界");
        }
        return data.get(index);
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
