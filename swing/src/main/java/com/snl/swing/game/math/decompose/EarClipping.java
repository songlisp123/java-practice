package com.snl.swing.game.math.decompose;

import com.snl.swing.game.math.Triangle;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Convex;

import java.util.List;

public class EarClipping extends AbstractDecomposer implements Decomposer , Triangulator {

    public EarClipping() {
    }

    @Override
    public List<Convex> decompose(Vector2D... vectors) {
        return List.of();
    }

    @Override
    public List<Triangle> triangulate(Vector2D... var1) {
        return List.of();
    }
}
