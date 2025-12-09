package com.snl.test.table.editor.prictice;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class SimpleListSelectModeDemo implements ListSelectionModel  {

    /**
     * 选择模型，默认以下三种
     */
    protected int selectionModel;
    /**
     * 选取可能最大索引
     */
    protected int maxIndex;
    /**
     * 选取的可能最小索引
     */
    protected int minIndex;
    /**
     * 监听器列表
     */
    protected final List<ListSelectionListener> listeners =
            new ArrayList<>();
    /**
     * 返回最近一次调用 setSelectionInterval()、
     * addSelectionInterval()或
     * removeSelectionInterval()方法时传入的第一个索引值。
     */
    protected int anchorSelectionIndex;
    /**
     * 从最近一次调用 setSelectionInterval()、
     * addSelectionInterval()或
     * removeSelectionInterval()时，返回第二个索引参数。
     */
    protected int leadSelectionIndex;
    /**
     * 值是否因用户改变
     * 设置 valueIsAdjusting 属性，该属性用于指示后续的选择更改是否应被视为一次整体更改。
     */
    protected boolean valueIsAdjusting;


    public SimpleListSelectModeDemo() {
        init();
    }

    private void init() {
        selectionModel = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
        valueIsAdjusting = false;
        //初始化索引
        anchorSelectionIndex = -1;
        leadSelectionIndex = -1;
    }


    @Override
    public void setSelectionInterval(int index0, int index1) {

    }

    @Override
    public void addSelectionInterval(int index0, int index1) {

    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {

    }

    /**
     * 获取第一个选择索引
     * @return {@code minIndex} 或者当选择为空的时候返回-1
     */
    @Override
    public int getMinSelectionIndex() {
        return isSelectionEmpty() ? -1 : minIndex;
    }

    /**
     * 获取选择区的最后一个索引
     * @return {@code maxIndex}或者选择为空时，返回-1
     */
    @Override
    public int getMaxSelectionIndex() {
        return isSelectionEmpty() ? -1 : maxIndex;
    }

    @Override
    public boolean isSelectedIndex(int index) {
        return false;
    }

    @Override
    public int getAnchorSelectionIndex() {
        return 0;
    }

    @Override
    public void setAnchorSelectionIndex(int index) {

    }

    @Override
    public int getLeadSelectionIndex() {
        return 0;
    }

    @Override
    public void setLeadSelectionIndex(int index) {

    }

    @Override
    public void clearSelection() {

    }

    @Override
    public boolean isSelectionEmpty() {
        return (minIndex > maxIndex);
    }

    @Override
    public void insertIndexInterval(int index, int length, boolean before) {

    }

    @Override
    public void removeIndexInterval(int index0, int index1) {

    }

    @Override
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        this.valueIsAdjusting = valueIsAdjusting;
    }

    @Override
    public boolean getValueIsAdjusting() {
        return valueIsAdjusting;
    }

    @Override
    public void setSelectionMode(int selectionMode) {
        this.selectionModel = selectionMode;
    }

    @Override
    public int getSelectionMode() {
        return selectionModel;
    }

    @Override
    public void addListSelectionListener(ListSelectionListener x) {
        listeners.add(x);
    }

    @Override
    public void removeListSelectionListener(ListSelectionListener x) {
        listeners.remove(x);
    }
}
