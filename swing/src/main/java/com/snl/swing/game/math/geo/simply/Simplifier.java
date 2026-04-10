package com.snl.swing.game.math.geo.simply;

import com.snl.swing.game.math.Vector2D;

import java.util.List;

public interface Simplifier {
    List<Vector2D> simplify(List<Vector2D> vector2DS);

    Vector2D[] simplify(Vector2D... vector2DS);
}
