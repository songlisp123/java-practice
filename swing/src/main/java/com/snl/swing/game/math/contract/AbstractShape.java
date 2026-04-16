package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;

public abstract class AbstractShape implements Shape {

    @Override
    public boolean containsPoint(Vector2D p) {
        return containsPoint(p.x,p.y);
    }

    @Override
    public void translate(Vector2D translated) {
        this.translate(translated.x,translated.y);
    }

    @Override
    public void scale(double scale) {
        this.scale(scale,scale);
    }

    @Override
    public void rotate(double rot, Vector2D rotateCenter) {
        this.rotate(rot,rotateCenter.x,rotateCenter.y);
    }

    @Override
    public <T extends Polygon> T rotateWithTheta(double rotateTheta) {
        return null;
    }
}
