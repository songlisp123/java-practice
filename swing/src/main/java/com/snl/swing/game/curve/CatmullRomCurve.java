package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;

public class CatmullRomCurve extends CurveImplement {


    public boolean initialize(final Vector2D[] positions,final float[] times,int count) {
        return false;
    }



    @Override
    public float SegmentArcLength(int segment, float u1, float u2) {
        return 0;
    }

    @Override
    public Vector2D derivative(float t) {
        return null;
    }

    @Override
    public Vector2D second_derivative(float t) {
        return null;
    }
}
