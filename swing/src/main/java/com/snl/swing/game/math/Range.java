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

    /**
     * 获取相交部分
     * @param b 范围b
     * @return 范围b
     */
    public Range hull(Range b) {
        Range r = new Range();
        r.min = Math.min(this.min,b.min);
        r.max = Math.max(this.max,b.max);
        return r;
    }

    public boolean includesInclusive(double value) {
        return value <= this.max && value >= this.min;
    }

    public boolean includesExclusive(double value) {
        return value < this.max && value > this.min;
    }

    public boolean includesInclusiveMin(double value) {
        return value < this.max && value >= this.min;
    }

    public boolean includesInclusiveMax(double value) {
        return value <= this.max && value > this.min;
    }

    public double getOverlap(Range interval) {
        return this.overlapping(interval) ?
                Math.min(this.max, interval.max) - Math.max(this.min, interval.min) : (double)0.0F;
    }

    public double clamp(double value) {
        return clamp(value, this.min, this.max);
    }

    private double clamp(double value, double min, double max) {
        if (value >= max)
            return max;
        if (value <= min)
            return min;
        return value;
    }

    public boolean isDegenerate() {
        return this.min == this.max;
    }

    public boolean isDegenerate(double diff) {
        return Math.abs(this.max - this.min) <= diff;
    }

    public boolean containsExclusive(Range range) {
        return range.min > min && range.max <= max;
    }

    public boolean containsInclusiveMin(Range range) {
        return range.min >= this.min && range.max < this.max;
    }

    public void union(Range range) {
        min = Math.min(range.min,min);
        max = Math.min(range.max,max);
    }

    public Range getUnion(Range range) {
        return new Range(Math.min(range.min, this.min), Math.max(range.max, this.max));
    }

    public void intersection(Range range) {
        if (this.overlapping(range)) {
            min = Math.max(min, range.min);
            max = Math.min(max, range.max);
        }else {
            min = 0.0;
            max = 0.0;
        }
    }

    public Range getIntersection(Range range) {
        return this.overlapping(range) ?
                new Range(Math.max(range.min, this.min), Math.min(range.max, this.max)) : new Range(0.0F,0.0F);

    }

    public double distance(Range range) {
        if (!this.overlapping(range)) {
            return this.max < range.min ? range.min -this.max : this.min- range.max;
        }else
            return 0;
    }

    public void expand(double value) {
        double e = value * (double)0.5F;
        this.min -= e;
        this.max += e;
        if (value < (double)0.0F && this.min > this.max) {
            double p = (this.min + this.max) * (double)0.5F;
            this.min = p;
            this.max = p;
        }

    }

    public Range getExpanded(double value) {
        double e = value * (double)0.5F;
        double min = this.min - e;
        double max = this.max + e;
        if (value < (double)0.0F && min > max) {
            double p = (min + max) * (double)0.5F;
            min = p;
            max = p;
        }

        return new Range(min, max);
    }

    public double getLength() {
        return this.max - this.min;
    }

    @Override
    public String toString() {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
