package com.snl.swing.game.math;


import java.util.Objects;

public class AABB extends Convexity implements Cloneable {

    public static final int sId = 1;

    protected Vector2D min,max;

    public AABB(Vector2D min, Vector2D max) {
        this.min = min;
        this.max = max;
        super.size = 4;
        super.offset = new Vector2D();
        fillVertices();
        //计算center
        super.center = this.getCenter();
    }

    public AABB() {
        this(new Vector2D(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY)
        ,new Vector2D(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY));
    }

    private void fillVertices() {
        vertices = new Vector2D[size];
        Vector2D l = new Vector2D(min.x,max.y);
        vertices[0] = l;
        vertices[1] = max;
        l = new Vector2D(max.x,min.y);
        vertices[2] = l;
        vertices[3] = min;
    }

    @Override
    public Vector2D getCenter() {
        Vector2D min = getMin();
        Vector2D max = getMax();
//        Vector2D move = max.sub(min);
//        double len = move.len();
//        Vector2D norm = move.norm();
//        return min.add(norm.scale(len / 2.0));
        return min.add(max).div(2);
    }

    //**********************************************************************//
    /* ******************          碰撞测试         ************************ */
    //**********************************************************************//

    @Override
    public boolean containsPoint(double x, double y) {
        if (x < min.x || x > max.x)
            return false;
        return !(y < min.y) && !(y > max.y);
    }


    //TODO 我需要时间来将这个东西重构一下

    public boolean collisionAABB(AABB aabb) {
        Vector2D min = this.getMin();
        Vector2D max = this.getMax();

        Vector2D otherMin = aabb.getMin();
        Vector2D otherMax = aabb.getMax();

        if (aabb == this)
            return false;
        if (min.x > otherMax.x || max.x < otherMin.x)
            return false;
        if (min.y > otherMax.y || max.y < otherMin.y)
            return false;
        return true;
    }

    public boolean contains(AABB aabb) {
        Vector2D min = this.getMin();
        Vector2D max = this.getMax();

        Vector2D otherMin = aabb.getMin();
        Vector2D otherMax = aabb.getMin();


        return otherMin.x >= min.x && otherMin.y >= min.y &&
                otherMax.x <= max.x && otherMax.y <= max.y;
    }

    public boolean collisionCircle(Circle circle) {
        return this.collisionCircle(circle.center,circle.r);
    }

    public boolean collisionCircle(Vector2D center,double r) {
        Vector2D near = new Vector2D();
        Vector2D min = getMin();
        Vector2D max = getMax();
        near.x = clampRange(center.x, min.x, max.x);
        near.y = clampRange(center.y, min.y, max.y);
        near = near.sub(center);
        return near.lenSqr() < Math.pow(r,2);
    }

    public boolean collisionEllipse(Vector2D center,double ra,double rb) {
        //转换到椭圆坐标系，并将圆心设置为center
        Vector2D min = getMin();
        Vector2D max = getMax();
        Vector2D minTsm = min.sub(center).div(ra,rb);
        Vector2D maxTsm = max.sub(center).div(ra,rb);

        //在椭圆空间中判断物体
        Vector2D near = new Vector2D();
        near.x = clampRange(0,minTsm.x, maxTsm.x);
        near.y = clampRange(0,minTsm.y,maxTsm.y);
        return near.lenSqr() <= 1;
    }

    /**
     * 判断是否与 线段🍌
     * @param p1 线段端点
     * @param p2 线段另一端点
     * @return 如果发生相交，返回{@code true}，否则，返回{@code false}
     */
    public boolean collisionLineSegment(Vector2D p1,Vector2D p2) {
        Vector2D d = p2.sub(p1);
        if (!(collisionLine(p1,d))){
            return false;
        }
        Range lRange = new Range();
        lRange.min = p1.x;
        lRange.max = p2.x;
        lRange.sort();

        //aabb在X轴上面的投影
        Range xrange = new Range(min.x,max.x);
        if (!(lRange.overlapping(xrange)))
            return false;
        //AABB在y轴上的投影
        lRange.min = p1.y;
        lRange.max = p2.y;
        lRange.sort();

        xrange = new Range(min.y,max.y);
        return lRange.overlapping(xrange);

    }

    public boolean collideSegment(SegMent segMent) {
        return this.collisionLineSegment(segMent.p1,segMent.p2);
    }

    @Override
    public boolean containsPoint(Vector2D p) {
        return this.contains(p.x,p.y);
    }

    public boolean contains(double x, double y) {
        return x >= min.x + this.offset.x && x <= max.x + this.offset.x &&
                y >= min.y + this.offset.y && y <= max.y + + this.offset.y;
    }

    /**
     * 判断是否与显示定义的线相交，有关线的定义请参阅{@link Line}
     * @param base 线的基点
     * @param direction 线的方向
     * @return 如果发生相交，返回{@code true}，否则，返回{@code false}
     */
    private boolean collisionLine(Vector2D base,Vector2D direction) {
        Vector2D f = direction.prep(); //法向量
        //TODO 我可以实现最优雅的部分
        Vector2D c1  = getMin();
        Vector2D c2 = getMax();
        Vector2D c3 = new Vector2D(c2.x,c1.y); //第三
        Vector2D c4 = new Vector2D(c1.x, c2.y); //第一

        double cd1,cd2,cd3,cd4;
        c1 = c1.sub(base);
        c2 = c2.sub(base);
        c3 = c3.sub(base);
        c4 = c4.sub(base);

        cd1 = c1.dot(f);
        cd2 = c2.dot(f);
        cd3 = c3.dot(f);
        cd4 = c4.dot(f);

        return cd1 * cd2 <= 0 ||
                cd2 * cd3 <= 0 ||
                cd3 * cd4 <= 0;
    }

    public boolean collideLine( Line line) {
        Vector2D pos = line.getPos();
        Vector2D norm = line.getMoveD().norm();
        return this.collisionLine(pos,norm);
    }

    public boolean collisionOrientedRectangle(OrientedRectangle or) {
        //方法一：将aabb矩形转换成obb
        //第一种方法
//        Vector2D ct = max.add(min).div(2);
//        Vector2D hf = max.sub(min).div(2);
//        OrientedRectangle o = new OrientedRectangle(ct,hf,0);
//        return OrientedRectangle.collide(o,or);
        //第二种方法，采用常规的sat原理
        AABB aabb = or.getAABB();
        if (!(collisionAABB(aabb)))
            return false;
        SegMent edge = or.getEdge(0);
        if (separating_axis_for_rectangle(edge))
            return false;
        edge = or.getEdge(1);
        return !separating_axis_for_rectangle(edge);
    }

    private boolean separating_axis_for_rectangle(SegMent axis) {
        SegMent rEdge01 = new SegMent();
        SegMent rEdge02 = new SegMent();
        Range axisRange,r0Range,r1Range,rProjection;
        Vector2D n = axis.p2.sub(axis.p1);

        rEdge01.p1 = getCorner(0);
        rEdge01.p2 = getCorner(1);

        rEdge02.p1=getCorner(2);
        rEdge02.p2 = getCorner(3);

        axisRange = axis.projection(n);
        r0Range = rEdge01.projection(n);
        r1Range = rEdge02.projection(n);
        rProjection = r0Range.hull(r1Range);

        return !rProjection.overlapping(axisRange);
    }

    private double clampRange(double c,double min,double max) {
        double clamp;
        if (c < min) clamp = min;
        else clamp = Math.min(c, max);
        return clamp;
    }

    @Override
    public Vector2D pickedRandomPoint(double s, double t) {
        if (s < 0 || s > 1 ||
                t < 0 || t > 1)
            throw new IllegalArgumentException("参数异常，必须是0到1之间");
        Vector2D result = new Vector2D();
        double w,h;
        w = max.x - min.x;
        h = max.y - min.y;
        s *= w;
        t *= h;

        result.x = min.x + s;
        result.y = max.y - t;
        return result;
    }

    //**********************************************************************//
    /* ******************          AABB变化         *********************** */
    //**********************************************************************//

    public void translate(double dx,double dy) {
/*
        this.min.x += dx;
        this.min.y += dy;
        this.max.x += dx;
        this.max.y += dy;

        this.center.x += dx;
        this.center.y += dy;
*/
        this.offset.x += dx;
        this.offset.y += dy;
        this.center.x += dx;
        this.center.y += dy;
    }

    public void translate(Vector2D d) {
        this.offset = offset.add(d);
        this.center = this.center.add(d);
    }

    public AABB getTranslated(double dx,double dy) {
        Vector2D min = getMin();
        Vector2D max = getMax();
        double x1 = min.x + dx;
        double y1 = min.y + dy;

        double x2 = max.x + dx;
        double y2 = max.y + dy;

        return new AABB(
                new Vector2D(x1,y1),new Vector2D(x2,y2)
        );
    }

    public double getArea() {
        return (max.x - min.x) * (max.y - min.y);
    }

    public AABB union(AABB aabb) {
        min.x = Math.min(this.min.x,aabb.min.x);
        min.y = Math.min(this.min.y,aabb.min.y);
        max.x = Math.max(this.max.x,aabb.max.x);
        max.y = Math.max(this.max.y,aabb.max.y);
        return this;
    }

    public AABB union(AABB aabb,AABB aabb2) {
        this.min.x = Math.min(aabb.min.x,aabb2.min.x);
        this.min.y = Math.min(aabb.min.y,aabb2.min.y);
        this.max.x = Math.max(aabb.max.x,aabb2.max.x);
        this.max.y = Math.max(aabb.max.y,aabb2.max.y);
        return this;
    }

    public AABB getUnion(AABB aabb) {
        return this.clone().union(aabb);
    }

    public AABB extend(double extension) {
        if (extension < 0)
            throw new IllegalArgumentException(extension+"参数必须为正");
        double e = extension * 0.5;
        Vector2D v = new Vector2D(e,e);
        this.min = this.min.add(v);
        this.max = this.max.sub(v);
        return this;
    }

    public AABB getExtended(double ex) {
        return this.clone().extend(ex);
    }

    public AABB scaled(double s) {
       return this.scaled(s,s);
    }

    @Override
    public void scale(double sx, double sy) {
        this.min = this.min.scale(sx);
        this.max = this.max.scale(sy);
    }

    private AABB scaled(double sx, double sy) {
        this.min = this.min.scale(sx);
        this.max = this.max.scale(sy);
        return this;
    }

    public AABB getScaled(double s) {
        return this.clone().scaled(s);
    }

    @Override
    public double getPerimeter() {
        double w = this.max.x - this.min.x;
        double h = this.max.y - this.min.x;
        return 2 * (w + h);
    }

    public AABB intersection(AABB aabb) {
        AABB ab = new AABB();
        Vector2D min = getMin();
        Vector2D max = getMax();

        Vector2D otherMax = aabb.getMax();
        Vector2D otherMin = aabb.getMin();

        ab.min.x = Math.max(min.x,otherMin.x);
        ab.min.y = Math.max(min.y,otherMin.y);
        ab.max.x = Math.min(max.x,otherMax.x);
        ab.max.y = Math.min(max.y,otherMax.y);

        if (ab.min.x > ab.max.x || ab.min.y > ab.max.y) {
            ab.min.x = 0.0F;
            ab.min.y = 0.0F;
            ab.max.x = 0.0F;
            ab.max.y = 0.0F;
        }

        return ab;
    }

    public AABB getIntersection(AABB aabb) {
        return this.clone().intersection(aabb);
    }

    public boolean isDegenerate() {
        return this.min.x == this.max.x || this.min.y == this.max.y;
    }

    public boolean isDegenerate(double error) {
        return Math.abs(this.max.x - this.min.x) <= error
                || Math.abs(this.max.y - this.min.y) <= error;
    }

    //**********************************************************************//
    /* ******************          get/set         *********************** */
    //**********************************************************************//

    public Vector2D getMin() {
        return min.add(offset);
    }

    public Vector2D getMax() {
        return max.add(offset);
    }

    private Vector2D getCorner(int n) {
        //TODO 这个方法很脆弱,不知道该怎么搞 ??? 【未完成 ❌】
        Vector2D r = getMin();
        Vector2D max = getMax();
        switch (n % 4) {
            case 0 :
                r.y = max.y;
                break;
            case 1 :
                r = max;
                break;
            case 2:
                r.x = max.x;
                break;
            default:
                break;
        }
        return r;
    }

    @Override
    public AABB clone() {
        return new AABB(this.min.clone(),this.max.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AABB aabb)) return false;
        return Objects.equals(min, aabb.min) && Objects.equals(max, aabb.max);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.max.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.max.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.min.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.min.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
