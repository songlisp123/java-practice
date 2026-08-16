package com.snl.swing.game.utils;

import java.util.random.RandomGenerator;

public class Generator {


    public static final RandomGenerator generator =
            RandomGenerator.getDefault();

    public static double generateDouble(double lowBound,double upBounds) {
        if (lowBound > upBounds)
            throw new UnsupportedOperationException("未收支持的操作");
        return generator.nextDouble(lowBound,upBounds);
    }

    public static float generateFloat(float lowBound,float upBounds) {
        if (lowBound > upBounds)
            throw new UnsupportedOperationException("未收支持的操作");
        return generator.nextFloat(lowBound,upBounds);
    }
}
