package com.snl.swing.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class PolygonMaker extends ShapeMaker {

    public PolygonMaker() {
        super(6);
    }

    @Override
    Shape setShape(Point2D[] p) {
        var path = new GeneralPath();
        path.moveTo(p[0].getX(),p[0].getY());
        for (int i= 1;i<p.length;i++) {
            path.lineTo(p[i].getX(),p[i].getY());
        }
        path.closePath();
        return path;
    }
}
