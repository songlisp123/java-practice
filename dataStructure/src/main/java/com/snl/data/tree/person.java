package com.snl.data.tree;

import java.awt.*;

public class person {
    private int height;     // 实际上是 key
    private Color color;
    public person(int h, Color c) {
        this.height = h;
        this.color = c;
    }
    public int getHeight() {
        return height;
    }
    public Color getColor() {
        return color;
    }
}
