package com.snl.swing.practice.combox.model;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontComboxModel implements ComboBoxModel<String> {

    protected Object selectedItem;
    protected final List<ListDataListener> listeners =
            new ArrayList<>();
    protected List<String> items;

    protected int index0;
    protected int index1;


    public FontComboxModel() {
        items = new ArrayList<>();
        String[] availableFontFamilyNames = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        items.addAll(Arrays.stream(availableFontFamilyNames).toList());
        setSelectedItem("楷体");
    }

    @Override
    public void setSelectedItem(Object anItem) {
        Object currentItem = selectedItem;
        if (currentItem != null)
            index0 = getIndex(currentItem);
        index1 = getIndex(anItem);
        selectedItem = anItem;
        fireDataChange();

    }

    private int getIndex(Object o) {
        for (String s : items) {
            if (s.equals(o)) {
                return items.indexOf(o);
            }
        }
        throw new IllegalArgumentException("未找到指定文字格式!");
    }

    private void fireDataChange() {
        ListDataEvent listDataEvent =
                new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(listDataEvent);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public String getElementAt(int index) {
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
