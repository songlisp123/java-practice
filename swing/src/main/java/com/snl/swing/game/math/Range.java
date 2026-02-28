package com.snl.swing.game.math;

public class Range {
    double min;
    double max;

    public Range() {
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }

    public Range(double min,double max)
    {
        this.min = min;
        this.max = max;
    }

    public void sort() {
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
    }

    public boolean overlapping(Range range) {
        return range.min <= max && min <= range.max;
    }

    public Range hull(Range b) {
        Range r = new Range();
        r.min = Math.min(this.min,b.min);
        r.max = Math.max(this.max,b.max);
        return r;
    }


    @Override
    public String toString() {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
