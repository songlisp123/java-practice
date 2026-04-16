package com.snl.swing.game.math;

import com.snl.swing.game.utils.Geometry;

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
        AABB aabb = getAABB();
        AABB aabb02 = convexity.getAABB();

        if (!aabb.collisionAABB(aabb02))
            return false;
        //否则，使用分离轴思想
        /*
        分离轴的思想是：如果存在一条能分离两多边形的直线，那么
        这两个多边形不会相交
         */
        Vector2D[] axis = this.getAxes();
        Range range,range1;
        for (Vector2D a : axis)
        {
             range = this.projectionOntoVector(a);
             range1 = convexity.projectionOntoVector(a);
            if (!range.overlapping(range1))
                return false;
        }
        axis = convexity.getAxes();
        for (Vector2D a : axis)
        {
             range = this.projectionOntoVector(a);
             range1 = convexity.projectionOntoVector(a);
            if (!range.overlapping(range1))
                return false;
        }
        return true;
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
        //TODO 有更好地算法
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
}
