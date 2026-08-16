package com.snl.swing.animator.interpolator;


//sin函数插值器
public final class SineInterpolator implements Interpolator {
    @Override
    public double interpolate(double fraction) {
        return Math.sin(fraction * Math.PI);
    }
}
