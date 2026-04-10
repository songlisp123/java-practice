package com.snl.swing.game.math.geo;

public abstract class Feature {

    public static final int NOT_INDEXED = -1;
    final int index;

    public Feature(int index) {
        this.index = index;
    }


    public int getIndex() {
        return index;
    }
}
