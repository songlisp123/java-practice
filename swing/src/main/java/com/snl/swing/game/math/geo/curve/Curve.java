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

    Vector2D getControlPoint01();
    Vector2D getControlPoint02();

    Vector2D getEndPoint();

    double getStartPointX();
    double getStartPointY();
    double getControlPoint01X();
    double getControlPoint01Y();

    double getControlPoint02X();
    double getControlPoint02Y();
    double getEndPointX();
    double getEndPointY();


    void setStartPointX(double x);
    void setStartPointY(double y);

    void setControlPoint01X(double x);
    void setControlPoint01Y(double y);

    void setControlPoint02X(double x);
    void setControlPoint02Y(double y);

    void setEndPointX(double x);
    void setEndpointY(double y);

    Vector2D getPointNearCurve(Vector2D point);
}
