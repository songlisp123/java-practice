package com.snl.swing.animator.evaluator;

import java.awt.*;

public class EvaluateColor extends Evaluator<Color> {
    @Override
    public Color evaluate(Color t1, Color t2, double fraction) {
        int red = t1.getRed();
        int blue = t1.getBlue();
        int green = t1.getGreen();

        return new Color(
                (int) (red + (t2.getRed() - red) * fraction),
                (int) (blue + (t2.getBlue() - blue) * fraction),
                (int) (green + (t2.getGreen() - green) * fraction)
        );
    }
}
