package com.apress.bgn.char006.genic;

import java.util.Objects;

public class Rectange implements Comparable<Rectange> {
    private double width;
    private double height;

    public Rectange() {}

    public Rectange(double width,double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Rectange{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rectange rectange = (Rectange) o;
        return Double.compare(width, rectange.width) == 0 && Double.compare(height, rectange.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    public int compareTo(Rectange other) {
        return (int) (this.width - other.width);
    }
}
