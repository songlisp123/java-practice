package com.snl.swing.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

public class CubicCurveMaker extends ShapeMaker {

    public CubicCurveMaker() {
        super(4);
    }

    @Override
    Shape setShape(Point2D[] p) {
        return new CubicCurve2D.Double(p[0].getX(),p[0].getY(),p[1].getX(),p[1].getY(),
                p[2].getX(),p[2].getY(),p[3].getX(),p[3].getY());
    }
}
