package com.snl.swing.game.math;

import java.awt.geom.Point2D;

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

    public Vector2D(Point2D o) {
        this.x = o.getX();
        this.y = o.getY();
        this.w = 1.0;
    }

    public void translation(double dx,double dy) {
        this.x += dx;
        this.y += dy;
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

    public Vector2D scale(double s)
    {
        return new Vector2D(this.x * s,this.y * s);
    }

    public Vector2D scale(double sx,double sy) {
        return new Vector2D(this.x * sx,this.y*sy);
    }

    public Vector2D inv() {
        return new Vector2D(-x,-y);
    }

    public Vector2D add(Vector2D v)
    {
        return new Vector2D(x + v.x,y + v.y);
    }

    public Vector2D sub(Vector2D v) {
        return new Vector2D(x - v.x,y - v.y);
    }

    public Vector2D mul(double scale)
    {
        return mul(scale,scale);
    }

    public Vector2D mul(double xScale,double yScale) {
        return new Vector2D(x * xScale,y * yScale);
    }

    public Vector2D div(double x,double y) {
        return new Vector2D(this.x / x,this.y / y);
    }

    public Vector2D div(double scale) {
        return div(scale,scale);
    }


    public Vector2D norm() {
        return div(len());
    }

    public double dot(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }

    public double len() {
        return Math.sqrt(x * x + y * y);
    }

    public double lenSqr() {
        return x * x + y * y;
    }

    /**
     * 投影向量
     * @return 投影向量
     */
    public Vector2D prep() {
        return new Vector2D(-y,x);
    }

    public double angle() {
        return Math.atan2(y,x);
    }

    public static Vector2D polar(double angle,double radius) {
        return new Vector2D(
                radius * Math.cos(angle),
                radius * Math.sin(angle)
        );
    }

    @Override
    public String toString() {
        return "[" + "\n" +
                x + "\n" +
                y + "\n" +
                w + "]";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double w) {
        this.w = w;
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x,y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2D other))
            return false;
        if (other == this)
            return true;
        return this.x == other.x &&
                this.y == other.y;
    }
}
