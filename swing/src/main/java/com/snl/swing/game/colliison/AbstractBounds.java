package com.snl.swing.game.colliison;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Translatable;

public abstract class AbstractBounds implements Bound , Translatable {

    protected Matrix3x3f transform;

    public AbstractBounds() {
        transform = Matrix3x3f.identity();
    }

    public AbstractBounds(double x,double y) {
        this();
        this.translate(x,y);
    }

    public AbstractBounds(Vector2D vector2D) {
        this();
        this.translate(vector2D);
    }

    @Override
    public void translate(Vector2D translated) {
        transform = transform.mul(Matrix3x3f.translate(translated));
    }

    @Override
    public void translate(double x, double y) {
        transform = transform.mul(Matrix3x3f.translate(x,y));
    }

    @Override
    public void shift(Vector2D shift) {
        transform = transform.mul(Matrix3x3f.translate(shift));
    }
}
