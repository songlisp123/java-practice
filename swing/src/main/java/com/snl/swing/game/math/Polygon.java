package com.snl.swing.game.math;

import java.util.Arrays;
import java.util.Iterator;

public class Polygon {
    //点 坐标云
    protected Vector2D[] vertices;
    protected int size; //点数量
    //偏移量
    protected Vector2D offset;
    //显示顶点
    protected boolean showVer;
    //面积
    protected double area;
    //重心
    protected Vector2D center;

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
     * 是否需要回执顶点
     * @return 绘制顶点
     */
    public boolean isShowVer() {
        return showVer;
    }

    public Iterator<Vector2D> iterator() {
        return new Convexity.PointIterator();
    }

    public void setShowVer(boolean showVer) {
        this.showVer = showVer;
    }

    public int getSize() {
        return size;
    }

    public Vector2D getCenter() {
        return center;
    }

    /*************  几何变换 ****************/

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
        Vector2D[] copy = new Vector2D[vertices.length];
        for (int i = 0;i<copy.length;i++) {
            copy[i] = rotate.mul(vertices[i].sub(v));
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

    /**
     * 获取缩放凸边形
     * @param sx 沿x轴缩放凸边形
     * @param sy 沿y轴缩放凸边形
     * @return 缩放后的凸边形
     */
    public Convexity getScaled(double sx, double sy) {
        Matrix3x3f scaled = Matrix3x3f.scale(sx, sy);
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i = 0;i<copy.length;i++)
            copy[i] = scaled.mul(copy[i]);
        return new Convexity(this.offset,copy);
    }

    public void shear(double sx,double sy) {
        Matrix3x3f shear = Matrix3x3f.shear(sx, sy);
        for (int i = 0;i<vertices.length;i++)
            vertices[i] = shear.mul(vertices[i]);
    }

    public Convexity getSheared(double sx,double sy) {
        Matrix3x3f shear = Matrix3x3f.shear(sx, sy);
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i = 0;i<copy.length;i++)
            copy[i] = shear.mul(copy[i]);
        return new Convexity(this.offset,copy);
    }

    /**
     * 点 迭代器,保留的是副本
     * @since 2026年4月2日20:39:19
     */
    class PointIterator implements Iterator<Vector2D> {

        private  Vector2D[] copy;
        private int index;

        public PointIterator() {
            copy = new Vector2D[size];
            System.arraycopy(vertices,0,copy,0,size);
            index = -1;
        }

        public PointIterator(int offset) {
            copy = new Vector2D[size + offset];
            System.arraycopy(vertices,0,copy,offset,size);
            index = -1; //从索引 -1 开始
        }

        @Override
        public boolean hasNext() {
            return index < size - 1;
        }

        @Override
        public Vector2D next() {
            return copy[++index];
        }
    }
}
