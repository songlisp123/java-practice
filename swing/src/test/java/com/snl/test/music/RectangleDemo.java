package com.snl.test.music;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RectangleDemo extends Rectangle2D.Double {

    protected final static double WEIGHT = 1;
    protected double height;
    protected double xCor;
    protected double yCor;

    public RectangleDemo(double xCor,double yCor,double height) {
        super(xCor,yCor, WEIGHT,height);
        this.height = height;
        this.xCor = xCor;
        this.yCor = yCor;
    }

    public void setHeight(double height) {
        if (height > 450) {
            height = height / 50;
        }
        setFrame(getLocation(),new Dimension((int) WEIGHT, (int) height));
    }

    public Point2D getLeftBottomPoint() {
        System.out.println(getX());
        return new Point2D.Double(getX(),500);
    }

    public Point2D getLocation() {
        return new Point2D.Double(xCor,yCor);
    }
}
