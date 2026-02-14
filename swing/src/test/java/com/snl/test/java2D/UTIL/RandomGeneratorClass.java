package com.snl.test.java2D.UTIL;

import java.util.random.RandomGenerator;

public class RandomGeneratorClass {

    final static RandomGenerator g = RandomGenerator.getDefault();

    public static double random(double bounds)
    {
        return g.nextDouble(-bounds / 2,bounds / 2);
    }

    public static double random(double min,double bounds)
    {
        return g.nextDouble(min,bounds);
    }

    public static int random(int bounds) {
        return g.nextInt(bounds);
    }
}
