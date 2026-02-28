package com.snl.swing.game.math;

import java.util.Arrays;

public class Convexity {
    Vector2D[] vertices;
    int size; //点数量
    Vector2D offset;
    //显示顶点
    boolean showVer;

    public Convexity() {
    }

    public Convexity(Vector2D offset, Vector2D... vertices) {
        this.offset = offset;
        this.vertices = vertices;
        size = this.vertices.length;
    }

    /**
     * 获取全部的边
     * @return 边集合
     */
    public SegMent[] getAllEdge() {
        SegMent[] segMents = new SegMent[size];
        for (int i=0;i< segMents.length;i++) {
            segMents[i] = getEdge(i);
        }
        return segMents;
    }

    /**
     * 获取投影轴
     * @return 投影轴集合
     */
    public Vector2D[] getAxis() {
        SegMent[] edges = getAllEdge();
        Vector2D[] axis = new Vector2D[size];
        for (int i = 0;i<axis.length;i++) {
            SegMent edge = edges[i];
            Vector2D prep = edge.p2.sub(edge.p1).prep();
            axis[i] = prep;
        }
        return axis;
    }

    /**
     * 获取边
     * @param n 第n条边
     * @return 边
     */
    public SegMent getEdge(int n) {
        if (n < 0 || n >= size) {
            throw new IllegalArgumentException("非法参数异常，边界必须在"+0+"到"+size+"区间");
        }
        SegMent edge = new SegMent();
        edge.p1 = this.vertices[n].add(offset);
        edge.p2 = this.vertices[(n+1)%size].add(offset);
        return edge;
    }

    /**
     * 获取点
     * @return 点集
     */
    public Vector2D[] getVertices() {
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i =0 ;i<size;i++) {
            copy[i] = copy[i].add(offset);
        }
        return copy;
    }

    /**
     * 将当前形状投影到轴线上
     * @param on 投影轴
     * @return 返回一维距离
     */
    public Range projectionOntoVector(Vector2D on) {
        Vector2D norm = on.norm();
        Range range = new Range();
        for (Vector2D v : vertices) {
            double dot = v.add(offset).dot(norm);
            if (dot < range.min)
                range.min = dot;
            if (dot > range.max)
                range.max = dot;
        }
        return range;
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
     * 是否包含某点,使用的是奇偶规则
     * @param pos 测试点
     * @return 如果包含该店，返回{@code true},否则返回{@code false}
     */
    public boolean containsPoint(Vector2D pos) {
        int inside = 0;
        Vector2D[] copy = getVertices();
        Vector2D s = copy[copy.length - 1];
        boolean start = pos.getY() > s.getY();
        for (Vector2D e : copy) {
            boolean end = pos.getY() > e.getY();
            if (start != end) {
                //计算
                double k = (e.getY() - s.getY()) / (e.getX() - s.getX());
                double insertX = s.getX() + (pos.getY() - s.getY()) / k;
                if (insertX > pos.getX())
                    inside++;
            }
            start = end;
            s = e;
        }
        return inside % 2 != 0;
    }

    /**
     * 获取AABB矩形
     * @return 包裹aabb矩形
     */
    public AABB getAABB() {
        AABB aabb = new AABB(new Vector2D(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY),
                new Vector2D(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY));
        Vector2D[] v = getVertices();
        for (Vector2D corner : v) {
            aabb.min.x = Math.min(corner.x,aabb.min.x);
            aabb.min.y = Math.min(corner.y,aabb.min.y);
            aabb.max.x = Math.max(corner.x,aabb.max.x);
            aabb.max.y = Math.max(corner.y,aabb.max.y);
        }
        return aabb;
    }

    //TODO 待办如何找到多边形的包围圆？？ 【未完成 ❌】
//    public Circle getCircle() {
//        AABB aabb = getAABB();
//        Vector2D ct = aabb.max.add(aabb.min).div(2);
//        Vector2D hf = aabb.max.sub(aabb.min).div(2);
//        OrientedRectangle o = new OrientedRectangle(ct,hf,0);
//        double r = o.halfExtend.len();
//        return new Circle(r,o.center);
//    }

    /**
     * 移动
     * @param delta 移动距离
     */
    public void move(Vector2D delta) {
        this.offset = this.offset.add(delta);
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
    public double getArea() {
        Vector2D v0 = vertices[0];
        double area = 0;
        for (int i = 0;i<size-2;i++) {
            Vector2D v1 = vertices[i + 1].sub(v0);
            v1.w = 0;
            Vector2D v2 = vertices[i + 2].sub(v0);
            v2.w = 0;
            Vector2D c = v1.crossDot(v2);
            area += c.v3len();
        }
        return area;
    }

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
            v1.w = 0;
            Vector2D v2 = vertices[i + 2].sub(v0);
            v2.w = 0;
            Vector2D c = v1.crossDot(v2);
            r[i] = c.v3len() / 2.0;
        }
        return r;
    }

    public double[] getNormSubTriangleArea() {
        double[] r = getSubTriangleArea();
        for (int  i = 0;i<r.length;i++) {
            r[i] = r[i] / getArea();
        }
        return r;
    }

    /**
     * 是否需要回执顶点
     * @return 绘制顶点
     */
    public boolean isShowVer() {
        return showVer;
    }

    public void setShowVer(boolean showVer) {
        this.showVer = showVer;
    }

    public void translate(double x,double y) {
        offset.x += x;
        offset.y += y;
    }

    public void translate(Vector2D m) {
        this.translate(m.x,m.y);
    }

    public Convexity getTranslated(Vector2D m) {
        Vector2D off = offset.add(m);
        return new Convexity(off,vertices);
    }

    public Convexity getRotateInstance(double rat,double x,double y) {
        Vector2D v = new Vector2D(x,y);
        return this.getRotateInstance(rat,v);
    }

    public Convexity getRotateInstance(double rat,Vector2D v) {
        Matrix3x3f rotate = Matrix3x3f.rotate(rat);
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i = 0;i<copy.length;i++) {
            copy[i] = rotate.mul(copy[i].sub(v));
            copy[i] = copy[i].add(v);
        }
        return new Convexity(this.offset,copy);
    }


    /**
     * 重置状态
     */
    public void reset() {
        offset.x= 0;
        offset.y = 0;
    }

    public Convexity getScaled(double sx, double sy) {
        Matrix3x3f scaled = Matrix3x3f.scale(sx, sy);
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i = 0;i<copy.length;i++)
            copy[i] = scaled.mul(copy[i]);
        return new Convexity(this.offset,copy);
    }
}
