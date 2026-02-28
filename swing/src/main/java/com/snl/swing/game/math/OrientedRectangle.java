package com.snl.swing.game.math;

public class OrientedRectangle {
    Vector2D center;
    Vector2D halfExtend;
    double rot; //这是弧度

    public OrientedRectangle(Vector2D center, Vector2D halfExtend, double rot) {
        this.center = center;
        this.halfExtend = halfExtend;
        this.rot = rot;
    }

    public Vector2D getCenter() {
        return center;
    }

    public Vector2D getHalfExtend() {
        return halfExtend;
    }

    public double getRot() {
        return rot;
    }

    /**
     * 这个方法将当前矩形居中原点并反旋转{@code rotate}度
     * @param n 多边形的边，默认顺时针返回边
     * @return 顺时针边
     * @implNote 请注意，顺序很重要
     */
    public SegMent getEdge(int n) {
        SegMent edge = new SegMent();
        Vector2D a = new Vector2D(halfExtend.x, halfExtend.y);
        Vector2D b = new Vector2D(halfExtend.x, halfExtend.y);
        switch (n % 4) {
            case 0:
                a.x = -a.x;
                break;
            case 1:
                b.y = -b.y;
                break;
            case 2 :
                a.y = -a.y;
                b = b.inv();
                break;
            default:
                a= a.inv();
                b.x = -b.x;
                break;
        }
        a.rotate(rot);
        a = a.add(center);

        b.rotate(rot);
        b = b.add(center);

        edge.p1 = a;
        edge.p2 = b;
        return edge;
    }

    /**
     * 这是一个返回各交点的方法，返回的角点不重要，但是顺序很重要
     * @param nr 第几个焦点
     * @return 角点，默认为顺时针
     */
    public Vector2D getCorner(int nr) {
        Vector2D corner = new Vector2D(halfExtend.x, halfExtend.y);
        switch (nr % 4) {
            case 0:
                corner.x = -corner.x;
                break;
            case 1 :
                break;
            case 2:
                corner.y = -corner.y;
                break;
            default:
                corner = corner.inv();
                break;
        }
        corner.rotate(rot);
        corner = corner.add(center);
        return corner;
    }

    /**
     * 获取AABB矩形
     * @return 包裹aabb矩形
     */
    public AABB getAABB() {
        AABB aabb = new AABB(new Vector2D(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY),
                new Vector2D(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY));
        int i;
        for (i = 0;i<4;i++) {
            Vector2D corner = getCorner(i);
            aabb.min.x = Math.min(corner.x,aabb.min.x);
            aabb.min.y = Math.min(corner.y,aabb.min.y);
            aabb.max.x = Math.max(corner.x,aabb.max.x);
            aabb.max.y = Math.max(corner.y,aabb.max.y);
        }
        return aabb;
    }

    /**
     * 获取包裹圆形
     * @return 获取包裹圆形
     */
    public Circle getCircle() {
        double r = halfExtend.len();
        return new Circle(r,center);
    }

    public boolean collideCircle(Vector2D c,double r) {
        AABB aabb = new AABB(new Vector2D(),halfExtend.mul(2));
        Vector2D distance = c.sub(center);
        distance.rotate(-rot);
        c = distance.add(halfExtend);
        return aabb.collisionCircle(c,r);
    }

    public  boolean separatingAxis(SegMent axis,OrientedRectangle or) {
        Range axisRange,r0Range,r2Range,rProjection;
        SegMent edge01 = or.getEdge(0);
        SegMent edge02 = or.getEdge(2);
        Vector2D p = axis.p2.sub(axis.p1);
        axisRange = axis.projection(p);
        r0Range = edge01.projection(p);
        r2Range = edge02.projection(p);
        rProjection = r0Range.hull(r2Range);
        return !rProjection.overlapping(axisRange);
    }

    public boolean collide(OrientedRectangle r2) {
        SegMent edge = getEdge(0);
        if (separatingAxis(edge,r2))
            return false;
        edge = getEdge(1);
        if (separatingAxis(edge,r2))
            return false;
        edge = r2.getEdge(0);
        if (separatingAxis(edge,this))
            return false;
        edge = r2.getEdge(1);
        return !separatingAxis(edge,this);
    }
}

//AI评价的错误之处？？
//1\轴没有法线
//2 、 只投影两条边,只投影两条边我感觉是对的，因为这两条边是平行的