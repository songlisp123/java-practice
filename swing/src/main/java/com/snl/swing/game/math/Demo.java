package com.snl.swing.game.math;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Demo extends DiKaErPlus {

    AABB aabb,aabb01;
    boolean insert;
    OrientedRectangle or,or1;
    Vector2D p1,p2;

    boolean p1M,p2M,clicked,drag;
    double r0;
    boolean aabb01Moving,circleMoving;

    int mode,currentMode;
    final String[] collide = {
            "直线与aabb","aabb相撞","aabb与圆","aabb与椭圆","aabb与方向矩形","or与or","or与圆","凸包与凸包"
    };

    Vector2D pos02;
    double ra = 1.2,rb = 0.7,r1 = 1.25;
    boolean tM;
    Vector2D c0;
    boolean orMoving;

    //凸包
    Convexity convexity,convexity02;
    boolean conver01Moving;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        aabb = new AABB(
                new Vector2D(-1,-1),new Vector2D(1,1)
        );
        aabb01 = new AABB(
                new Vector2D(1,1.2),new Vector2D(2.5,2.5)
        );
        or = new OrientedRectangle(
                new Vector2D(2,3),new Vector2D(2,2),Math.PI / 3
        );

        or1 = new OrientedRectangle(
                new Vector2D(-1,1),new Vector2D(1,1),Math.PI / 6
        );

        c0 = new Vector2D(-2,2);
        p1 = new Vector2D(0,1);
        p2 = new Vector2D(2,1.0);
        pos02 = new Vector2D(-2,-2);
        r0 = .1;

//        convexity = new Convexity(
//                new Vector2D(0,0),
//                new Vector2D(2,0),
//                new Vector2D(1,2)
//        );
//
//        convexity02 = new Convexity(
//                new Vector2D(5,0),
//                new Vector2D(7,0),
//                new Vector2D(6,2)
//        );
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_T))
        {
            mode = ++currentMode%collide.length;
        }
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,p1,.25))
            p1M = true;
        if (clicked && pointInCircle(mouse,p2,.25))
            p2M = true;
        if (clicked && aabb01.containsPoint(mouse))
            aabb01Moving = true;
        if (clicked && pointInEllipse(mouse,pos02,ra,rb))
            tM = true;
        if (clicked && pointInCircle(mouse,c0,r1))
            circleMoving = true;
        if (clicked && pointInOrientedRectangle(mouse,or))
            orMoving = true;
        if (clicked && convexity.containsPoint(mouse))
            conver01Moving = true;

        p1M = p1M && drag;
        p2M = p2M && drag;
        aabb01Moving = aabb01Moving && drag;
        circleMoving = circleMoving && drag;
        tM = tM && drag;
        orMoving = orMoving && drag;
        conver01Moving = conver01Moving && drag;

        Matrix3x3f re = getReverseScaleViewPortMat();
        Vector2D v = re.mul(mouseDelta);
        if (p1M)
            p1 = p1.add(v);
        if (p2M)
            p2 = p2.add(v);
        if (aabb01Moving) {
            aabb01.min = aabb01.min.add(v);
            aabb01.max = aabb01.max.add(v);
        }
        if (circleMoving)
            c0 = c0.add(v);
        if (tM)
            pos02 = pos02.add(v);
        if (orMoving)
            or.center = or.center.add(v);

        if (conver01Moving)
            convexity.move(v);

        clicked = false;
        switch (mode) {
            case 0 -> insert = aabb.collisionLineSegment(p1,p2);
            case 1 -> insert = aabb.collisionAABB(aabb01);
            case 2 -> insert = aabb.collisionCircle(c0,r1);
            case 3 -> insert = aabb.collisionEllipse(pos02,ra,rb);
            case 4 -> insert = aabb.collisionOrientedRectangle(or);
            case 5 -> insert = or.collide(or1);
            case 6 -> insert = or.collideCircle(c0,r1);
            case 7 -> insert = convexity.collideOtherConvexity(convexity02);
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        if (insert)
            g2.setColor(Color.red);
        else
            g2.setColor(Color.WHITE);
        drawAAbb(g2,aabb,false);
        drawAAbb(g2,aabb01,false);
        drawCircle(g2,c0,r1,true);
        drawLine(g2,p1,p2);
        drawCircle(g2,p1,r0);
        drawCircle(g2,p2,r0);
        drawEllipse(g2,pos02,ra,rb);
        drawOrientedRectangle(g2,or,false);
        drawOrientedRectangle(g2,or1,false);
        drawConvexity(g2,convexity,false);
        drawConvexity(g2,convexity02,false);
        AABB aabb1 = or.getAABB();
        drawAAbb(g2,aabb1,false);
        Circle circle = or.getCircle();
        drawCircle(g2,circle.center,circle.r,false);
        AABB aabb2 = convexity.computeAABB();
        drawAAbb(g2,aabb2,false);
        g2.drawString("按下 T 键切换撞击模式",30,130);
        g2.drawString("碰撞模式:"+collide[mode],30,150);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new Demo());
    }
}
