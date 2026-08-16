package com.snl.swing.animator.interpolator;


//倒放效果
public final class Inverter implements Interpolator {

    private static Inverter instance = null;

    public static Inverter getInstance() {
        if (instance == null)
            instance = new Inverter();
        return instance;
    }

    @Override
    public double interpolate(double fraction) {
        return 1.0 - fraction;
    }
}
