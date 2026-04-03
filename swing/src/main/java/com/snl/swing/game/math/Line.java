package com.snl.swing.game.math;

public class Line {
    int mode;
    public static final int XIANSHI = 1;
    public static final int YINGSHI = 2;

    Vector2D pos;
    Vector2D moveD;
    //隐式声明
    Vector2D n;
    double c;

    public Line(Vector2D pos, Vector2D o,int mode) {
        if (mode < XIANSHI || mode > YINGSHI)
            throw new IllegalArgumentException("非法参数异常：参数应该在"+XIANSHI+"和" +YINGSHI+"区间之中");
        this.mode = mode;
        switch (mode) {
            case XIANSHI, YINGSHI -> {
                this.pos = pos;
                moveD = o;
                n = o.prep();
                c = -(n.dot(pos));
            }
        }
    }

    public Line() {

    }

    /**
     * 判断是否与另一条线相撞
     * @param l 测试线
     * @return 如果相撞，则返回{@code true},否则返回{@code false}
     * @implNote 目前俩说，现在这个判断基于线的显示定义
     */
    public boolean collision(Line l) {
        Vector2D l1 = l.getMoveD().prep();
        double dot = this.moveD.dot(l1);
        //还需要判断是否重叠
        Vector2D p = pos.sub(l.pos);
        double d = p.dot(l1);
        if (Math.abs(dot) == 0 && d == 0)
            return false;
        return Math.abs(dot) != 0; //包括反向
    }

    /*
    显示碰撞点的程序
     */
    public Vector2D collisionPoint(Line l) {
        Vector2D c = null;
        if (!collision(l))
            return c;
        //计算交点，可以使用显示，或者是隐式计算，事实上，我们需要这两者。但是先将当前线段设置为显示，
        //l设置为隐式
        Vector2D ln = null;
        double lc = 0;
        double f,d;
        if (l.getMode() == XIANSHI) {
            ln = l.getMoveD().prep();
            lc = -(ln.dot(l.getPos()));
        }
        if (l.getMode() == YINGSHI)
        {
            ln = l.getN();
            lc = l.getC();
        }
        //当前直线显示定义
        f = ln.dot(pos)+lc;
        d = ln.dot(moveD);
        c = pos.sub(moveD.scale(f/ d));
        return c;

    }

    /**
    点距离 改直线 最近的点
    可以简化成 ： 以点p做直线的垂线，垂足为所得
     @implNote 废弃
     */
    @Deprecated
    public Vector2D getNearestPoint(Vector2D p) {
        Vector2D q; //假设垂足为q
        //归一化向量
        if (n == null) {
            n = moveD.prep().norm();
            c = -(n.dot(pos));
        }

        double t = n.dot(p) + c;
        q = p.sub(n.scale(t));
        return q;
    }

    /**
     * 点到 直线的距离
     * @param p
     * @return
     */
    public double distance(Vector2D p) {
        Vector2D v = this.nearestPoint(p);
        v = p.sub(v);
        return v.len();
    }

    /**
     * 获取 线上最近点
     * @param p 测试点
     * @return
     */
    public Vector2D nearestPoint(Vector2D p) {
        Vector2D norm = this.n.norm();
        double t = norm.dot(p) + this.c;
        return p.sub(norm.scale(t));
    }

    /*
    获取距离
     */
    public double distanceOfPoint(Vector2D p) {
        Vector2D q = nearestPoint(p);
        return q.sub(p).len();
    }

    /**
     * 是否与给定的圆碰撞
     * @param circle 碰撞测试圆
     * @return 如果发生碰撞，则返回{@code true}，否则返回{@code true}
     */
    public boolean collideCircle(Circle circle) {
        return circle.collisionLine(this);
    }

    /**
     * 获取与另一条线的角度
     * @param line 测试线
     * @return 返回直线交叉角度
     */
    public double getAngle(Line line) {
        Vector2D l1_norm = this.moveD.norm();
        Vector2D l2_norm = line.moveD.norm();
        return Math.acos(l1_norm.dot(l2_norm));
    }


    /**
     * 获取 点到直线的 垂直距离
     * @param v 测试点
     * @return
     */
    public double getVerticalDistance(Vector2D v) {
        double d;
        double p = distanceOfPoint(v); //垂直投影
        double k = moveD.y / moveD.x;
        d = p / k;
        return d;
    }

    /**
     * 获取隐式声明法向量
     * @return 直线的法向量
     */
    public Vector2D getN() {
        return n;
    }

    /**
     * 获取直线上的点
     * @return 直线上的点
     */
    public Vector2D getPos() {
        return pos;
    }

    /**
     * 获取显示声明的直线的移动方向向量
     * @return 移动向量
     */
    public Vector2D getMoveD() {
        return moveD;
    }

    /**
     * 获取硬式声明的直线的C距离值
     * @return 距离值
     * @implNote 这个距离值加上后，直线上的点回归到原点
     */
    public double getC() {
        return c;
    }

    /**
     * 获取直线定义模式
     * @return ………………
     */
    public int getMode() {
        return mode;
    }
}
