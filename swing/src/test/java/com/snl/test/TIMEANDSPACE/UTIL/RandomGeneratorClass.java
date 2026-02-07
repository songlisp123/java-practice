package com.snl.test.TIMEANDSPACE.UTIL;

import java.util.random.RandomGenerator;

public class RandomGeneratorClass {

    final static RandomGenerator g = RandomGenerator.getDefault();

    public static double random(double bounds)
    {
        return g.nextDouble(-bounds / 2,bounds / 2);
    }

    public static int random(int bounds) {
        return g.nextInt(bounds);
    }
}
