package com.snl.swing.spline;

public class QuEquation extends AbstractEquation {

    // a * x * x + b * x + c = y
    private double a,b,c;

    public QuEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double compute(double xPos) {
        return a * xPos * xPos + b * xPos + c;
    }
}
