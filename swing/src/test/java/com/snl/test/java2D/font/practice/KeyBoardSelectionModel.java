package com.snl.test.java2D.font.practice;

public class KeyBoardSelectionModel {

    int clickedIndex = -1; //索引
    int maskIndex = -1; //索引
    int total; //全部数量

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
        this.clickedIndex = clickedIndex;
    }

    public void setMaskIndex(int maskIndex) {
        this.maskIndex = maskIndex;
    }

    public void resetMaskIndex() {
        maskIndex = -1;
    }
}
