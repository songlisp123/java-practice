package com.snl.test.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class RoundRectangleMaker extends ShapeMaker {

    public RoundRectangleMaker() {
        super(2);
    }

    @Override
    Shape setShape(Point2D[] p) {
        RoundRectangle2D.Double aDouble = new RoundRectangle2D.Double(0,0,0,0,20,20);
        aDouble.setFrameFromDiagonal(p[0],p[1]);
        return aDouble;
    }
}
