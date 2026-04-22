package com.snl.swing.game.math.geo;

public interface PathIterator {

    public static final int SEG_MOVE_TO = 0;
    public static final int SEG_LINETO = 1;
    public static final int SEG_QUADTO = 2;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_CLOSE = 4;

    boolean done();
    void next();

    int currentSegment(double[] coords);
}
