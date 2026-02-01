package com.snl.test.vwctor;

public class Vector2D {
    double x,y,w;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
        this.w = 1.0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.w = 1.0;
    }

    public Vector2D(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
    }

    public Vector2D(double x, double y, double w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    public void translation(double dx,double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void scale(double sx,double sy){
        this.x *= sx;
        this.y *= sy;
    }

    public void shear(double sx,double sy) {
        double temp = x + sx * y;
        y += sy * x;
        x = temp;
    }

    public void rotate(double rad) {
        double temp = x * Math.cos(rad) - y * Math.sin(rad);
        y = x * Math.sin(rad) + y * Math.cos(rad);
        x = temp;
    }

    @Override
    public String toString() {
        return "[" + "\n" +
                x + "\n" +
                y + "\n" +
                w + "]";
    }
}
