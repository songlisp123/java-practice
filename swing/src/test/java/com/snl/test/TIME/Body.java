package com.snl.test.TIME;

import com.snl.test.vwctor.Vector2D;

import java.awt.geom.Point2D;

//这个类主要是世界坐标
public class Body {
    Point2D o; //世界坐标，起始点
    Vector2D v; //速度向量
    double d;
    int w = 8, h = 8;
    double oldXSpeed;
    double oldYSpeed;
    double dx,dy;

    static final int M_1 = 0; //直线匀速
    static final int M_2 = 1; //直线匀加速
    static final int M_3 = 2; //抛物线运动

    public Body() {
        reset();
    }

    public Body(double vx,double vy) {
        reset();
        v = new Vector2D(vx,vy);
    }

    public Body(Point2D o) {
        this.o = o;
    }

    public Body(Vector2D v, Point2D o) {
        this.v = v;
        this.o = o;
    }

    public void update(double delta, int moveMode,double a) {
        if (moveMode < M_1  || moveMode > M_3)
            throw new IllegalArgumentException("非法参数异常");
        switch (moveMode) {
            case M_1 -> {
                d += v.getX() * delta; //一秒移动一个单位距离
                o = new Point2D.Double(
                        d,o.getY()
                );
            }
            case M_2 -> {
                //匀加速
                oldXSpeed = v.getX();
                double newXSpeed = oldXSpeed + a * delta;
                d += (oldXSpeed + newXSpeed) * delta / 2;
                v = new Vector2D(newXSpeed,0.0);
                o = new Point2D.Double(
                        d,o.getY()
                );
            }

            case M_3 -> {
                //抛物线运动
                oldYSpeed = v.getY();
                double newYSpeed = oldYSpeed + a * delta;
                dx += v.getX() * delta;
                dy += (newYSpeed + oldYSpeed) * delta / 2;
                d += Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                v = new Vector2D(v.getX(),newYSpeed);
                o = new Point2D.Double(
                        dx,dy
                );
            }
        }

    }

    public Point2D getO() {
        return o;
    }

    public void setO(Point2D o) {
        this.o = o;
    }

    public Vector2D getV() {
        return v;
    }

    public void setV(Vector2D v) {
        this.v = v;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void reset() {
        d = 0;
        o = new Point2D.Double(0, 0);
        v = new Vector2D(1.0,.0);
    }


    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void flipXSpeed() {
        this.v = new Vector2D(
                -v.getX(), v.getY()
        );
    }

    public double getSpeed() {
        return Math.sqrt(
                Math.pow(v.getX(),2) + Math.pow(v.getY(),2)
        );
    }
}
