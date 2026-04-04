package com.snl.swing.game.math;

import com.snl.swing.game.math.contract.AbstractShape;
import com.snl.swing.game.math.contract.PointIterator;
import com.snl.swing.game.math.contract.Wound;
import com.snl.swing.game.utils.Geometry;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Polygon extends AbstractShape implements Wound {
    //点 坐标云
    protected Vector2D[] vertices;
    //点数量
    protected int size;
    //偏移量
    protected Vector2D offset;
    //显示顶点
    protected boolean showVer;
    //面积
    protected double area;
    //重心
    protected Vector2D center;

    public Polygon() {
    }

    public Polygon(Vector2D[] vertices) {
        validate(vertices);
        this.vertices = vertices;
        this.size = this.vertices.length;
        this.offset = new Vector2D();
        this.center = Geometry.getAverageCenter(this.vertices);
    }

    public Polygon(Polygon polygon) {
        this.vertices = polygon.vertices;
        this.offset = polygon.offset;
        this.area = polygon.area;
        this.size = polygon.size;
        this.showVer = polygon.showVer;
        this.center = polygon.center;
    }

    public Polygon(Vector2D center,Vector2D offset,Vector2D...vector2DS) {
        validate(vector2DS);
        this.center = center;
        this.offset = Objects.requireNonNullElseGet(offset, Vector2D::new);
        this.size = vector2DS.length;
        this.vertices = vector2DS;
    }

    private void validate(Vector2D[] vertices) {
        if (vertices == null)
            throw new IllegalArgumentException("非法参数异常，参数不能为null");
        int length = vertices.length;
        if (length < 3)
            throw new IllegalArgumentException("数组长度必须大于等于3");
        //判断 数组元素里面是否 有 null 值
        for (Vector2D v : vertices) {
            if (v == null)
                throw new NullPointerException("元素不能为null");
        }
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

    @Override
    public Iterator<Vector2D> getVertexIterator() {
        return new PointIterator(getVertices());
    }

    /**
     * 获取点
     * @return 点集
     */
    @Override
    public Vector2D[] getVertices() {
        Vector2D[] copy = Arrays.copyOf(vertices, size);
        for (int i = 0 ;i<size;i++) {
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

    /**
     * 移动
     * @param delta 移动距离
     */
    public void move(Vector2D delta) {
        this.offset = this.offset.add(delta);
    }

    public void translate(double x,double y) {
        offset.x += x;
        offset.y += y;

        this.center.x += x;
        this.center.y += y;
    }

    public void translate(Vector2D m) {
        this.translate(m.x,m.y);
    }

    public Polygon getTranslated(double x , double y) {
        if (x == 0 && y == 0)
            return this;
        else {
            double ox = this.offset.x + x;
            double oy = this.offset.y + y;

            double cx = this.center.x + x;
            double cy = this.center.y + y;
            return new Polygon(
                    new Vector2D(cx,cy),new Vector2D(ox,oy),this.vertices
            );
        }
    }

    public Polygon getTranslated(Vector2D m) {
        if (m == null || m.equals(new Vector2D()))
            return new Polygon(this);
        else {
            Vector2D offsetTranslate = this.offset.add(m);
            Vector2D centerTranslate = this.center.add(m);
            return new Polygon(centerTranslate,offsetTranslate,this.vertices);
        }
    }

    @Override
    public void rotate(double rotateTheta) {
        return;
    }

    @Override
    public void rotate(double rot, Vector2D rotateCenter) {
        return;
    }

    @Override
    public void rotate(double rot, double x, double y) {
        return;
    }

    public Polygon getRotateInstance(double rot,double x,double y) {
        //初始化
        Vector2D c = new Vector2D();
        //旋转矩阵
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        //平移
        c.x = x;
        c.y = y;
        Vector2D[] copy = new Vector2D[size];
        for (int i = 0; i < size; ++i) {
            copy[i] = rotate.mul(vertices[i].sub(c));
            copy[i] = copy[i].add(c);
        }
        Vector2D c2 = this.center.clone();
        //旋转 重心
        c2.sub(c).rotate(rot);
        c2.x += x;
        c2.y += y;
        return new Polygon(c2,this.offset,copy);
    }

//    public Convexity getRotateInstance(double rat,double x,double y) {
//        Vector2D v = new Vector2D(x,y);
//        return this.getRotateInstance(rat,v);
//    }

    public Polygon getRotateInstance(double rot,Vector2D v) {
        return this.getRotateInstance(rot,v.x,v.y);
    }

//    public Convexity getRotateInstance(double rat,Vector2D v) {
//        Matrix3x3f rotate = Matrix3x3f.rotate(rat);
//        Vector2D[] copy = new Vector2D[vertices.length];
//        for (int i = 0;i<copy.length;i++) {
//            copy[i] = rotate.mul(vertices[i].sub(v));
//            copy[i] = copy[i].add(v);
//        }
//        return new Convexity(this.offset,copy);
//    }

    /**
     * 重置状态
     */
    public void reset() {
        offset.x= 0;
        offset.y = 0;
    }


//    public Convexity getScaled(double sx, double sy) {
//        Matrix3x3f scaled = Matrix3x3f.scale(sx, sy);
//        Vector2D[] copy = Arrays.copyOf(vertices, size);
//        for (int i = 0;i<copy.length;i++)
//            copy[i] = scaled.mul(copy[i]);
//        return new Convexity(this.offset,copy);
//    }

    @Override
    public void scale(double scale) {
        this.scale(scale,scale);
    }

    @Override
    public void scale(double sx, double sy) {
        for (int i = 0;i<vertices.length;i++)
            vertices[i] = vertices[i].scale(sx,sy);
    }

    /**
     * 获取缩放多边形
     * @param sx 沿x轴缩放凸边形
     * @param sy 沿y轴缩放凸边形
     * @return 缩放后的凸边形
     * @since 2026年4月4日11:31:25
     */
    public Polygon getScaled(double sx,double sy) {
        if (sx == 1 && sy == 1)
            return this;
        Vector2D[] copy = new Vector2D[size];
        for (int i = 0;i<copy.length;i++)
            copy[i] = vertices[i].scale(sx,sy);
        //缩放重心
        Vector2D scale = this.center.scale(sx, sy);
        return new Polygon(scale,this.offset,copy);
    }

    @Override
    public void shear(double sx,double sy) {
        Matrix3x3f shear = Matrix3x3f.shear(sx, sy);
        for (int i = 0;i<vertices.length;i++)
            vertices[i] = shear.mul(vertices[i]);
    }

    public Polygon getSheared(double sx,double sy) {
        Matrix3x3f shear = Matrix3x3f.shear(sx, sy);
        Vector2D[] copy = new Vector2D[size];
        for (int i = 0;i<copy.length;i++) {
            copy[i] = shear.mul(vertices[i]);
        }
        return new Polygon(this.center,this.offset,copy);
    }

    public void setVertices(Vector2D[] vertices) {
        validate(vertices);
        this.vertices = vertices;
    }

}
