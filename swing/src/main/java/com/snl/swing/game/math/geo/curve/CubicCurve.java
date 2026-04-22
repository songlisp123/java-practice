package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.geo.PathIterator;

import java.io.Serial;

public class CubicCurve implements Curve {

    private double x1,y1;
    private double ctrlx1,ctrly1;
    private double ctrlx2,ctrly2;
    private double x2,y2;

    @Serial
    private static final long serialVersionUID = -4202960122839707295L;

    public CubicCurve() {
    }

    public CubicCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2,
                      double ctrly2, double x2, double y2) {
        setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }

    public CubicCurve(CubicCurve curve) {
        setCurve(curve.x1,curve.y1,curve.ctrlx1,
                curve.ctrly1,curve.ctrlx2,curve.ctrly2,curve.x2,curve.y2);
    }

    public CubicCurve(Curve src) {
        setCurve(
                src.getStartPointX(),src.getStartPointY(),
                src.getControlPoint01X(),src.getControlPoint01Y(),
                src.getControlPoint02X(),src.getControlPoint02Y(),
                src.getEndPointX(),src.getEndPointY()
        );
    }

    public void setCurve(double x1, double y1, double ctrlx1, double ctrly1,
                         double ctrlx2, double ctrly2, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx1 = ctrlx1;
        this.ctrly1 = ctrly1;
        this.ctrlx2 = ctrlx2;
        this.ctrly2 = ctrly2;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public Vector2D getStartPoint() {
        return new Vector2D(x1,y1);
    }

    @Override
    public Vector2D getEndPoint() {
        return new Vector2D(x2,y2);
    }

    @Override
    public double getStartPointX() {
        return x1;
    }

    @Override
    public double getStartPointY() {
        return y1;
    }

    @Override
    public double getEndPointX() {
        return x2;
    }

    @Override
    public double getEndPointY() {
        return y2;
    }

    @Override
    public Vector2D getPointNearCurve(Vector2D point) {
        return null;
    }

    public Vector2D getControlPoint01() {
        return new Vector2D(ctrlx1,ctrly1);
    }

    @Override
    public double getControlPoint01X() {
        return ctrlx1;
    }

    @Override
    public double getControlPoint01Y() {
        return ctrly1;
    }

    @Override
    public double getControlPoint02X() {
        return ctrlx2;
    }

    @Override
    public double getControlPoint02Y() {
        return ctrly2;
    }

    @Override
    public void setStartPointX(double x) {
        this.x1 = x;
    }

    @Override
    public void setStartPointY(double y) {
        this.y1 = y;
    }

    @Override
    public void setControlPoint01X(double x) {
        this.ctrlx1 = x;
    }

    @Override
    public void setControlPoint01Y(double y) {
        this.ctrly1 = y;
    }

    @Override
    public void setControlPoint02X(double x) {
        this.ctrlx2 = x;
    }

    @Override
    public void setControlPoint02Y(double y) {
        this.ctrly2 = y;
    }

    @Override
    public void setEndPointX(double x) {
        this.x2 = x;
    }

    @Override
    public void setEndpointY(double y) {
        this.y2 = y;
    }

    @Override
    public Vector2D getControlPoint02() {
        return new Vector2D(ctrlx2,ctrly2);
    }


    public Vector2D getControlPoint2() {
        return new Vector2D(ctrlx2,ctrly2);
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public AABB getAABB() {
        //也很难
        return null;
    }

    @Override
    public PathIterator getPathIterator(Matrix3x3f transform) {
        return new CubicIterator(this,transform);
    }

    @Override
    public void rotate(double rotateTheta) {

    }

    @Override
    public void rotate(double rot, double x, double y) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double sx, double sy) {

    }

    @Override
    public void translate(double x, double y) {

    }

    // ***********************************************************//
    // *****************************曲线平整度**************************//
    // ***********************************************************//

    //也很难
    public double getFatness() {
        return Math.sqrt(0.0);
    }

    public double getFatnessSqr() {
        return 0.0;
    }


    //********************************* *********************//
    //************************* 碰撞测试 *********************//
    //************************* ****** *********************//
    //也很难

    public boolean intersects(Vector2D min,Vector2D max) {
        return false;
    }

    public boolean intersects(AABB aabb) {
        return false;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        //TODO 难
        return false;
    }

    //********************************* *********************//
    //************************* 细分测试 *********************//
    //************************* ****** *********************//

    public void subdivide(CubicCurve left,CubicCurve right) {subdivide(this,left,right);}

    public static void subdivide(CubicCurve src,CubicCurve left,CubicCurve right) {}

    public void setControlPoint01(Vector2D d) {
        ctrlx1 += d.x;
        ctrly1 += d.y;
    }

    public void setControlPoint02(Vector2D d) {
        ctrlx2 += d.x;
        ctrly2 += d.y;
    }
}
