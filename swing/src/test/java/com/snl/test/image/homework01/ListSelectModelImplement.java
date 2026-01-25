package com.snl.test.image.homework01;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class ListSelectModelImplement implements ListSelectionModel {


    private final List<ListSelectionListener> listeners = new ArrayList<>();
    private int selectionMode; //选择模型

    private static final int MIN = -1;
    private static final int MAX = Integer.MAX_VALUE;
    private int minIndex = MAX;
    private int maxIndex = MIN;
    private boolean isAdjusting = false;
    private int anchorIndex = -1;
    private int leadIndex = -1;
    protected boolean leadAnchorNotificationEnabled = true;
    private int firstAdjustedIndex = MAX;
    private int lastAdjustedIndex = MIN;

    private int firstChangedIndex = MAX;
    private int lastChangedIndex = MIN;

    private void updateLeadAnchorIndices(int anchorIndex, int leadIndex) {
        if (leadAnchorNotificationEnabled)
        {
            //如果允许通知
            if (this.anchorIndex != anchorIndex) {
                //todo
                markAsDirty(this.anchorIndex);
                markAsDirty(anchorIndex);
            }

            if (this.leadIndex != leadIndex)
            {
                markAsDirty(this.leadIndex);
                markAsDirty(leadIndex);
            }
        }

        this.anchorIndex = anchorIndex;
        this.leadIndex = leadIndex;
    }

    /**
     * 帮助方法:更新第一调整索引和最后调整索引
     * @param r 参数
     */
    private void markAsDirty(int r) {
        if (r == -1)
            return;
        firstAdjustedIndex = Math.min(firstAdjustedIndex,r);
        lastAdjustedIndex = Math.max(lastAdjustedIndex,r);
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        //判断参数是否符合要求
        if (index0 == -1 || index1 == -1)
            return;
        //判断选择模型
        if (getSelectionMode() == SINGLE_SELECTION)
            index0 = index1;
        //更新索引
        updateLeadAnchorIndices(index0,index1);
        //获取最小索引
        int getMinIndex = this.minIndex;
        int getMaxIndex = this.maxIndex;
        int setMin = Math.min(index0,index1);
        int setMax = Math.max(index0,index1);
        //TODO
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {

    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {

    }

    @Override
    public int getMinSelectionIndex() {
        return 0;
    }

    @Override
    public int getMaxSelectionIndex() {
        return 0;
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
        return false;
    }

    @Override
    public void insertIndexInterval(int index, int length, boolean before) {

    }

    @Override
    public void removeIndexInterval(int index0, int index1) {

    }

    @Override
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        isAdjusting = valueIsAdjusting;
    }

    @Override
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    @Override
    public void setSelectionMode(int selectionMode) {
        switch (selectionMode)
        {
            case SINGLE_SELECTION :
            case SINGLE_INTERVAL_SELECTION:
            case MULTIPLE_INTERVAL_SELECTION:
                this.selectionMode = selectionMode;
                break;
            default:
                throw new IllegalArgumentException("非法参数异常");
        }
    }

    @Override
    public int getSelectionMode() {
        return selectionMode;
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
