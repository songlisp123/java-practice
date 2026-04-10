package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.Vector2D;

import java.util.Iterator;

public interface Wound {

    Iterator<Vector2D> getVertexIterator();

    Vector2D[] getVertices();

    Vector2D[] getNormals();

    Iterator<Vector2D> getNormalIterator();
}
