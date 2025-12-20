package com.snl.test.TwoDimensionDemo.transitionDemo;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SimpleRectangleDemo extends Rectangle2D.Double {

    protected Rectangle2D bound;

    public SimpleRectangleDemo(double x, double y, double w, double h) {
        super(x, y, w, h);
        bound = this.getBounds2D();
    }

    protected Point2D getCenterPoint() {
        double centerX = getCenterX();
        double centerY = getCenterY();
        return new Point2D.Double(centerX,centerY);
    }

    protected Point2D getLeftUpperPoint() {
        double x = bound.getX();
        double y = bound.getY();
        return new Point2D.Double(x,y);
    }

    protected Point2D getLeftBottomPoint() {
        double x = bound.getX();
        double y = bound.getY() + getHeight();
        return new Point2D.Double(x,y);
    }

    protected Point2D getRightUpperPoint() {
        double x = bound.getX() + getWidth();
        double y = bound.getY();
        return new Point2D.Double(x,y);
    }

    protected Point2D getRightBottomPoint() {
        double x = bound.getX() + getWidth();
        double y = bound.getY() + getHeight();
        return new Point2D.Double(x,y);
    }
}
