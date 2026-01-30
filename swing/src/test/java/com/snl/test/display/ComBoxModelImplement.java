package com.snl.test.display;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ComBoxModelImplement implements ComboBoxModel<DisplayMode> {

    //监听器
    final List<ListDataListener> listeners = new ArrayList<>();
    //显示模式监听器列表
    final List<DisplayModeInterface> modeInterfaceList =  new ArrayList<>();
    //包装器
    DisplayModeWrapper modeWrapper;

    public ComBoxModelImplement() {
        modeWrapper = new DisplayModeWrapper();
        setSelectedItem(modeWrapper.getCurrentDisplayMode());
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem == null)
            return;
        DisplayMode currentDisplayMode = modeWrapper.getCurrentDisplayMode();
        if (anItem.equals(currentDisplayMode))
            return;
        modeWrapper.setCurrentDisplayMode((DisplayMode) anItem);
        fireEvent();
    }

    @Override
    public Object getSelectedItem() {
        return modeWrapper.getCurrentDisplayMode();
    }

    @Override
    public int getSize() {
        return modeWrapper.size();
    }

    @Override
    public DisplayMode getElementAt(int index) {
        return modeWrapper.getIndex(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void addDisplayListener(DisplayModeInterface l) {
        modeInterfaceList.add(l);
    }

    public void removeDisplayListener(DisplayModeInterface l) {
        modeInterfaceList.remove(l);
    }


    private void fireEvent() {
        for (DisplayModeInterface l : modeInterfaceList)
            l.update();
    }

}
