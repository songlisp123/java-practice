package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;

/**
 * 使用 akins 算法模拟曲线
 */
public class Polynomials implements CurveContract {

    private float[] times;
    private Vector2D[] positions;

    @Override
    public Vector2D evaluate(float t) {
        return null;
    }

    @Override
    public float arcLength(float t1, float t2) {
        return 0;
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

    @Override
    public void flush() {

    }


    public static Vector2D aitken(int degree,Vector2D[] positions,float t) {
        int r,i;
        float t1;
        Vector2D[] temp = new Vector2D[20];
        System.arraycopy(positions,0,temp,0,degree + 1);

        for (r = 1; r<=degree;r++) {
            for (i = 0;i <= degree - r;i ++) {
                t1 = (float) (degree * t - i) / r;
                temp[i] = temp[i].scale(1 - t1)
                        .add(temp[i + 1].scale(t1));
            }
        }

        return temp[0];
    }
}
