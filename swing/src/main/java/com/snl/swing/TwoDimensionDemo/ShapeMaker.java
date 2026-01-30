package com.snl.swing.TwoDimensionDemo;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class ShapeMaker {

    /**
     * 绘制图形的点数数量
     */
    protected int pointCount;

    public ShapeMaker(int pointCount) {
        this.pointCount = pointCount;
    }

    abstract Shape setShape(Point2D[] p);

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }
}
