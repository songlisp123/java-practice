package com.snl.swing.animator.interpolator;

public final class LinearInterpolator implements Interpolator {

    public LinearInterpolator() {
    }

    private static LinearInterpolator INSTACE = null;

    public static LinearInterpolator getInstance() {
        if (INSTACE == null)
            INSTACE = new LinearInterpolator();
        return INSTACE;
    }

    @Override
    public double interpolate(double fraction) {
        return  fraction;
    }
}
