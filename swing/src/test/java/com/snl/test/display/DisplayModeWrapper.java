package com.snl.test.display;

import com.snl.test.frame.util.Utils;

import java.awt.*;

/**
 * 显示模式包装类，包装了当前设备的所有显示模式
 * 和当前的显示模式
 */
public class DisplayModeWrapper {

    /**
     * 显示模式数组
     */
    private DisplayMode[] modes;
    /**
     * 当前显示模式
     */
    private DisplayMode currentDisplayMode;

    public DisplayModeWrapper() {
        setModes(Utils.listAllDisplayModes());
        setCurrentDisplayMode(Utils.getCurrentDisplayMode());
    }

    public DisplayMode[] getModes() {
        return modes;
    }

    public void setModes(DisplayMode[] modes) {
        if (modes == null || modes.length == 0)
            return;
        this.modes = modes;
    }

    public DisplayMode getCurrentDisplayMode() {
        return currentDisplayMode;
    }

    public void setCurrentDisplayMode(DisplayMode currentDisplayMode) {
        if (currentDisplayMode == null)
            return;
        this.currentDisplayMode = currentDisplayMode;
    }

    public int getIndexOfDisplayMode(DisplayMode mode) {
        DisplayMode[] modes = getModes();
        int result = -1 ,i;
        if (modes == null || modes.length == 0)
            return result;
        for (i = 0;i<modes.length;i++)
        {
            if (modes[i].equals(mode)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public int getIndexOfCurrentDisplayMode() {
        return getIndexOfDisplayMode(currentDisplayMode);
    }

    public int size() {
        return modes.length;
    }

    public DisplayMode getIndex(int index) {
        if (index < 0|| index > size())
            throw new ArrayIndexOutOfBoundsException("数组超出边界");
        return modes[index];
    }
}
