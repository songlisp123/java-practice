package com.snl.swing.game.math;

import java.util.Objects;

public class Circle {

    public double r;
    public Vector2D center;
    public Vector2D offset;
    public double area;

    /*
    点 与 圆的策略
     */
    public static final int OUTSIDE = 0;
    public static final int INSIDE = 1;
    public static final int ONCIRCLE = 2;
    public static final int CHONGHE = 3;

    /*
    线 与 圆
     */

    public static final int OUTSIDE_LINE = 4; //无交点
    public static final int Q_LINE = 5; //相切
    public static final int COLLIDE_LINE = 6; //相交

    public Circle(double r, Vector2D center) {
        this.r = r;
        this.center = center;
        this.offset = new Vector2D();
        area = Math.PI * Math.pow(this.r,2);
    }

    public Circle(Circle circle) {
        this.r = circle.r;
        this.center = circle.center;
        this.offset = circle.offset;
    }

    public Circle() {
        this.offset = new Vector2D();
    }

    /**
     * 获取离点p最近的点
     * @param p 最近点p
     * @return 返回离点p最近的点
     */
    public Vector2D getNearestPoint(Vector2D p) {
        int i = containsPoint(p);
        Vector2D v = null;
        switch (i) {
            case OUTSIDE -> {
                Vector2D c1 = getCenter();
                Vector2D d = p.sub(c1).norm();
                v = c1.add(d.scale(r));
            }
            case INSIDE -> {
                Vector2D base = getCenter();
                //获取方向向量的归一化
                Vector2D direct = p.sub(base).norm();
                //可以优化
                Vector2D point = base.add(direct.scale(r));
                return point;

//                Line line = new Line(base,direct,Line.YINGSHI);
//                Vector2D[] cts = this.collidePointInLine(line);
//                if (p.x > this.center.x) {
//                    //在右半圆
//                    for (Vector2D vector2D : cts) {
//                        if (vector2D.x > p.x)
//                            v = vector2D;
//                    }
//                }
//                if (p.x < this.center.x) {
//                    //在左半圆
//                    for (Vector2D vector2D : cts) {
//                        if (vector2D.x < p.x)
//                            v = vector2D;
//                    }
//                }
            }

            case ONCIRCLE -> v = p;
            case CHONGHE -> {
                //重合的情况下，取任意点
                Vector2D c1 = getCenter();
                v = new Vector2D(c1.x + r,c1.y);
            }
            case -1 -> throw new IllegalArgumentException("暂未找到该点");
        }

        return v;
    }


    /**
     * 获取 离点p 圆上最远点
     * @param p 测试点
     * @return 最远点
     * @since 2026年4月2日18:23:25
     */
    public Vector2D getFarthestPoint(Vector2D p) {
        Vector2D v = null;
        int mode = containsPoint(p);
        Vector2D c1 = getCenter();
        switch (mode) {
            case CHONGHE -> {
                //与 圆心 重合
                v = new Vector2D(c1.x + r,c1.y);
            }
            case ONCIRCLE,OUTSIDE -> {
                // 在圆上或者 圆外
                Vector2D m = p.sub(c1).norm(); //向量r
                v = c1.sub(m.scale(this.r));
            }
            case INSIDE -> {
                //在内部
                Vector2D m = p.sub(c1).norm(); //向量
                return c1.sub(m.scale(r));
//                Line l = new Line(c1,m,Line.YINGSHI);
//                //测试交点
//                Vector2D[] vs = this.collidePointInLine(l);
//                for (Vector2D v2d : vs) {
//                    Vector2D c = p.sub(v2d).norm(); //归一化向量
//                    double dot = c.dot(m);
//                    if (dot < 0)
//                        return v2d;
//                }
//            }
            }
        }
        return v;
    }

    /**
     * 判断该点的位置
     * @param p 待判断点
     * @return 点模式
     */
    public int containsPoint(Vector2D p) {
        double lenSqr = p.sub(this.center.add(offset)).lenSqr();
        double rd = Math.pow(r,2);
        double diff = lenSqr - rd;
        if (diff < 0) {
            //点在圆内
            return INSIDE;
        } else if (diff > 0) {
            //点在圆外
            return OUTSIDE;
        }else if (Math.abs(diff) < Epsilon.PRECISION) {
            //点在圆上
            return ONCIRCLE;
        } else if (lenSqr <= Epsilon.E) {
            //点与圆心重合
            return CHONGHE;
        }
        return -1;
    }

    /**
     *
     * @param p
     * @return
     * @since 2026年4月2日00:07:08
     */
    public boolean containsPointInBoolean(Vector2D p) {
        return this.containsPoint(p) != OUTSIDE; //不在圆外
    }

    /**
     * 判断 是否与给定的线相交
     * @param line 测试线
     */
    public boolean collisionLine(Line line) {
        int mode = this.collideLine(line);
        return mode != OUTSIDE_LINE;
    }

    /**
     * 获取碰撞点
     * @param line 碰撞线
     * @return 碰撞点坐标，如果未发生碰撞，返回{@code null}
     */
    public Vector2D[] collidePointInLine(Line line) {
        //判断交点
        int mode = this.collideLine(line);
        double a = line.moveD.lenSqr();
        Vector2D D = line.pos.sub(this.center.add(this.offset));
        double b = 2 * line.moveD.dot(D);
        double c = D.lenSqr() - this.r * this.r;
        double d = b * b - 4 * a * c;
        if (d < 0) return null; //无交点
        d = Math.max(0,d);
        double sqrtD = Math.sqrt(d);
        switch (mode) {
            case OUTSIDE_LINE -> {
                return null;
            }
            case Q_LINE -> {
                double t = -b / (2 * a);
                Vector2D p = line.pos.add(line.moveD.mul(t));
                return new Vector2D[]{p};
            }
            case COLLIDE_LINE -> {
                double t1 = (-b + sqrtD) / (2 * a);
                double t2 = (-b - sqrtD) / (2 * a);

                Vector2D p1 = line.pos.add(line.moveD.mul(t1));
                Vector2D p2 = line.pos.add(line.moveD.mul(t2));
                return new Vector2D[]{p1,p2};
            }
        }
        return null;
    }

    /**
     * 私有方法测试相交线
     * @param line 测试相交线
     */
    private int collideLine(Line line) {
        double d = line.distanceOfPoint(getCenter());
        double diff = d - r;
        if (diff > Epsilon.PRECISION) {
            //无交点
            return OUTSIDE_LINE;
        } else if (Math.abs(diff) < Epsilon.E) {
            //相切，一交点
            return Q_LINE;
        }
        //相交，两交点
        return COLLIDE_LINE;
    }

    /**
     * 判断 圆是否相撞
     * @param circle 圆
     * @return
     */
    public boolean collideCircle(Circle circle) {
        if (circle.equals(this))
            return false;
        double d = this.getCenter().sub(circle.getCenter()).len();
        double rd = this.r + circle.r;
        return !(d - rd > Epsilon.E);
    }

    /**
     * 给定一点 相切与 圆
     * @param p 给定点
     * @return 与点相切的直线,如果点在圆内部，返回null
     */
    public Line[] getQLine(Vector2D p) {
        int mode = this.containsPoint(p);
        if (mode == INSIDE || mode == CHONGHE)
            return null;
        Line line = new Line();
        if (mode == ONCIRCLE) {
            //如果点在圆上
            line.pos = p;
            line.moveD = p.sub(this.getCenter()).prep();
            return new Line[]{line};
        }
        //否则点在圆外,有两条切线
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Circle circle)) return false;
        return Double.compare(r, circle.r) == 0
                && Objects.equals(center, circle.center)
                && Objects.equals(offset, circle.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, center, offset);
    }

    //获取面积
    public double getArea() {
        return this.area;
    }

    public Vector2D getCenter() {
        return center.add(offset);
    }

    public void reset() {
        this.offset.x = 0;
        this.offset.y = 0;
    }

    /**
     * 碰撞圆形相交
     * @param circle 测试圆形
     * @return 碰撞交点
     */
    public Vector2D[] collideCircleInPoint(Circle circle) {
        //TODO 待实现
        Vector2D c1 = this.getCenter();
        Vector2D c2 = circle.getCenter();
        Vector2D diff = c2.sub(c1);
        double d = diff.len();
        double r1 = this.r;
        double r2 = circle.r;

        if (d > r1 + r2)
            return null; //不相交
        if (d < Math.abs(r1 - r2))
            return null; //完全包含
        if (d == 0 && r1 == r2)
            return null;  //重合
        Vector2D dir = diff.norm(); //归一化
        // 计算 a
        double a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
        // 基点
        Vector2D p = c1.add(dir.scale(a));
        // 相切（一个点）
        double h2 = r1 * r1 - a * a;
        if (h2 == 0) {
            return new Vector2D[]{p};
        }
        // 两个交点
        double h = Math.sqrt(h2);
        // 垂直方向（法线）
        Vector2D prep = dir.prep();

        Vector2D p1 = p.add(prep.scale(h));
        Vector2D p2 = p.sub(prep.scale(h));
        return new Vector2D[]{p1,p2};
    }

    /**
     * 获取 两圆 相交的面积
     * @param circle 测试圆
     * @return 相交的面积
     */
    public double getCollideAreaInCircle(Circle circle) {
        double area = 0;

        Vector2D c1 = this.getCenter();
        Vector2D c2 = circle.getCenter();
        double d = c1.sub(c2).len(); //中心距离
        //情况一 ： 如果两圆不相交
        if (d > this.r + circle.r + Epsilon.E)
            return area;
        //情况二 ： 如果大圆完全包含小圆
        if (d < Math.abs(this.r-circle.r)) {
            if (this.r > circle.r)
            {
                area = Math.PI * this.r * this.r;
            }
            else {
                area = Math.PI * circle.r * circle.r;
            }
            return area;
        }
        //情况三 : 如果相交时，两圆的圆心在共线轴外
        if (d * d >= Math.abs(this.r * this.r-circle.r * circle.r)) {
            //TODO
        }
        return area;
    }


    /**
     * 获取 相交圆的最小包裹圆
     * @param circle 测试圆
     * @return
     */
    public Circle getCollideAreaInSmallestCircle(Circle circle) {
        //计算 两员 中心距离
        Vector2D c1 = this.getCenter();
        Vector2D c2 = circle.getCenter();
        //中心距离
        double l = Math.abs(c1.sub(c2).len());

        //第一种情况 ：不相交
        if (l > this.r + circle.r + Epsilon.E) {
            return null;
        }

        //第二种情况：大圆包裹小圆
        if (l < Math.abs(this.r - circle.r)) {
            //判断谁是大圆
            if (this.r > circle.r) {
                return circle;
            }
            else
                return this;
        }
        //第三种情况 ： 两圆相交，取最小的圆
        Circle result = new Circle();
        double z,t;
        z = (l * l + this.r * this.r - circle.r * circle.r) / (2 * l);
        t = Math.sqrt(this.r * this.r - z * z);
        result.center = c1.add(
                c2.sub(c1).scale(z / l)
        );
        result.r = t;
        return result;
    }

    /**
     * 过圆心做线与指定的线平行
     * @param line 指定线
     * @return 圆心
     */
    public Line getParaLine(Line line) {
        Vector2D c1 = this.getCenter();
        return new Line(
                c1,line.moveD,Line.YINGSHI
        );
    }

    /**
     * 获取包裹AABB
     */
    public AABB getAABB() {
        Vector2D c1 = this.getCenter();
        double bx = c1.x - this.r;
        double by = c1.y - this.r;
        double tx = c1.x + this.r;
        double ty = c1.y + this.r;
        Vector2D min = new Vector2D(bx,by);
        Vector2D max = new Vector2D(tx,ty);
        return new AABB(min,max);
    }


    /**
     * 获取AABB矩形
     * @param mat 变换矩阵
     * @return 包裹AABB
     */
    public AABB  getAABB(Matrix3x3f mat) {
        //TODO
        if (mat == null)
            return getAABB();
        Vector2D c1 = this.getCenter();
        c1 = mat.mul(c1);
        double bx = c1.x - this.r;
        double by = c1.y - this.r;
        double tx = c1.x + this.r;
        double ty = c1.y + this.r;
        Vector2D min = new Vector2D(bx,by);
        Vector2D max = new Vector2D(tx,ty);
        return new AABB(min,max);
    }


    //**********************************************************************//
    /* ******************          转化操作         *********************** */
    //**********************************************************************//

    //缩放
    public void scale(double scale) {
        this.r  *= scale;
    }

    public Circle getScaled(double scale) {
        double s = this.r * scale;
        return new Circle(s,getCenter());
    }

    //平移
    public void translated(double tx,double ty) {
        offset.x += tx;
        offset.y += ty;
    }

    public void translated(Vector2D moved) {
        this.offset = this.offset.add(moved);
    }

    public Circle getTranslated(double x,double y) {
        double xt = this.offset.x + x;
        double yt = this.offset.y + y;
        Vector2D moved = new Vector2D(xt,yt);
        return new Circle(this.r,getCenter().add(moved));
    }

    public Circle getTranslated(Vector2D p) {
        return getTranslated(p.x,p.y);
    }

    //旋转
    public void rotate(double rotate,Vector2D rotateCenter) {
        this.offset.sub(rotateCenter)
                .rotate(rotate);
        this.offset.add(rotateCenter);
    }

    public Circle getRotate(double rotate,Vector2D rotateCenter) {
        Vector2D c1 = getCenter();
        Matrix3x3f rMat = Matrix3x3f.rotate(rotate);
        Vector2D mulled = rMat.mul(c1.sub(rotateCenter));
        mulled = mulled.add(rotateCenter);
        return new Circle(this.r,mulled);
    }

    //**********************************************************************//
    /* ******************          投影操作         *********************** */
    //**********************************************************************//

    //投影在直线上面
    public Range projectOnVector(Vector2D onto) {
        if (onto == null)
            throw  new IllegalArgumentException("非法参数异常");
        Vector2D c1 = this.getCenter();
        double dot = c1.dot(onto);
        return new Range(dot - this.r ,dot + this.r);
    }

    @Override
    public String toString() {
        return "Circle{" +
                "r=" + r +
                ", center=" + center +
                ", offset=" + offset +
                ", area=" + area +
                '}';
    }

}
