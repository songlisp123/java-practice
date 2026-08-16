package com.snl.swing.animator.interpolator;

//弧长类
public class LengthItem {
    double length;
    double t;
    double fraction;

    LengthItem(double length, double t, double fraction) {
        this.length = length;
        this.t = t;
        this.fraction = fraction;
    }

    LengthItem(double length, double t) {
        this.length = length;
        this.t = t;
    }

    public double getLength() {
        return this.length;
    }

    public double getT() {
        return this.t;
    }

    public double getFraction() {
        return this.fraction;
    }

    void setFraction(double totalLength) {
        this.fraction = this.length / totalLength;
    }
}
