package com.snl.swing.game.math.geo;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Shape;

import java.awt.geom.GeneralPath;

public class Path implements Shape {

    private GeneralPath gp;

    public Path() {
        gp = new GeneralPath();
    }


    public void moveTo(double x,double y) {
        gp.moveTo(x,y);
    }

    public void lineTo(double x,double y) {
        gp.lineTo(x,y);
    }

    public void quadTo(double ctrlx,double ctrly,double epx,double epy) {
        gp.quadTo(ctrlx, ctrly, epx, epy);
    }

    public void  cuTo(double ctrl1x,double ctrl1y,double ctrl2x,double ctrl2y,double epx,double epy) {
        gp.curveTo(ctrl1x, ctrl1y, ctrl2x, ctrl2y, epx, epy);
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public boolean containsPoint(Vector2D p) {
        return false;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return false;
    }

    @Override
    public AABB getAABB() {
        return null;
    }

    @Override
    public PathIterator getPathIterator(Matrix3x3f transform) {
        return null;
    }

    @Override
    public void rotate(double rotateTheta) {

    }

    @Override
    public <T extends Polygon> T rotateWithTheta(double rotateTheta) {
        return null;
    }

    @Override
    public void rotate(double rot, Vector2D rotateCenter) {

    }

    @Override
    public void rotate(double rot, double x, double y) {

    }

    @Override
    public void scale(double scale) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double sx, double sy) {

    }

    @Override
    public void translate(Vector2D translated) {

    }

    @Override
    public void translate(double x, double y) {

    }

    public GeneralPath getGp(Matrix3x3f viewPort) {
        GeneralPath p = new GeneralPath();
        java.awt.geom.PathIterator pathIterator = gp.getPathIterator(Matrix3x3f.convertIntoAffineTransform(viewPort));
        double[] s = new double[6];
        while (!pathIterator.isDone()) {
            int mode = pathIterator.currentSegment(s);
            switch (mode) {
                case java.awt.geom.PathIterator.SEG_MOVETO ->
                    p.moveTo(s[0],s[1]);
                case java.awt.geom.PathIterator.SEG_LINETO ->
                    p.lineTo(s[0],s[1]);
                case java.awt.geom.PathIterator.SEG_QUADTO ->
                    p.quadTo(s[0],s[1],s[2],s[3]);
                case java.awt.geom.PathIterator.SEG_CUBICTO ->
                    p.curveTo(s[0],s[1],s[2],s[3],s[4],s[5]);
            }
            pathIterator.next();
        }
        return p;
    }

    public void setGp(GeneralPath gp) {
        this.gp = gp;
    }
}
