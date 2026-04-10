package com.snl.swing.game.math.decompose;

import com.snl.swing.game.math.Triangle;
import com.snl.swing.game.math.Vector2D;

import java.util.List;

public interface Triangulator {
    List<Triangle> triangulate(Vector2D... var1);
}
