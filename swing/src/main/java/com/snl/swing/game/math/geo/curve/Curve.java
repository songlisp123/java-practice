package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Shape;

import java.io.Serializable;

public interface Curve extends Shape,Cloneable, Serializable {

    @Override
    default void translate(Vector2D translated) {
        this.translate(translated.x,translated.y);
    }

    @Override
    default void scale(double scale) {
        this.scale(scale,scale);
    }

    @Override
    default void rotate(double rot, Vector2D rotateCenter) {
        this.rotate(rot,rotateCenter.x,rotateCenter.y);
    }

    @Override
    default <T extends Polygon> T rotateWithTheta(double rotateTheta) {
        return null;
    }

    @Override
    default boolean containsPoint(Vector2D p) {
        return this.containsPoint(p.x,p.y);
    }

    Vector2D getStartPoint();

    Vector2D getEndPoint();

    double getStartPointX();
    double getStartPointY();
    double getEndPointX();
    double getEndPointY();
}
