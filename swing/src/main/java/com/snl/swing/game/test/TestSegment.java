package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TestSegment extends DiKaErPlus {

    private SegMent segMent,s2;
    private Vector2D v;
    private boolean drag,clicked,m1,m2,m3;
    private Vector2D testPoint,nr;
    Vector2D center,p2Copy;
    Vector2D project;
    Range projection;

    Circle circle,c2;
    Vector2D segCollideCircle;

    double rot,rotTheta;
    boolean moving;

    double rot2,rotTheta2;

    double speed;

    Vector2D[] vector2DS,vs,vs2;

    Vector2D p1Copy,p3Copy;

    SegMent scaledSegment,copy;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        segMent = new SegMent();
        s2 = new SegMent();
        segMent.p1 = new Vector2D(-3,-3);
        segMent.p2 = new Vector2D(2,0);
        p2Copy = segMent.p2.clone();

        s2.p1 = new Vector2D(-1,2);
        p1Copy = new Vector2D(-1,2);
        s2.p2 = new Vector2D(-2,3);
        p3Copy = new Vector2D(-2,3);

        testPoint = new Vector2D(3,5);

        project = new Vector2D(-1,3);

        rotTheta = Math.PI / 6;
        rotTheta2 = Math.PI / 4;

        circle = new Circle(3,new Vector2D());
        c2 = new Circle(1,new Vector2D());
        speed = 0.1;

        scaledSegment = new SegMent(
                new Vector2D(2,3),new Vector2D(-2,1)
        );

        copy = scaledSegment.clone();
//        scaledSegment.scale(0.5);
//        scaledSegment.shear(0.2,0.6);
        scaledSegment.rotate(Math.PI / 3);

    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //更新
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            moving = !moving;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP)) {
            rotTheta += rotTheta * speed;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN)) {
            rotTheta -= rotTheta * speed;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        //更新精灵
        if (clicked && pointInCircle(mouse,segMent.p1,0.15)) {
            m1 = true;
        }
        m1 = m1 && drag;

        if (clicked && pointInCircle(mouse,s2.p2,0.15)) {
            m2 = true;
        }
        m2 = m2 && drag;

        if (clicked && pointInCircle(mouse,s2.p1,0.15))
            m3 = true;
        m3 = m3 && drag;

        Matrix3x3f re = getReverseScaleViewPortMat();
        Vector2D d = re.mul(mouseDelta);
        if (m1) {
            segMent.p1.x += d.x;
            segMent.p1.y += d.y;
        }

        if (m2) {
            p3Copy.x += d.x;
            p3Copy.y += d.y;
        }

        if (m3) {
            // 复制
            p1Copy.x += d.x;
            p1Copy.y += d.y;
        }
        Matrix3x3f rotate = Matrix3x3f.rotate(0);
        if (moving) {
            //旋转
            rot += rotTheta * delta;
            rotate = Matrix3x3f.rotate(rot);
//            rotate = rotate.mul(Matrix3x3f.translate(2, 0)); //绕原点 做 半径 为 2 的圆周运动
            segMent.p2 = rotate.mul(p2Copy);

            rot2 += rotTheta2 * delta;
            rotate = Matrix3x3f.rotate(rot2);


        }


        s2.p1 = rotate.mul(p1Copy);
        s2.p2 = rotate.mul(p3Copy);

        v  = segMent.collidePoint(s2);
        nr = segMent.getNearestPoint(testPoint);

        center = segMent.getCenter();

        //投影
        projection = segMent.projection(project);

        vector2DS = segMent.collideCircleToVectorArray(c2);
        vs = segMent.collideCircleToVectorArray(circle);
        vs2 = s2.collideCircleToVectorArray(circle);
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setPaint(Color.CYAN);
        drawLine(g2,segMent.p1,segMent.p2);
        drawLine(g2,s2.p1,s2.p2);
        drawCircle(g2,segMent.p1,0.15,false);
        drawCircle(g2,segMent.p2,0.15,false);
        drawCircle(g2,testPoint,0.1,true);
        drawCircle(g2,nr,0.1,true);

        drawCircle(g2,center,0.1,false);

        drawCircle(g2,circle,false);
        drawCircle(g2,c2,false);

        drawCircle(g2,s2.p2,0.15,false);
        drawCircle(g2,s2.p1,0.15,false);
        if (vector2DS != null) {
            for (Vector2D v : vector2DS) {
                drawCircle(g2, v, 0.05, true);
            }
        }

        if (vs != null) {
            for (Vector2D v : vs) {
                drawCircle(g2, v, 0.05, true);
            }
        }

        if (vs2 != null) {
            for (Vector2D v : vs2) {
                drawCircle(g2, v, 0.05, true);
            }
        }

        if (v != null) drawCircle(g2,v,0.1,true);
        g2.setStroke(
                new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                        new float[]{3.0F,5.0F,3.0F},2)
        );
        drawLine(g2,testPoint,nr);

        drawLine(g2,scaledSegment.p1,scaledSegment.p2);
        drawLine(g2,copy.p1,copy.p2);
        g2.setPaint(Color.red);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestSegment());
    }
}
