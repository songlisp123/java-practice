package com.snl.swing.game.math;

public final class Epsilon {

    public static final double E = compute();

    private static double compute() {
        double e;
        for (e = 0.5F; 1.0F + e > 1.0F;e *= 0.5F) {}
        System.out.println("e = " + e);
        return e;
    }
}
