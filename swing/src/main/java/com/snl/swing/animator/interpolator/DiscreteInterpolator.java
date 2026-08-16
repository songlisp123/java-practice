package com.snl.swing.animator.interpolator;

public final class DiscreteInterpolator implements Interpolator {

    private static DiscreteInterpolator instance = null;
    private static double error = 0.05;

    public static DiscreteInterpolator getInstance() {
        if (instance == null)
            instance = new DiscreteInterpolator();
        return instance;
    }

    public DiscreteInterpolator() {
    }

    @Override
    public double interpolate(double fraction) {
        double abs = Math.abs(1 - fraction);
        if (abs < error)
            return 1;
        return 0;
    }
}
