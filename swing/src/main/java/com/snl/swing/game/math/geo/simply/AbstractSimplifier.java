package com.snl.swing.game.math.geo.simply;

import com.snl.swing.game.math.Vector2D;

import java.util.List;

public class AbstractSimplifier implements Simplifier {
    public AbstractSimplifier() {
    }

    @Override
    public List<Vector2D> simplify(List<Vector2D> vector2DS) {
        return List.of();
    }

    @Override
    public Vector2D[] simplify(Vector2D... vector2DS) {
        return new Vector2D[0];
    }
}
