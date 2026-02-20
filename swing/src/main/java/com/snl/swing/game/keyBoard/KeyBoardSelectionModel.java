package com.snl.swing.game.keyBoard;

public class KeyBoardSelectionModel {

    private int clickedIndex = -1; //索引
    private int maskIndex = -1; //索引
    private final int total; //全部数量

    public KeyBoardSelectionModel(int total) {
        this.total = total;
    }

    public void addClickedIndex(int delta) {
        clickedIndex += delta;
        clickedIndex = Math.max(0,Math.min(clickedIndex,total-1));
    }

    public int getClickedIndex() {
        return clickedIndex;
    }

    public int getMaskIndex() {
        return maskIndex;
    }

    public void setClickedIndex(int clickedIndex) {
        if (clickedIndex < 0 || clickedIndex >= total)
            throw new ArrayIndexOutOfBoundsException("数组边界异常");
        this.clickedIndex = clickedIndex;
    }

    public void setMaskIndex(int maskIndex) {
        if (maskIndex < 0 || maskIndex >= total)
            throw new ArrayIndexOutOfBoundsException("数组边界异常");
        this.maskIndex = maskIndex;
    }

    public void resetMaskIndex() {
        maskIndex = -1;
    }
}
