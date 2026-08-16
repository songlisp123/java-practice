package com.snl.swing.spline;

public class LineEquation  extends AbstractEquation {

    //ax + by + c = 0
    private double a,b,c;

    public LineEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double compute(double xPos) {
        return (a * xPos + c) / -b;
    }
}
