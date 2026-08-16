package com.snl.swing.animator.evaluator;

import com.snl.swing.game.math.Vector2D;

public class EvaluateVector2D extends Evaluator<Vector2D> {

    private Vector2D v;

    @Override
    public Vector2D evaluate(Vector2D t1, Vector2D t2, double fraction) {
        if (v == null)
            v = t1.clone();
        v.x = getEvaluatedValue(t1.x,t2.x,fraction);
        v.y = getEvaluatedValue(t1.y,t2.y,fraction);
        return v;
    }

    private static double getEvaluatedValue(double start, double end, double fraction) {
        return start + (end - start) * fraction;
    }
}
