package com.snl.test.frame.collide;

import com.snl.test.frame.SimpleGameFrame;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

//使用向量而不是形状
public class TestCollisionDemo02 extends SimpleGameFrame {

    Vector2D min0,min0Copy;
    Vector2D max0,max0Copy;
    Vector2D rect0Pos;
    boolean rect0Collision,rect0Moving;

    Vector2D min1,min1Copy;
    Vector2D max1,max1Copy;
    Vector2D rect02Pos;
    boolean rect02Collision,rect02Moving;

    boolean clicked,dragging;

    Vector2D c0,c0Pos;
    double r0;
    boolean c0Collision,c0Moving;

    Vector2D c1,c1Pos;
    double r1;
    boolean c1Collision,c1Moving;

    Vector2D mousePos;
    Vector2D mouseDelta;
    Color default_color = Color.lightGray;
    Color collidsionColor = Color.RED;

    public TestCollisionDemo02() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        mousePos = new Vector2D();
        min0 = new Vector2D(-1,-1f);
        max0 = new Vector2D(1,1);

        min1 = new Vector2D(-1,-1);
        max1 = new Vector2D(1,1);

        r0 = 1;
        r1 = 0.75;
        resetPos();
    }

    private void resetPos() {
        rect0Pos = new Vector2D();
        rect02Pos = new Vector2D(4,4);
        c0Pos = new Vector2D(0.0,.0);
        c1Pos = new Vector2D(0, 3.0f);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //TODO
        //获取当前鼠标
        Vector2D pos = getMousePointInWorldPosition();
        //获取鼠标位移
        mouseDelta = pos.sub(mousePos);
        //修正位移
        mousePos = pos;
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //TODO
        Matrix3x3f mat = Matrix3x3f.translate(rect0Pos.getX(),rect0Pos.getY());
        min0Copy = mat.mul(min0); //世界坐标
        max0Copy = mat.mul(max0); //世界坐标

        mat = Matrix3x3f.translate(rect02Pos.getX(), rect02Pos.getY());
        min1Copy = mat.mul(min1); //世界坐标
        max1Copy = mat.mul(max1); //世界坐标

//        定位圆心
        mat = Matrix3x3f.translate(c0Pos.getX(), c0Pos.getY());
        c0 = mat.mul(new Vector2D());
        mat = Matrix3x3f.translate(c1Pos.getX(), c1Pos.getY());
        c1 = mat.mul(new Vector2D());

        //更新
        if (clicked && pointInAABB(min0Copy,max0Copy)) {
            rect0Moving = true;
        }

        if (clicked && pointInAABB(min1Copy,max1Copy))
        {
            rect02Moving = true;
        }

        if (clicked && pointInCircle(mousePos,c0,r0))
        {
            c0Moving = true;
        }

        if (clicked && pointInCircle(mousePos,c1,r1))
        {
            c1Moving = true;
        }

        rect0Moving = rect0Moving && dragging;
        if (rect0Moving)
            rect0Pos = rect0Pos.add(mouseDelta);
        rect02Moving = rect02Moving && dragging;
        if (rect02Moving)
            rect02Pos = rect02Pos.add(mouseDelta);

        c0Moving = c0Moving && dragging;
        if (c0Moving)
            c0Pos = c0Pos.add(mouseDelta);
        c1Moving = c1Moving && dragging;
        if (c1Moving)
            c1Pos = c1Pos.add(mouseDelta);

        //碰撞
        rect0Collision = false;
        rect02Collision = false;
        c0Collision = false;
        c1Collision = false;

        if (insertAABB(min0Copy,max0Copy,min1Copy,max1Copy))
        {
            rect0Collision = rect02Collision = true;
            System.out.println("矩阵相撞");
        }
//
        if (insertCircle(c0,r0,c1,r1))
        {
            c0Collision = c1Collision = true;
            System.out.println("c0与c1相交");
        }

        if (circleCollideAABB(c0,r0,min0Copy,max0Copy))
        {
            c0Collision = true;
            rect0Collision = true;
            System.out.println("c0与r1相撞");
        }

        if (circleCollideAABB(c0,r0,min1Copy,max1Copy))
        {
            c0Collision = true;
            rect02Collision = true;
            System.out.println("c0与r2相撞");
        }

        if (circleCollideAABB(c1,r1,min0Copy,max0Copy))
        {
            c1Collision = true;
            rect0Collision = true;
            System.out.println("c1与r1矩阵相撞");
        }

        if (circleCollideAABB(c1,r1,min1Copy,max1Copy))
        {
            c1Collision = true;
            rect02Collision = true;
            System.out.println("c1与r2矩阵相撞");
        }

    }

    private boolean circleCollideAABB(Vector2D c, double r, Vector2D min, Vector2D max) {
        double d = 0;
        if (c.getX() < min.getX())
            d += Math.pow((c.getX() - min.getX()),2);
        if (c.getX() > max.getX())
        {
            d += Math.pow((c.getX() - max.getX()),2);
        }
        if (c.getY() < min.getY())
            d += Math.pow((c.getY() - min.getY()),2);
        if (c.getY() > max.getY())
            d += Math.pow((c.getY() - max.getY()),2);
        return d < Math.pow(r,2);
    }

    private boolean insertCircle(Vector2D c0, double r0, Vector2D c1, double r1) {
        Vector2D v = c0.sub(c1);
        double r = r0 + r1;
        return v.lenSqr() < Math.pow(r,2);
    }

    private boolean insertAABB(Vector2D min0, Vector2D max0, Vector2D min1, Vector2D max1) {
        if (min0.getX() > max1.getX() || max0.getX() < min1.getX())
            return false;
        if (min0.getY() > max1.getY() || max0.getY() < min1.getY())
            return false;
        return true;

    }

    private boolean pointInCircle(Vector2D mousePos, Vector2D o, double r) {
        Vector2D v = mousePos.sub(o);
        return v.lenSqr() < Math.pow(r,2);
    }

    private boolean pointInAABB(Vector2D min, Vector2D max) {
        Vector2D p = getMousePointInWorldPosition();
        return p.getX() >= min.getX() && p.getX() <= max.getX()
                && p.getY() >= min.getY() && p.getY() <= max.getY();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(rect0Collision?collidsionColor:default_color);
        drawAABB(g2,min0Copy,max0Copy);
        g2.setColor(rect02Collision?collidsionColor:default_color);
        drawAABB(g2,min1Copy,max1Copy);
        g2.setColor(c0Collision?collidsionColor:default_color);
        drawCircle(g2,c0Pos,r0);
        g2.setColor(c1Collision?collidsionColor:default_color);
        drawCircle(g2,c1Pos,r1);
        g2.dispose();
    }

    private void drawCircle(Graphics2D g2, Vector2D o, double r) {
        Matrix3x3f viewport = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        //中心点
        Vector2D v;
        v = new Vector2D(o);
        v = viewport.mul(v);
        Vector2D wh = new Vector2D(2 *r,2 *r);
        wh = scale.mul(wh);
        double w = wh.getX();
        double h = wh.getY();
        double centerX = v.getX() - w / 2.0;
        double centerY = v.getY() - h / 2.0;
        //另一种方法试试
        Shape s = new Ellipse2D.Double(
                centerX,centerY,w,h
        );
        g2.draw(s);
        //绘制总店
        g2.drawString("[%.2f,%.2f]".formatted(o.getX(),o.getY()),
                (int) v.getX(), (int) (v.getY() - 10));
    }

    private void drawAABB(Graphics2D g2, Vector2D min, Vector2D max) {
        Matrix3x3f viewport = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        //获取左上角点
        Vector2D v;
        Vector2D left = new Vector2D(min.getX(),max.getY());
        //左上角
        v = viewport.mul(left);
        double w = max.getX() - min.getX();
        double h = max.getY() - min.getY();

        Vector2D wh = new Vector2D(w,h);
        wh = scale.mul(wh);
        Shape s = new Rectangle2D.Double(
                v.getX(),v.getY(),wh.getX(),wh.getY()
        );
        g2.drawString("[%.2f,%.2f]".formatted(left.getX(),left.getY()),
                (int) v.getX(), (int) (v.getY() - 10));
        //右上角
        double wx = left.getX() + w;
        double sx = v.getX() + wh.getX();
        g2.drawString("[%.2f,%.2f]".formatted(wx,left.getY()),
                (int) sx, (int) (v.getY() - 10));
        //左下角
        wx = left.getX();
        double wy = left.getY() - h;
        double sy = v.getY() + wh.getY();
        sx = v.getX();
        g2.drawString("[%.2f,%.2f]".formatted(wx,wy),
                (int) sx, (int) (sy - 10));
        //右下角
        wx = left.getX() + w;
        wy = left.getY() - h;
        sx = v.getX() + wh.getX();
        sy = v.getY() + wh.getY();
        g2.drawString("[%.2f,%.2f]".formatted(wx,wy),
                (int) sx, (int) (sy - 10));
        g2.draw(s);
    }

    @Override
    protected void reset() {
        super.reset();
        //TODO
        resetPos();
    }

    public static void main(String[] args) {
        launchGame(new TestCollisionDemo02());
    }
}
