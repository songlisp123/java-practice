package com.snl.swing.game.math.geo.hull;

import com.snl.swing.game.math.Vector2D;

import java.util.List;

public interface HullGenerator {
    Vector2D[] generate(Vector2D...vector2DS);

    List<Vector2D> generate(List<Vector2D> vector2DS);
}
