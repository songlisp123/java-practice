package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.SegMent;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.geo.hull.PathIterator;

import java.util.Arrays;

public class QuadCurve implements Curve {
    //起点坐标
    private double x1,y1;

    //控制点坐标
    private double ctrlx,ctrxy;

    //结束点坐标
    private double x2,y2;

    public QuadCurve(double x1, double y1, double ctrlx, double ctrxy, double x2, double y2) {
        setCurve(x1, y1, ctrlx, ctrxy, x2, y2);
    }

    public void setCurve(double x1, double y1, double ctrlx, double ctrxy, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx = ctrlx;
        this.ctrxy = ctrxy;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setCurve(double[] coords,int offset) {
        if (coords == null)
            throw new IllegalArgumentException("非法参数异常");
        int length = coords.length;
        if (length < 6 + offset)
            throw new ArrayStoreException("数组元素错误");
        setCurve(coords[offset],coords[1 + offset],
                coords[2 + offset],coords[3 + offset],
                coords[4 + offset],coords[5 + offset]);
    }

    public void setCurve(Vector2D start,Vector2D control,Vector2D end) {
        setCurve(start.x,start.y,control.x,control.y,end.x,end.y);
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

    public double getCtrlx() {
        return ctrlx;
    }

    public double getCtrxy() {
        return ctrxy;
    }

    public Vector2D getControlPoint() {
        return new Vector2D(ctrlx,ctrxy);
    }

    @Override
    public double getArea() {
        return 0;
    }

    // ***********************************************************//
    // ***********************************************************//
    // ***********************************************************//
    // 以下的实现有点难度 //

    public double getFlatness() {
        return Math.sqrt(SegMent.ptSegDistSq(x1,y1,x2,y2,ctrlx,ctrxy));
    }

    public double getFlatnessSq() {
        return SegMent.ptSegDistSq(x1,y1,x2,y2,ctrlx,ctrxy);
    }

    @Override
    public AABB getAABB() {
        //TODO 难
        return null;
    }

    // 细分领域
    public void subdivide(QuadCurve left,QuadCurve right) {
        subdivide(this,left,right);
    }

    @Override
    public PathIterator getPathIterator(Matrix3x3f transform) {
        return null;
    }

    //********************************* *********************//
    //************************* 几何变换 *********************//
    //************************* ****** *********************//

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

    //********************************* *********************//
    //************************* 静态方法 *********************//
    //************************* ****** *********************//

    /**
     * 这是计算 二次曲线的根的公式
     * @param eqn 一个参数方程系数的数组
     * @param result 根
     * @return 根的数量, {@code -1}的时候表示常熟，或者永远为0
     * @implNote 请注意，我们的参数系数为{c,b,a}，其中参数方程为：
     * {@code a*x^2 + b * x + c = 0}
     */
    public static int solveQuadratic(double[] eqn,double[] result) {
        //任意
        if (eqn == null)
            throw new IllegalArgumentException("数组不能为null");
        int length = eqn.length;
        if (length < 3)
            throw new ArrayStoreException("数组长度不符合要求");
        if (result == null)
            result = new double[2];
        length = result.length;
        if (length < 2)
            throw new ArrayStoreException("数组长度不符合要求");

        //参数方程系数
        double a = eqn[2];
        double b = eqn[1];
        double c = eqn[0];
        int root = 0;
        if (a == 0)
        {
            //二次曲线退化成直线
            if (b == 0)
            {
                //直线退化成常熟
                return -1;
            }
            else
                result[root++] = - c / b;
        }
        else {
            //常规二次方程，求根公式 d = b * b - 4 a * c
            double d = b * b - 4.0 * a * c;
            if (d < 0)
                //无解
                return 0;

            d = Math.sqrt(d);

            if (b < 0.0)
                b = -b;
            double q = (b + d) / -2.0;
            result[root++] = q / a;
            if (q != 0) {
                result[root++] =c /q;
            }
        }

        return root;
    }

    public static void subdivide(double[] src,int srcOffset,double[] left,int leftOffset,
                                 double[] right,int rightOffset) {
        double x1,x2,y1,y2,ctrlx,ctrly;
        double ctrlx1,ctrly1,ctrlx2,ctrly2;

        //获取源
        x1 = src[srcOffset];
        y1 = src[srcOffset + 1];
        ctrlx = src[srcOffset + 2];
        ctrly = src[srcOffset + 3];
        x2 = src[srcOffset + 4];
        y2 = src[srcOffset + 5];
        if (left != null) {
            left[leftOffset] = x1;
            left[leftOffset + 1] = y1;
        }

        if (right != null)
        {
            right[rightOffset + 4] = x2;
            right[rightOffset + 5] = y2;
        }

        ctrlx1 = (x1 + ctrlx) / 2.0;
        ctrly1 = (y1 + ctrly) / 2.0;

        ctrlx2 = (x2 + ctrlx) / 2.0;
        ctrly2 = (y2 + ctrly) / 2.0;

        ctrlx = (ctrlx1 + ctrlx2) / 2.0;
        ctrly = (ctrly1 + ctrly2) / 2.0;
        if (left != null)
        {
            left[leftOffset + 2] = ctrlx1;
            left[leftOffset + 3] = ctrly1;
            left[leftOffset + 4] = ctrlx;
            left[leftOffset + 5] = ctrly;
        }
        if (right != null)
        {
            right[rightOffset] = ctrlx;
            right[rightOffset + 1] = ctrly;
            right[rightOffset + 2] = ctrlx2;
            right[rightOffset + 3] = ctrly2;
        }
    }

    public static void subdivide(QuadCurve src,QuadCurve left,QuadCurve right) {
        double x1,x2,y1,y2,ctrlx,ctrly;
        double ctrlx1,ctrly1,ctrlx2,ctrly2;
        x1 = src.x1;
        x2 = src.x2;
        y1 = src.y1;
        y2 = src.y2;
        ctrlx = src.ctrlx;
        ctrly = src.ctrxy;

        ctrlx1 = (x1 + ctrlx) / 2.0;
        ctrly1 = (y1 + ctrly) / 2.0;

        ctrlx2 = (x2 + ctrlx) / 2.0;
        ctrly2 = (y2 + ctrly) / 2.0;

        ctrlx = (ctrlx1 + ctrlx2) / 2.0;
        ctrly = (ctrly1 + ctrly2) / 2.0;
        if (left != null)
            left.setCurve(x1,y1,ctrlx1,ctrly1,ctrlx,ctrly);
        if (right != null)
            right.setCurve(ctrlx,ctrly,ctrlx2,ctrly2,x2,y2);
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

}
