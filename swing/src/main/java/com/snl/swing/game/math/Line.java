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
            case XIANSHI -> {
                this.pos = pos;
                moveD = o;
            }
            case YINGSHI -> {
                n = o.prep();
                c = -(n.dot(pos));
            }
        }
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
