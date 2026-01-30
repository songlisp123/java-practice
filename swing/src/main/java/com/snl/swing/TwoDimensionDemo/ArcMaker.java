package com.snl.swing.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.*;

public class ArcMaker extends ShapeMaker {

    public ArcMaker() {
        super(4);
    }

    @Override
    Shape setShape(Point2D[] p) {
        double centerX = (p[0].getX() + p[1].getX()) / 2;
        double centerY = (p[0].getY() + p[1].getY()) / 2;
        double width = Math.abs(p[1].getX() - p[0].getX());
        double height = Math.abs(p[1].getY() - p[0].getY());

        double skewedStartAngle = Math.toDegrees(Math.atan2(-(p[2].getY() - centerY) * width,
                (p[2].getX() - centerX) * height));
        double skewedEndAngle = Math.toDegrees(Math.atan2(-(p[3].getY() - centerY) * width,
                (p[3].getX() - centerX) * height));
        double skewedAngleDifference = skewedEndAngle - skewedStartAngle;
        if (skewedStartAngle < 0) skewedStartAngle += 360;
        if (skewedAngleDifference < 0) skewedAngleDifference += 360;

        var s = new Arc2D.Double(0, 0, 0, 0,
                skewedStartAngle, skewedAngleDifference, Arc2D.OPEN);
        s.setFrameFromDiagonal(p[0], p[1]);

        var g = new GeneralPath();
        g.append(s, false);
        var r = new Rectangle2D.Double();
        r.setFrameFromDiagonal(p[0], p[1]);
        g.append(r, false);
        var center = new Point2D.Double(centerX, centerY);
        g.append(new Line2D.Double(center, p[2]), false);
        g.append(new Line2D.Double(center, p[3]), false);
        return g;
    }
}
