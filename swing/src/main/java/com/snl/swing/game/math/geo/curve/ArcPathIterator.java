package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.geo.PathIterator;

public class ArcPathIterator implements PathIterator {
    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void next() {

    }

    @Override
    public int currentSegment(double[] coords) {
        return 0;
    }
}
