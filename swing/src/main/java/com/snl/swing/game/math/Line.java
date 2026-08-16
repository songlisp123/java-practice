package com.snl.swing.game.math;

public class Line {
    int mode;
    public static final int XIANSHI = 1;
    public static final int YINGSHI = 2;

    public static final int OUTSIDE = 3;
    public static final int COLLISION = 4;

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

    public Line() {}

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
            return true;
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
    @Deprecated
    public double distance(Vector2D p) {
        Vector2D v = this.nearestPoint(p);
        v = p.sub(v);
        return v.len();
    }

    /**
     * 获取 线上最近点
     * @param p 测试点
     * @return 线上最近点 到 p
     * @implNote 《图像宝石》第一卷第一章
     */
    public Vector2D nearestPoint(Vector2D p) {
        //获取 缩放距离
        //第一种方法：
        Vector2D norm = this.n.norm();
        double c = -(norm.dot(pos));
        double t = norm.dot(p) + c;
        return p.sub(norm.mul(t));
        //第二种方法：
//        double t = this.n.dot(p) + this.c;
//        t /= this.n.lenSqr();
//        return p.sub(this.n.mul(t));
    }

    /*
    获取距离
     */
    public double distanceOfPoint(Vector2D p) {
        Vector2D q = this.nearestPoint(p);
        return q.sub(p).len();
    }

    /**
     * 获取与另一条线的角度
     * @param line 测试线
     * @return 返回直线交叉角度
     * @implNote 中学物理
     */
    public double getAngle(Line line) {
        Vector2D l1_norm = this.moveD.norm();
        Vector2D l2_norm = line.moveD.norm();
        double dot = l1_norm.dot(l2_norm);
        if (dot <= Epsilon.PRECISION)
            //如果垂直
            return Math.PI / 2.0;
        if (dot > 1.0 - Epsilon.PRECISION &&
        dot < 1.0 + Epsilon.PRECISION)
            //如果平行
            return 0;
        //否则，调用公式：
        /*
        cos θ = a * b (如果，a、b都是归一化向量)
         */
        return Math.acos(l1_norm.dot(l2_norm));
    }

    /**
     * 获取 竖直 点
     * @param vector2D 测试点
     * @return 竖直线与点相交
     * @since 2026年4月3日22:41:16
     * @implNote 《图像宝石》 第一卷第一章关于 竖直线的讨论
     */
    public Vector2D getVerticalPoint(Vector2D vector2D) {
        /*
        l : a * x + b * y + c = 0
         */
        double a,b,c; //线的参数方程系数
        a = this.moveD.y;
        b = -this.moveD.x;
        c = this.pos.y * b + this.pos.x * a;

        double x,y;
        x = vector2D.x;
        y = (c - a * vector2D.x) / b;
        return new Vector2D(x,y);
    }

    /**
     * 获取 水平 点
     * @param vector2D 测试点
     * @return 水平线与点相交
     * @since 2026年4月3日22:41:16
     * @implNote 《图像宝石》 第一卷第一章关于 水平线的讨论
     * @since 2026年4月3日23:45:45
     */
    public Vector2D getHPoint(Vector2D vector2D) {
         /*
        l : a * x + b * y + c = 0
         */
        double a,b,c; //线的参数方程系数
        a = this.moveD.y;
        b = -this.moveD.x;
        c = this.pos.y * b + this.pos.x * a;

        double x,y;
        y = vector2D.y;
        x = (c - b * vector2D.y) / a;
        return new Vector2D(x,y);
    }

    /**
     * 获取 点到直线的 垂直距离
     * @param v 测试点
     * @return 点 {@code v} 到 改直线的垂直距离
     * @implNote 请注意不是投影距离，而是垂直距离,
     * 《图像宝石》 第一卷第一章关于 竖直线的讨论
     * @since 2026年4月3日23:45:37
     */
    public double getVerticalDistance(Vector2D v) {
        double d; //结果值
        double temp,p,k; //中间值
        p = this.distanceOfPoint(v); //投影距离
        k = moveD.y / moveD.x; // 斜率
        temp = 1 / (1 + Math.pow(k,2));
        temp = Math.sqrt(temp);
        d = p / temp;
        return d;
    }

    /**
     * 获取 点 p 到直线的水平距离，更多信息，请参阅"{@link Vector2D}"
     * @param vector2D 测试点
     * @return 水平距离
     * @since 2026年4月3日22:23:24
     */
    public double getHDistance(Vector2D vector2D) {
        double m,v,h;  //斜率,垂直距离,水平距离
        m = this.moveD.y / this.moveD.x; //斜率
        v = this.getVerticalDistance(vector2D); //竖直距离
        h = v / m;
        return h;
    }

    /**
     * 是否与指定的{@code segment} 相碰撞
     * @param segMent 线段部分
     * @return 是否碰撞
     * @since 2026年4月3日23:45:29
     */
    public int collideSegment(SegMent segMent) {
//        double a,b; //符号
//        Vector2D v1,v2,norm;
//        norm = this.moveD.norm();
//        v1 = this.pos.sub(segMent.p1);
//        v2 = this.pos.sub(segMent.p2);
//        v1 = segMent.p1.sub(this.pos);
//        v2 = segMent.p2.sub(this.pos);
//        a = v1.dot(norm); //点积
//        b = v2.dot(norm); //点积 2
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);
//        if (a * b > 0) {
//            //如果点积大于零,线段在直线一侧
//            return OUTSIDE;
//        }else {
//            //否则线段与直线相交
//            return COLLISION;
//        }
        Vector2D A = segMent.p1;
        Vector2D B = segMent.p2;

        // 直线法向量
        Vector2D n = this.moveD.prep().norm();

        double d1 = A.sub(this.pos).dot(n);
        double d2 = B.sub(this.pos).dot(n);

        // 同侧
        if (d1 * d2 > 0) {
            return OUTSIDE;
        }

        // 平行情况（避免除0）
        double denom = this.moveD.prep().dot(B.sub(A));
        if (Math.abs(denom) < Epsilon.PRECISION) {
            return OUTSIDE;
        }

        return COLLISION;
    }

    public Vector2D collideSegmentInPoint(SegMent segMent) {
        int mode = this.collideSegment(segMent);
        switch (mode) {
            case OUTSIDE -> {
                return null;
            }
            case COLLISION -> {
                Vector2D mov = segMent.p1.sub(segMent.p2);
                Line l = new Line(segMent.p1,mov,Line.XIANSHI);
                return this.collisionPoint(l);
            }
        }
        return null;
    }

    public boolean collideSegmentBoolean(SegMent segMent) {
        int mode = this.collideSegment(segMent);
        return mode == COLLISION;
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
