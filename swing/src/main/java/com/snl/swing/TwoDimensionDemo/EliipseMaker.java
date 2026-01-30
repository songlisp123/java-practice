package com.snl.swing.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class EliipseMaker extends ShapeMaker {

    public EliipseMaker() {
        super(2);
    }

    @Override
    Shape setShape(Point2D[] p) {
        Ellipse2D.Double e = new Ellipse2D.Double();
        e.setFrameFromDiagonal(p[0],p[1]);
        return e;
    }
}
