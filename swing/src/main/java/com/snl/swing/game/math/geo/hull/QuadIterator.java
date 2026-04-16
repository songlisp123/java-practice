package com.snl.swing.game.math.geo.hull;

import com.snl.swing.game.math.Vector2D;

public class QuadIterator implements PathIterator {
    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void next() {

    }

    @Override
    public int currentSegment(Vector2D[] coords) {
        return 0;
    }
}
