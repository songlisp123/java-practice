package com.snl.test.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineMaker extends ShapeMaker {

    public LineMaker() {
        super(2);
    }

    @Override
    Shape setShape(Point2D[] point) {
        return new Line2D.Double(point[0],point[1]);
    }
}
