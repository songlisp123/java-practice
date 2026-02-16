package com.snl.test.image.motion;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class ComboxModelDemo implements ComboBoxModel<String> {

    final List<ListDataListener> listeners = new ArrayList<>();
    String[] data;

    String oldItem;
    String newItem;
    int oldIndex,newIndex;

    public ComboxModelDemo(String[] data) {
        this.data = data;
        setSelectedItem(this.data[0]);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        oldItem = newItem;
        if (oldItem != null)
            oldIndex = getIndex(oldItem);
        newItem = anItem.toString();
        newIndex = getIndex(newItem);
        fireEvent(oldIndex,newIndex);
    }

    private int getIndex(String item) {
        for (int i = 0;i<data.length;i++) {
            if (item.equals(data[i]))
                return i;
        }
        return -1;
    }

    private void fireEvent(int oldIndex, int newIndex) {
        if (oldIndex == -1 || oldIndex > getSize() || newIndex == -1 ||
            newIndex > getSize())
            throw new IllegalArgumentException("非法参数异常");
        ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, oldIndex, newIndex);
        for (ListDataListener l : listeners)
            l.contentsChanged(listDataEvent);
    }

    @Override
    public String getSelectedItem() {
        return newItem;
    }

    @Override
    public int getSize() {
        return data.length;
    }

    @Override
    public String getElementAt(int index) {
        if (index <0 || index > getSize())
            throw new ArrayIndexOutOfBoundsException("超出索引边界");
        return data[index];
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
