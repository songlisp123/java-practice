package com.snl.swing.game.utils;

import com.snl.swing.game.math.*;
import com.snl.swing.game.math.geo.curve.CubicCurve;
import com.snl.swing.game.math.geo.curve.Curve;
import com.snl.swing.game.math.geo.curve.QuadCurve;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 一个帮助方法
 */
public class Geometry {

    private static final double FRACTOR = 1 / 3.0;


    /**
     *
     * @param t
     * @return
     * @param <T>
     */
    public static <T extends Polygon> Vector2D getAverageCenter(T t) {
        Vector2D temp = new Vector2D();
        Vector2D center = new Vector2D();
        Iterator<Vector2D> iterator = t.getVertexIterator();
        while (iterator.hasNext()) {
            Vector2D v2 = iterator.next();
            temp = temp.add(v2);
        }
        int size = t.getSize();
        center.x = temp.x / size;
        center.y = temp.y / size;
        return center;
    }
    
    public static Vector2D getAverageCenter(Vector2D...vector2DS) {
        if (vector2DS == null) 
            throw new IllegalArgumentException("参数不能为null");
        int size = vector2DS.length;
        if (size == 0) {
            throw new IllegalArgumentException("数组长度不能为0");
        } else if (size == 1) {
            Vector2D center = vector2DS[0];
            if (center == null)
                throw new NullPointerException("数组元素不能为null");
            return center.clone();
        }else {
            Vector2D ac = new Vector2D(); //初始化
            for (Vector2D v : vector2DS) {
                //遍历所有点
                if (v == null)
                    throw new NullPointerException("数组元素不能为null");
                /*
                我们之所以描述这个，是因为一个公式，一个多边形的顺时针顶点的和
                除以点的个数等于该多边形的重心
                 */
                ac = ac.add(v);
            }

            ac.x /= size;
            ac.y /= size;
            return ac;
        }
    }

    public static Vector2D getAverageCenter(List<Vector2D> vector2DS) {
        return getAverageCenter(vector2DS.toArray(Vector2D[]::new));
    }

    /**
     * 获取面积权重中心
     * @param vector2DS 点坐标
     * @return 权重中心
     */
    public static Vector2D getAreaWeightedCenter(Vector2D...vector2DS) {
        //获取重心
        Vector2D ac = getAverageCenter(vector2DS);
        int size = vector2DS.length;
        Vector2D awc = new Vector2D();
        double area = 0;

        //计算面积的权重
        for (int i = 0;i<size;i++) {
            Vector2D p1 = vector2DS[i];
            Vector2D p2 = i + 1 < size ? vector2DS[i + 1] : vector2DS[0];
            /*
            以下步骤：计算子三角的面积
             */
            p1 = p1.sub(ac);
            p2 = p2.sub(ac);
            //叉积 计算 有向三角形面积 ，这是个带符号的有向三角形面积的两倍
            double crossed = p1.cross2D(p2);
            //获取面积
            double triangleArea = crossed / 2.0;
            area += triangleArea;
            //判断新的中心
            awc.add(
                    p1.add(p2).mul(FRACTOR).mul(triangleArea)
            );
        }

        if (Math.abs(area) <= Epsilon.E) {
            //如果 面积 小于精度
            return vector2DS[0].clone();
        }else {
            /*
            c = c / 总面积 + 重心
             */
            awc = awc.div(area).add(ac);
            return awc;
        }
    }

    public static <T extends Polygon>  Vector2D getAreaWeightedCenter(T t) {
        return getAreaWeightedCenter(t.getVertices());
    }

    public static Vector2D getAreaWeightedCenter(Collection<Vector2D> collection) {
        return getAreaWeightedCenter(collection.toArray(Vector2D[]::new));
    }

    public static  boolean CheckSign(double a,double b) {
        return a * b > 0;
    }

    public static Vector2D[] getCounterClockwiseEdgeNormals(Vector2D...v2ds) {
        if (v2ds == null)
            throw new IllegalArgumentException("参数不能为null");
        int size = v2ds.length;
        if (size <= 1)
            throw new IllegalArgumentException("点数必须大于等于2");
        Vector2D[] normals = new Vector2D[size];

        for (int i = 0;i<size;i++) {
            Vector2D p1 = v2ds[i];
            Vector2D p2 = i + 1 == size ? v2ds[0] : v2ds[i + 1];
            //截取向量
            Vector2D norm = p2.sub(p1).prep().norm();
            normals[i] = norm;
        }

        return normals;
    }

    /**
     * 获取环绕规则，环绕规则是一个复杂的问题
     * @param vector2DS
     * @return
     * @implNote github开源代码
     */
    public static final double getWinding(Vector2D...vector2DS) {
        if (vector2DS == null)
            throw new IllegalArgumentException("参数为null");
        else {
            double area = 0;
            int length = vector2DS.length;
            if (length < 2)
                throw new IllegalArgumentException("参数数组至少大于等于2");
            for (int i = 0;i<length;++i){
                //TODO
                int  j = i + 1 == length ? 0 : i + 1;
                Vector2D p1 = vector2DS[i];
                Vector2D p2 = vector2DS[j];
                if (p1 == null)
                    throw new NullPointerException("数组元素为空");
                if (p2 == null)
                    throw new NullPointerException("数组元素为空");

                area += p1.cross2D(p2);
            }
            return area;
        }
    }


    /**
     * 判断给定点与线段的位置
     * @param ap1 点1
     * @param ap2 点2
     * @param segMent 线段
     * @return {@code 0}，表示其中一点位于线段上，{@code < 0} 表示相交
     * {@code > 0} 表示p1\p2位于同一侧
     * @implNote 图像宝石第一卷
     */
    public static final int PointWhere(Vector2D ap1, Vector2D ap2, SegMent segMent) {
        //计算各向量
        double dx,dy,dx1,dy1,dx2,dy2,p_1,p_2;
        //计算dx，这是线段的水平距离
        dx = segMent.p2.x - segMent.p1.x;
        dy = segMent.p2.y - segMent.p1.x;
        //计算
        dx1 = ap1.x - segMent.p1.x;
        dy1 = ap1.y - segMent.p1.y;

        //计算dx2，dy2
        dx2 = ap2.x - segMent.p2.x;
        dy2 = ap2.y - segMent.p2.y;

        //计算p_2,p_2
        p_1 = dx * dy1 - dx1 * dy;
        p_2 = dx * dy2 - dx2 * dy;

        if (p_1 == 0 || p_2 == 0)
            return 0;
        else
            if ((p_1 > 0 && p_2 < 0) || (p_1 < 0 && p_2 >0))
                return -1;
            return 1;
    }

    public static final Polygon transformPolygon(Polygon src,Polygon des,Matrix3x3f transform) {
        if (src == null)
            throw new IllegalArgumentException("");
        if (des == null) {
            des = new Polygon(src);
        }

        Vector2D[] vs = des.getVertices();
        for (int i = 0;i<vs.length;i++) {
            vs[i] = transform.mul(vs[i]);
        }

        des.setVertices(vs);
        return des;
    }


    public static final Curve transFormCurve(Curve src, Curve des, Matrix3x3f transform) {
        if (src instanceof QuadCurve quadCurve) {
            if (des == null)
                des = new QuadCurve(src);
            Vector2D startPoint = src.getStartPoint();
            startPoint = transform.mul(startPoint);

            Vector2D cp = src.getControlPoint01();
            cp =  transform.mul(cp);

            Vector2D endPoint = src.getEndPoint();
            endPoint = transform.mul(endPoint);

            des.setStartPointX(startPoint.x);
            des.setStartPointY(startPoint.y);

            des.setControlPoint01X(cp.x);
            des.setControlPoint01Y(cp.y);

            des.setEndPointX(endPoint.x);
            des.setEndpointY(endPoint.y);

            return des;
        } else if (src instanceof CubicCurve c) {
            if (des == null)
                des = new CubicCurve(src);
            Vector2D startPoint = src.getStartPoint();
            startPoint = transform.mul(startPoint);

            Vector2D cp1 = src.getControlPoint01();
            cp1 = transform.mul(cp1);

            Vector2D cp2 = src.getControlPoint02();
            cp2 = transform.mul(cp2);

            Vector2D endPoint = src.getEndPoint();
            endPoint = transform.mul(endPoint);

            des.setStartPointX(startPoint.x);
            des.setStartPointY(startPoint.y);

            des.setControlPoint01X(cp1.x);
            des.setControlPoint01Y(cp1.y);

            des.setControlPoint02X(cp2.x);
            des.setControlPoint02Y(cp2.y);

            des.setEndPointX(endPoint.x);
            des.setEndpointY(endPoint.y);

            return des;
        }
        throw new RuntimeException();
    }
}
