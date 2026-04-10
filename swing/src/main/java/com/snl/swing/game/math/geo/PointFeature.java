package com.snl.swing.game.math.geo;

import com.snl.swing.game.math.Vector2D;

public class PointFeature extends Feature {

    final Vector2D point;

    public PointFeature(Vector2D point) {
        this(-1,point);
    }

    public PointFeature(int index, Vector2D point) {
        super(index);
        this.point = point;
    }

    public Vector2D getPoint() {
        return point;
    }
}
