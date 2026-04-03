package com.snl.swing.game.math;

public final class Epsilon {

    public static final double E = compute();

    public static final double PRECISION = 1.0 / 1_000_000;

    private static double compute() {
        double e;
        for (e = 0.5F; 1.0F + e > 1.0F;e *= 0.8F) {}
        System.out.println("e = " + e);
        return e;
    }
}
