package com.snl.swing.game.math;

import com.snl.swing.game.utils.Geometry;

import java.util.Arrays;
import java.util.Iterator;

public class Convexity extends Polygon {

    public Convexity() {}

    public Convexity(Vector2D offset, Vector2D... vertices) {
        validate(vertices);
        if (offset == null) this.offset = new Vector2D();
        else this.offset = offset;
        this.vertices = vertices;
        size = this.vertices.length;
        center = Geometry.getAverageCenter(vertices);
    }

    /**
     * 判断 端点 的有效性
     * @param vectors 端点
     * @since 2026年4月2日21:07:38
     */
    private void validate(Vector2D...vectors) {
        if (vectors == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        if (vectors.length == 0) {
            //如果点坐标的长度 等于0
            throw new IllegalArgumentException("点数不能非零");
        }
        int length = vectors.length;
        if (length < 3) {
            throw new IllegalArgumentException("点数必须大于等于三");
        }
        //否则 ，验证是否有null的点
        for (Vector2D vector : vectors) {
            if (vector == null)
                throw new IllegalArgumentException("参数不能为null");
        }
        //验证是否是凸变形
        double sign = 0,area = 0;
        for (int i = 0;i<length ; i++) {
            Vector2D p0 = i - 1 < 0 ? vectors[length - 1] : vectors[i - 1];
            Vector2D p1 = vectors[i];
            Vector2D p2 = (i + 1 == length) ? vectors[0] : vectors[i + 1];
            if (p1.equals(p2)) {
                //如果，p1和p2相同 ，则表明，共点，返回
                throw new ArithmeticException("不能共点");
            }

            //叉积
            double cross = p0.sub(p1).cross2D(p1.sub(p2));
            //判断 叉积符号，好吧，如果叉积返回小于0的数字，则表明
            double tsign = Math.signum(cross);

            area += cross;
            if (Math.abs(cross) > Epsilon.E && sign != 0.0F  && tsign != sign) {
                throw new IllegalArgumentException("必须是凸多边形");
            }
            sign = tsign;
        }

        if (Math.abs(area) <= Epsilon.E) {
            throw new RuntimeException("凸多边形有面积接近为0的区域");
        }else {
            this.area = area;
        }
    }

    /**
     * 是否与另一个凸变型碰撞
     * @param convexity 测试的凸变型
     * @return 如果包含，返回{@code true},否则返回{@code false}
     * @implNote 该程序由分离轴定理实现
     */
    public boolean collideOtherConvexity(Convexity convexity) {
        Vector2D[] axis = this.getAxis();
        Range range,range1;
        for (Vector2D a : axis)
        {
             range = this.projectionOntoVector(a);
             range1 = convexity.projectionOntoVector(a);
            if (!range.overlapping(range1))
                return false;
        }
        axis = convexity.getAxis();
        for (Vector2D a : axis)
        {
             range = this.projectionOntoVector(a);
             range1 = convexity.projectionOntoVector(a);
            if (!range.overlapping(range1))
                return false;
        }
        return true;
    }

    /**
     * 获取AABB矩形
     * @return 包裹aabb矩形
     */
    public AABB getAABB() {
        AABB aabb = new AABB(new Vector2D(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY),
                new Vector2D(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY));
        Vector2D[] v = this.getVertices();
        for (Vector2D corner : v) {
            aabb.min.x = Math.min(corner.x,aabb.min.x);
            aabb.min.y = Math.min(corner.y,aabb.min.y);
            aabb.max.x = Math.max(corner.x,aabb.max.x);
            aabb.max.y = Math.max(corner.y,aabb.max.y);
        }
        return aabb;
    }

    //TODO 待办如何找到多边形的包围圆？？ 【未完成 ❌】
    /*
    对于三角形来说：我们需要找到
     */
    public Circle getCircle() {
        AABB aabb = getAABB();
        Vector2D ct = aabb.max.add(aabb.min).div(2);
        Vector2D hf = aabb.max.sub(aabb.min).div(2);
        OrientedRectangle o = new OrientedRectangle(ct,hf,0);
        double r = o.halfExtend.len();
        return new Circle(r,o.center);
    }


    /**
     * 获取周长
     * @return 多边形周长
     */
    public double getPerimeter() {
        double r = 0;
        Vector2D p = vertices[size - 1];
        for (Vector2D vertex : vertices) {
            Vector2D d = p.sub(vertex);
            r += d.len();
            p = vertex;
        }
        return r;
    }

    /**
     * 获取面积
     * @return 改凸变形的面积
     */
    /*
    我们使用一个常用的公式：
    ||
     */
    public double getArea() {
        Vector2D v0 = vertices[0];
        double area = 0;
        for (int i = 0;i<size-2;i++) {
            Vector2D v1 = vertices[i + 1].sub(v0);
            v1.w = 0;
            Vector2D v2 = vertices[i + 2].sub(v0);
            v2.w = 0;
            Vector2D c = v1.crossDot(v2);
            area += c.v3len() / 2.0;
        }
        return area;
//        return Math.abs(this.area / 2.0);
    }

    /**
     * 随机选择一个点
     * @param s 权重
     * @param t 权重
     * @return 凸变形上的随机点
     */
    public Vector2D pickedRandomPoint(double s,double t) {
        if (s<0 || s > 1 ||
            t < 0 || t > 1)
            throw new IllegalArgumentException("参数异常，必须是0到1之间");
        return null;
    }

    /**
     * 根据公式，将凸变形分割成不同的字三角形
     * @return 子三角形的面积集合
     */
    public double[] getSubTriangleArea() {
        double[] r = new double[size-2];
        Vector2D v0 = vertices[0];
        for (int i = 0;i<size-2;i++) {
            Vector2D v1 = vertices[i + 1].sub(v0);
            Vector2D v2 = vertices[i + 2].sub(v0);
            double c = v1.cross2D(v2);
            r[i] = c / 2.0;
        }
        return r;
    }


    /**
     * 获取归一化法向子三角形面积
     * @return 子三角型面积
     */
    public double[] getNormSubTriangleArea() {
        double area = getArea();
        double[] r = getSubTriangleArea();
        for (int  i = 0;i<r.length;i++) {
            r[i] = r[i] / area;
        }
        return r;
    }

    /*
    获取 离 p 点最近的凸边形点
     */
    public Vector2D getNearestPoint(Vector2D p) {
        //TODO
        return null;
    }

    /*
    获取 离 p 点 最远的凸变形点
     */
    public Vector2D getFarthestPoint(Vector2D var1) {
        //TODO
        return null;
    };

//    public static void main(String[] args) {
//        Convexity convexity = new Convexity(new Vector2D(),
//                new Vector2D(0, 2), new Vector2D(1, 5), new Vector2D(3, 0), Vector2D.originPoint);
//        Vector2D c = convexity.getCenter();
//        Vector2D averageCenter = Geometry.getAverageCenter(convexity);
//        System.out.println("averageCenter = " + averageCenter);
//
//        Vector2D areaWeightedCenter = Geometry.getAreaWeightedCenter(convexity);
//        System.out.println("areaWeightedCenter = " + areaWeightedCenter);
//    }
}
