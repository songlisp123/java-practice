package com.snl.test.frame.collide;

import com.snl.test.frame.SimpleGameFrame;
import com.snl.test.frame.util.Utils;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TestCollideDemo03 extends SimpleGameFrame {

    protected Point2D rect01Pos; //坐标点
    protected Vector2D min0,min0Copy;
    protected Vector2D max0,max0Copy;
    protected boolean rect01Collision,rect01Moving;

    protected Point2D rect02Pos;//坐标点
    protected Vector2D min1,min1Copy;
    protected Vector2D max1,max1Copy;
    protected boolean rect02Moving;

    protected boolean clicked,dragging;
    protected Paint paint,cllidePaint;
    Vector2D mousePoint;
    Vector2D mouseDelta;
    Color defaultColor1 = Color.WHITE;
    Color defaultColor2 = Color.BLACK;
    Color collideColor1 = Color.red;
    Color collideColor2 = Color.magenta;
    Point2D c0,c0Pos,c1,c1Pos;
    double r0,r1;
    protected boolean c0Moving,c1Moving;

    public TestCollideDemo03() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        //TODO
        mousePoint = new Vector2D();
        min0 = new Vector2D(0.0,0.0);
        max0 = new Vector2D(1,1);

        min1 = new Vector2D(-2,-2);
        max1 = new Vector2D(1,1);
        r0 = 1.5;
        r1 = 3;
        resetPos();
        createPaint();
    }

    private void createPaint() {
        BufferedImage bi = getTextureImage(true); // 创建碰撞纹理
        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        cllidePaint = new TexturePaint(bi,r);

        BufferedImage ti = getTextureImage(false);
        var r1 = new Rectangle2D.Double(0,0,ti.getWidth(),ti.getHeight());
        paint = new TexturePaint(ti,r1);
    }

    private void resetPos() {
        rect01Pos = new Point2D.Double(-3,3);
        rect02Pos = new Point2D.Double(3,3);

        c0Pos = new Point2D.Double();
        c1Pos = new Point2D.Double(-3,-3);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //TODO
        Vector2D mouse = getMousePointInWorldPosition();
        mouseDelta = mouse.sub(mousePoint);
        this.mousePoint = mouse;
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }


    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //TODO
        Matrix3x3f view = Matrix3x3f.translate(rect01Pos.getX(),rect01Pos.getY());
        min0Copy = view.mul(min0);
        max0Copy = view.mul(max0);

        view = Matrix3x3f.translate(rect02Pos.getX(),rect02Pos.getY());
        min1Copy = view.mul(min1);
        max1Copy = view.mul(max1);

        //定位圆形
        view = Matrix3x3f.translate(c0Pos.getX(),c0Pos.getY());
        c0 = view.mul(new Point2D.Double());

        view = Matrix3x3f.translate(c1Pos.getX(),c1Pos.getY());
        c1 = view.mul(new Point2D.Double());

        //选择
        if (clicked && pointInAABB(mousePoint,min0Copy,max0Copy))
        {
            rect01Moving = true;
        }

        if (clicked && pointInAABB(mousePoint,min1Copy,max1Copy))
        {
            rect02Moving = true;
        }

        if (clicked && pointInCircle(mousePoint,c0,r0))
        {
            c0Moving = true;
        }

        if (clicked && pointInCircle(mousePoint,c1,r1))
        {
            c1Moving = true;
        }

        rect01Moving = rect01Moving && dragging;
        if (rect01Moving) {
            rect01Pos = new Point2D.Double(
                    rect01Pos.getX() + mouseDelta.getX(),
                    rect01Pos.getY() + mouseDelta.getY()
            );
        }

        rect02Moving = rect02Moving && dragging;
        if (rect02Moving) {
            rect02Pos = new Point2D.Double(
                    rect02Pos.getX() + mouseDelta.getX(),
                    rect02Pos.getY() + mouseDelta.getY()
            );
        }

        c0Moving = c0Moving && dragging;
        if (c0Moving)
        {
            c0Pos = new Point2D.Double(
                    c0Pos.getX() + mouseDelta.getX(),
                    c0Pos.getY() + mouseDelta.getY()
            );
        }

        c1Moving = c1Moving && dragging;
        if (c1Moving)
        {
            c1Pos = new Point2D.Double(
                    c1Pos.getX() + mouseDelta.getX(),
                    c1Pos.getY() + mouseDelta.getY()
            );
        }
        //逻辑判断失误
        rect01Collision = AABBCollision(min0Copy, max0Copy, min1Copy, max1Copy);
        boolean b = CircleCollision(c0, r0, c1, r1);
        if (b)
            System.out.println("媛媛相交");
        boolean a = AABBCollideCircle(c0,r0,min0Copy,max0Copy);
        if (a)
            System.out.println("aaa");
        boolean c = AABBCollideCircle(c0,r0,min1Copy,max1Copy);
        if (c)
            System.out.println("ccc");
        boolean d = AABBCollideCircle(c1,r1,min0Copy,max0Copy);
        boolean f = AABBCollideCircle(c1,r1,min1Copy,max1Copy);
        if (d)
            System.out.println("ddd");
        if(f)
            System.out.println("fff");
    }

    private boolean AABBCollideCircle(Point2D c, double r, Vector2D min, Vector2D max) {
        double d = 0;
        if (c.getX() < min.getX())
            d += Math.pow((c.getX() - min.getX()),2);
        if (c.getX() > max.getX())
        {
            d += Math.pow((c.getX() - max.getX()),2);
        }
        if (c.getY() < min.getY())
        {
            d += Math.pow((c.getY() - min.getY()),2);
        }
        if (c.getY() > max.getY())
        {
            d += Math.pow((c.getY() - max.getY()),2);
        }
        return d < Math.pow(r,2);
    }

    private boolean CircleCollision(Point2D c0, double r0, Point2D c1, double r1) {
        double distance = c0.distance(c1);
        return distance <= r0 + r1;
    }

    private boolean pointInCircle(Vector2D mousePoint, Point2D c0, double r) {
        Vector2D v = Utils.pointConvertToVector(c0);
        Vector2D v1 = mousePoint.sub(v);
        return v1.lenSqr() < Math.pow(r,2);
    }

    private boolean AABBCollision(Vector2D min0, Vector2D max0, Vector2D min1, Vector2D max1) {
        if (min0.getX() > max1.getX() || max0.getX() < min1.getX())
            return false;
        if (min0.getY() > max1.getY() || max0.getY() < min1.getY())
            return false;
        return true;
    }

    private boolean pointInAABB(Vector2D mousePoint, Vector2D min, Vector2D max) {
        return mousePoint.getX() >= min.getX() && mousePoint.getX() <= max.getX()
                && mousePoint.getY() >= min.getY() && mousePoint.getY() <= max.getY();
    }

    @Override
    protected void reset() {
        super.reset();
        resetPos();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        //TODO
        drawAABB(g2,min0Copy,max0Copy);
        drawAABB(g2,min1Copy,max1Copy);
        drawCircle(g2,c0,r0);
        drawCircle(g2,c1,r1);
        g2.dispose();
    }

    private void drawCircle(Graphics2D g2, Point2D o, double r) {
        g2.setPaint(rect01Collision ? cllidePaint:paint);
        Matrix3x3f view = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();

        Point2D sp = view.mul(o);
        Vector2D v = new Vector2D(2 * r,2 * r);
        v = scale.mul(v); // 获取宽度
        double w = v.getX();
        double h = v.getY();
        double centerX = sp.getX() - w / 2.0;
        double centerY = sp.getY() - h / 2.0;
        Shape s = new Ellipse2D.Double(centerX,centerY, w,h);
        g2.fill(s);
    }

    private void drawAABB(Graphics2D g2, Vector2D min, Vector2D max) {
        g2.setPaint(rect01Collision ? cllidePaint:paint);
        Matrix3x3f view = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        //左上角点
        Point2D p;
        Point2D leftPoint = new Point2D.Double(
                min.getX(),max.getY()
        );
        Vector2D wh = new Vector2D(
                max.getX() - min.getX(),
                max.getY() - min.getY()
        );

        p = view.mul(leftPoint); //左上角点
        wh = scale.mul(wh); //矩形宽高度

        Shape r = new Rectangle2D.Double(
                p.getX(),p.getY(),
                wh.getX(),wh.getY()
        );
        g2.fill(r);
    }

    private BufferedImage getTextureImage(boolean b) {
        int size = 20;
        BufferedImage bi = new BufferedImage(
                size,size,BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(b ? collideColor1 : defaultColor1);
        g2.fillRect(0,0,size / 2 ,size /2);
        g2.setPaint(b ? collideColor2 : defaultColor2);
        g2.fillRect(size / 2,0,size,size / 2);
        g2.setPaint(b ? collideColor2 : defaultColor2);
        g2.fillRect(0,size / 2,size /2 ,size);
        g2.setPaint(b ? collideColor1 : defaultColor1);
        g2.fillRect(size / 2,size /2 ,size,size);
        return bi;
    }

    public static void main(String[] args) {
        launchGame(new TestCollideDemo03());
    }
}
