package com.snl.swing.animator.evaluator;

public class EvaluateDouble extends Evaluator<Double> {

    public EvaluateDouble() {
    }

    @Override
    public Double evaluate(Double t1, Double t2, double fraction) {
        return t1 + (t2 - t1) * fraction;
    }
}
