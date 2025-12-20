package com.snl.test.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

public class QuardCurveMaker extends ShapeMaker {

    public QuardCurveMaker() {
        super(3);
    }

    @Override
    Shape setShape(Point2D[] p) {
        return new QuadCurve2D.Double(p[0].getX(),p[0].getY(),p[1].getX(),p[1].getY(),
                p[2].getX(),p[2].getY());
    }
}
