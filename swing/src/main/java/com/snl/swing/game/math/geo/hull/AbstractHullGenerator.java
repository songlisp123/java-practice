package com.snl.swing.game.math.geo.hull;

import com.snl.swing.game.math.Vector2D;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractHullGenerator implements HullGenerator {
    public AbstractHullGenerator() {
    }

    @Override
    public List<Vector2D> generate(List<Vector2D> vector2DS) {
        return Arrays.asList(
                vector2DS.toArray(Vector2D[]::new)
        );
    }
}
