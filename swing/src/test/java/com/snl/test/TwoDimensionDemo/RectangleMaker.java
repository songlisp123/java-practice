package com.snl.test.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RectangleMaker extends ShapeMaker {

    public RectangleMaker() {
        super(2);
    }

    @Override
    Shape setShape(Point2D[] p) {
        Rectangle2D.Double rec = new Rectangle2D.Double();
        rec.setFrameFromDiagonal(p[0],p[1]);
        return rec;
    }
}
