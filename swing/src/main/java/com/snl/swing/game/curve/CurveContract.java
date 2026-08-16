package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;
import com.snl.swing.tank.Flushable;

public interface CurveContract extends Flushable {

    Vector2D evaluate(float t);

    float arcLength(float t1,float t2);

    float SegmentArcLength(int segment,float u1,float u2);

    Vector2D derivative(float t);
    Vector2D second_derivative(float t);

}
