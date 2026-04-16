package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.geo.hull.PathIterator;

public interface Shape extends Transformable {

    double getArea();

    boolean containsPoint(Vector2D p);

    boolean containsPoint(double x,double y);

    AABB getAABB();

    PathIterator getPathIterator(Matrix3x3f transform);
}
