package com.snl.swing.game.math.decompose;

import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Convex;

import java.util.List;

public interface Decomposer {

    List<Convex> decompose(Vector2D...vectors);

    List<Convex> decompose(List<Vector2D> vector2DS);
}
