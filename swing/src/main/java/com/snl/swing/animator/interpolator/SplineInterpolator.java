package com.snl.swing.animator.interpolator;


import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * 核心插值器，有关{@code Interpolator}的说明参阅{@link Interpolator}
 * @since 2026年5月6日21:13:19
 * @author snl
 */
public final class SplineInterpolator implements Interpolator {
    //控制点1
    private double x1;
    //控制点1Y轴
    private double y1;
    //控制点2X轴
    private double x2;
    //控制点y轴
    private double y2;
    //弧长
    private ArrayList lengths = new ArrayList();

    public SplineInterpolator(double x1, double y1, double x2, double y2) {
        // 由于我们在0-1区间插值，所以确保坐标落在0+1区间
        if (!(x1 < 0.0F) && !(x1 > 1.0F) && !(y1 < 0.0F)
                && !(y1 > 1.0F)
                && !(x2 < 0.0F) && !(x2 > 1.0F)
                && !(y2 < 0.0F) && !(y2 > 1.0F)) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            double prevX = 0.0F;
            double prevY = 0.0F;
            double prevLength = 0.0F;

            for(double t = 0.01F; t <= 1.0F; t += 0.01F) {
                Point2D.Double xy = this.getXY(t);
                double length = prevLength + (double)Math.sqrt((double)((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY)));
                LengthItem lengthItem = new LengthItem(length, t);
                this.lengths.add(lengthItem);
                prevLength = length;
                prevX = xy.x;
                prevY = xy.y;
            }

            for (Object length : this.lengths) {
                LengthItem lengthItem = (LengthItem) length;
                lengthItem.setFraction(prevLength);
            }

        } else {
            throw new IllegalArgumentException("Control points must be in the range [0, 1]:");
        }
    }

    private Point2D.Double getXY(double t) {
        double invT = 1.0F - t;
        double b1 = 3.0F * t * invT * invT;
        double b2 = 3.0F * t * t * invT;
        double b3 = t * t * t;
        Point2D.Double xy = new Point2D.Double(b1 * this.x1 + b2 * this.x2 + b3, b1 * this.y1 + b2 * this.y2 + b3);
        return xy;
    }

    private double getY(double t) {
        double invT = 1.0F - t;
        double b1 = 3.0F * t * invT * invT;
        double b2 = 3.0F * t * t * invT;
        double b3 = t * t * t;
        return b1 * this.y1 + b2 * this.y2 + b3;
    }

    @Override
    public double interpolate(double lengthFraction) {
        double interpolatedT = 1.0F;
        double prevT = 0.0F;
        double prevLength = 0.0F;

        for (Object length : this.lengths) {
            LengthItem lengthItem = (LengthItem) length;
            double fraction = lengthItem.getFraction();
            double t = lengthItem.getT();
            if (lengthFraction <= fraction) {
                double proportion = (lengthFraction - prevLength) / (fraction - prevLength);
                interpolatedT = prevT + proportion * (t - prevT);
                return this.getY(interpolatedT);
            }

            prevLength = fraction;
            prevT = t;
        }

        return this.getY(interpolatedT);
    }
}
