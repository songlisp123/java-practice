package com.snl.swing.homework01.data;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.BitSet;
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

    private BitSet value = new BitSet(32); //这个字眼什么意思?

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
        int clearMin = this.minIndex;
        int clearMax = this.maxIndex;
        int setMin = Math.min(index0,index1);
        int setMax = Math.max(index0,index1);
        //TODO
        changeSelection(clearMin, clearMax, setMin, setMax);
    }

    private void changeSelection(int clearMin, int clearMax, int setMin, int setMax) {
        this.changeSelection(clearMin,clearMax,setMin,setMax,true);
    }

    private void changeSelection(int clearMin, int clearMax, int setMin, int setMax, boolean clearFirst) {
        for (int i = Math.min(setMin,clearMin);i<= Math.max(clearMax,setMax);i++) {
            boolean shouldClear = contains(clearMin,clearMax,i);
            boolean shouldSet = contains(setMin,setMax,i);

            if (shouldClear && shouldSet) {
                //如果应该清除和设置
                if (clearFirst)
                    shouldClear = false;
                else
                    shouldSet = false;
            }

            if (shouldClear) {
                //TODO
                clear(i);
            }
            if (shouldSet) {
                //TODO
                set(i);
            }
            //阻止索引越界
            if (i == MAX) {
                break;
            }
        }
        //TODO 触发事件变更通知
    }

    private void clear(int r) {
        if (!value.get(r))
            return;
        value.clear(r);
        markAsDirty(r);

        if (r == minIndex && minIndex < MAX) {
            //如果r等于最小值
            for (minIndex = minIndex + 1 ;minIndex < maxIndex;minIndex++) {
                //更新最小索引，当发现最小索引的时候？？？
                if (value.get(minIndex))
                    break;
            }
        }

        if (r == maxIndex) {
            //如果当前值等于最大索引,减小索引
            for (maxIndex = maxIndex -1 ;minIndex <= maxIndex;maxIndex--) {
                if (value.get(maxIndex))
                    break;
            }
        }

        if (isSelectionEmpty())
        {
            //如果选择区间为空
            minIndex = MAX;
            maxIndex = MIN;
        }
    }

    private void set(int r) {
        if (value.get(r)) {
            return;
        }
        value.set(r);
        markAsDirty(r);

        //更新最大最小索引
        minIndex = Math.min(minIndex,r);
        maxIndex = Math.max(maxIndex,r);
    }

    private boolean contains(int a, int b, int i) {
        return (i >= a) && (i<=b);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        if (index0 == -1 || index1 == -1)
            return;
        if (getSelectionMode() == SINGLE_SELECTION)
        {
            //如果是单选模式
            setSelectionInterval(index0,index1);
            return;
        }

        //否则
        updateLeadAnchorIndices(index0,index1);

        int clearMin = MAX;
        int clearMax = MIN;
        int setMin = Math.min(index0,index1);
        int setMax = Math.max(index0,index1);

        if (getSelectionMode() == SINGLE_INTERVAL_SELECTION &&
                (setMax < minIndex -1 || setMin > maxIndex - 1)) {
            setSelectionInterval(index0,index1);
            return;
        }

        changeSelection(clearMin,clearMax,setMin,setMax);
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
        this.removeIndexIntervalImpl(index0,index1,true);
    }

    private void removeIndexIntervalImpl(int index0, int index1, boolean changeLeadAnchor) {
        if (index0 == -1 || index1 == -1)
            return;
        if (changeLeadAnchor) {
            updateLeadAnchorIndices(index0, index1);
        }
        int clearMin = Math.min(index0, index1);
        int clearMax = Math.max(index0, index1);
        int setMin = MAX;
        int setMax = MIN;
        if (getSelectionMode() != MULTIPLE_INTERVAL_SELECTION &&
                clearMin > minIndex && clearMax < maxIndex) {
            clearMax = maxIndex;
        }

        changeSelection(clearMin, clearMax, setMin, setMax);
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
        return minIndex > maxIndex;
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
