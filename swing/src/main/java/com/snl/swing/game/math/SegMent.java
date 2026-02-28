package com.snl.swing.game.math;

public class SegMent {
    Vector2D p1,p2;

    public SegMent() {
    }

    public Range projection(Vector2D on) {
        Range r1 = new Range();
        Vector2D unit = on.norm();
        r1.min = p1.dot(unit);
        r1.max = p2.dot(unit);
        r1.sort();
        return r1;
    }

    @Override
    public String toString() {
        return "SegMent{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }
}
