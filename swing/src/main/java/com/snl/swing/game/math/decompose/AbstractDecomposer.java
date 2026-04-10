package com.snl.swing.game.math.decompose;

import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Convex;

import java.util.List;

public abstract class AbstractDecomposer implements Decomposer {

    public AbstractDecomposer() {
    }

    @Override
    public List<Convex> decompose(List<Vector2D> vector2DS) {
        return this.decompose(vector2DS.toArray(Vector2D[]::new));
    }
}
