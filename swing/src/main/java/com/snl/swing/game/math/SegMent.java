package com.snl.swing.game.math;

/**
 * 线段建模
 */
public class SegMent {
    public Vector2D p1,p2;

    private static final int DONT_INTERSECT = 0; //不相交
    private static final int DO_INTERSECT = 1; //交叉
    private static final int COLLINEAR = 2;  //共线

    private static final int PointBeyondP1 = 3; //在 p1 外侧
    private static final int PointBeyondP2 = 4; //在 p2 外侧
    private static final int ONP1P2RANGE = 5; //在 p1、p2 区间

    private static final int OUTSIDE = 6; //线段 在 圆 外面
    private static final int INSIDE = 7; //线段 在 圆 里面
    private static final int INSECTIONINONEPOINT = 8; //线段 相交于一点
    private static final int COLLISION = 9; //线段 相交于一点

    public SegMent() {
    }

    public Range projection(Vector2D on) {
        Range r1 = new Range();
        Vector2D unit = on.norm();
        r1.min = p1.dot(unit);
        r1.max = p2.dot(unit);
        r1.sort();
        return r1;
    }

    /*
    判断是否与另一条线段相交
     */
    public int collisionSegment(SegMent segMent) {
        double a1,a2,b1,b2,c1,c2; //线系数
        double r1,r2,r3,r4; //符号值
        double denom; //中间值

        /*
        计算当前线的系数方程：
        a1*x + b1 * y + c1 =  0
         */
        a1  = p2.y - p1.y;
        b1 = p1.x - p2.x;
        c1 = p2.x * p1.y - p2.y * p1.x;

        /*
        计算 r3和r4
         */
        r3 = a1 * segMent.p1.x + b1 * segMent.p1.y + c1;
        r4 = a1 * segMent.p2.x + b1 * segMent.p2.y + c1;

        //如果符号相同
        if (r3 != 0 && r4 != 0 && this.sameSigns(r3,r4)) {
            //无相交
            return DONT_INTERSECT;
        }

        /*
        计算第二个线的方程系数
         */
        a2 = segMent.p2.y - segMent.p1.y;
        b2 = segMent.p1.x - segMent.p2.x;
        c2 = segMent.p2.x * segMent.p1.y - segMent.p2.y * segMent.p1.x;

        /*
        计算 r1 和 r2
         */
        r1 = a2 * this.p1.x + b2 * this.p1.y + c2;
        r2 = a2 * this.p2.x + b2 * this.p2.y + c2;

        //判断符号
        if (r1 != 0 && r2 != 0 && sameSigns(r1,r2)) {
            //无相交
            return DONT_INTERSECT;
        }

        denom  = a1 * b2  - a2 * b1;
        if (denom == 0)
            return COLLINEAR;
        //否则 相交
        return DO_INTERSECT;
    }

    //返回交点
    public Vector2D collidePoint(SegMent segMent) {
        Vector2D v = new Vector2D();
        int mode = this.collisionSegment(segMent);
        switch (mode) {
            case DONT_INTERSECT  , COLLINEAR -> {
                return null;
            }
            case DO_INTERSECT -> {
                double a1,a2,b1,b2,c1,c2;
                double demo,num;
                a1  = this.p2.y - this.p1.y;
                b1 = this.p1.x - this.p2.x;
                c1 = p2.x * p1.y - p2.y * p1.x;

                a2 = segMent.p2.y - segMent.p1.y;
                b2 = segMent.p1.x - segMent.p2.x;
                c2 = segMent.p2.x * segMent.p1.y - segMent.p2.y * segMent.p1.x;

                demo = a1 * b2 - a2 * b1;

                num = b1 * c2 - b2 * c1;
//                v.x = (num < 0 ? num-offset : num + offset) / demo;
                v.x = num / demo;

                num = a2 * c1 - a1 * c2;
//                v.y = (num < 0? num - offset : num + offset) / demo;
                v.y = num / demo;
            }
        }
        return v;
    }

    /**
     * 获取 点 的模式
     * @param p 测试点
     * @return
     */
    public int getNearestPointMode(Vector2D p) {
        if (p == null)
            throw new IllegalArgumentException("非法参数异常，参数不能为null");
        double t = (p.x - p1.x) * (p2.x - p1.x) +
                (p.y - p1.y) * (p2.y - p1.y);
        if (t < 0) {
            //p 在 线段p1、p2外，接近p1
            return PointBeyondP1;
        }else {
            //判断是否接近p2
            t = (p2.x - p.x) * (p2.x - p1.x)
                    + (p2.y - p.y) * (p2.y - p1.y);
            if (t < 0)
                // p 在 p2侧 外
                return PointBeyondP2;
            else
            {
                return ONP1P2RANGE;
            }
        }
    }

    /**
     * 获取接近点p的线上的点
     * @param p 测试点
     * @return
     */
    public Vector2D getNearestPoint(Vector2D p) {
        int mode = this.getNearestPointMode(p);
        Vector2D v = null;
        switch (mode) {
            case PointBeyondP1 -> v = this.p1;
            case PointBeyondP2 -> v = this.p2;
            case ONP1P2RANGE -> {
                //否则 p在线段 区间中
                Vector2D m = p1.sub(p2).norm();
                Line l = new Line(p2,m,Line.YINGSHI);
                v = l.nearestPoint(p);
            }
        }
        return v;
    }

    /**
     * 是否符号相同
     * @param a 测试数一
     * @param b 测试数2
     * @return 是否相同符号
     */
    private boolean sameSigns(double a,double b) {
        return a*b >= 0;
    }

    /*
    获取中心点
     */
    public Vector2D getCenter() {
        if (p1.equals(p2))
            return p1;
        Vector2D center;
        Vector2D m = p2.sub(p1).norm(); //计算方向向量 - 归一化
        center = p1.add(m.scale(length() / 2));
        return center;
    }

    /*
    直线长度
     */
    public double length() {
        return p2.sub(p1).len();
    }

    /**
     * 获取 点p到直线的距离
     * @param p 测试点
     * @return
     */
    public double getDistanceOfPoint(Vector2D p) {
        int mode = this.getNearestPointMode(p);
        double a = -1;
        switch (mode) {
            case PointBeyondP1 -> a = p.sub(p1).len();
            case PointBeyondP2 -> a = p.sub(p2).len();
            case ONP1P2RANGE -> {
                //否则 p在线段 区间中
                Vector2D m = p1.sub(p2).norm();
                Line l = new Line(p2,m,Line.YINGSHI);
                Vector2D nr = l.nearestPoint(p);
                a = p.sub(nr).len();
            }
        }
        return a;
    }

     //TODO
    public int collideCircle(Circle circle) {
        //内部
        if (!circle.containsPointInBoolean(this.p1)
        && !circle.containsPointInBoolean(this.p2)) {
            //在外部，分为三种情况
            //第一种 ： 完全不相交
            double d , r;
            Vector2D mo = p1.sub(p2).norm();
            d = this.getDistanceOfPoint(circle.getCenter());
            r = circle.r;

            if (d > r) {
                return OUTSIDE;
            }
            if (d < r)
                return COLLISION;
        }
        //外部
        if (circle.containsPointInBoolean(this.p1)
        && circle.containsPointInBoolean(this.p2)) {
            return INSIDE;
        }
        //相交与一点
        if (circle.containsPointInBoolean(this.p1)
            && !circle.containsPointInBoolean(this.p2)) {
            //如果p1在内部 且 p2 在外部
            return INSECTIONINONEPOINT;
        }

        if (!circle.containsPointInBoolean(this.p1)
            && circle.containsPointInBoolean(this.p2))
        {
            //如果 p1 在外部 且 p2 在内部
            return INSECTIONINONEPOINT;
        }
        return -1;
    }

    public Vector2D[] collideCircleToVectorArray(Circle circle) {
        int mode = this.collideCircle(circle);
        switch (mode) {
//            case -1 -> throw new RuntimeException("发生未知异常");
            case OUTSIDE,INSIDE -> {
                return null;
            }
            case INSECTIONINONEPOINT -> {
                Vector2D[] v = new Vector2D[1];
                double dot;
                Vector2D norm = this.p1.sub(this.p2).norm();
                Line l = new Line(p2,norm,Line.YINGSHI);
                Vector2D[] vector2DS = circle.collidePointInLine(l);
                for (Vector2D v2d : vector2DS) {
                    dot = v2d.dot(norm);
                    if (dot < 0)
                        //在外侧
                        continue;
                    else
                        //在内测
                        v[0] = v2d;
                }
                return v;
            }
            case COLLISION -> {
                Vector2D[] v;
                Vector2D norm = this.p1.sub(this.p2).norm();
                Line l = new Line(p2,norm,Line.YINGSHI);
                v = circle.collidePointInLine(l);
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "SegMent{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }
}
