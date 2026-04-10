package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TestCircleDemo02 extends DiKaErPlus {

    //测试 圆 demo
    Circle circleDemo,circleDemo02;
    boolean circleMoving,clicked,drag,collide,showCollidePoint,ccp;

    Vector2D[] collisionPoints;

    boolean stop,step,flag;

    @Override
    protected void gameInitial() {
        super.gameInitial();

        circleDemo = new Circle(0.56,new Vector2D());
        circleDemo02 = new Circle(0.65,new Vector2D(-1,2));
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            stop = !stop;
        step = keyBoardEvent.keyDownOnce(KeyEvent.VK_ENTER);

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,circleDemo)) {
            circleMoving = true;
        }
        circleMoving = circleMoving && drag;
        if (!stop) {
            if (flag) {
                if (circleMoving) {
                    //TODO
                    //如果移动点
                    Matrix3x3f re = getReverseScaleViewPortMat();
                    Vector2D d = re.mul(mouseDelta);
                    circleDemo.translate(d);
                }

                collide = circleDemo.collideCircle(circleDemo02);


                System.out.println(collide);
                flag = false;
            }
            else {
                if (circleMoving) {
                    //TODO
                    //如果移动点
                    Matrix3x3f re = getReverseScaleViewPortMat();
                    Vector2D d = re.mul(mouseDelta);
                    circleDemo.translate(d);
                }

                collide = circleDemo.collideCircle(circleDemo02);
            }
        }else {
            if (step) {
                flag = true;
            }
        }
        if (collide)
        {
            collisionPoints = circleDemo.collideCircleInPoint(circleDemo02);
            //碰撞轴线
            Vector2D p1ToP2 = collisionPoints[1].sub(collisionPoints[0]).norm();
            //碰撞圆心
            Vector2D center = circleDemo.getCenter();
            Vector2D p1ToCenter = center.sub(collisionPoints[0]).norm();
            //点击
            double dot = p1ToCenter.dot(p1ToP2);
            Vector2D result = p1ToCenter.sub(p1ToP2.mul(dot));
            double c1Depth = circleDemo.r - result.len();


            //求另一圆的碰撞圆形
            Vector2D p2Center = circleDemo02.getCenter();
            Vector2D p2ToCenter = p2Center.sub(collisionPoints[0]).norm();
            result = p2ToCenter.sub(p1ToP2.mul(dot));
            double c2Depth = circleDemo.r - result.len();

            double depth = c1Depth + c2Depth;

        }
        else
            collisionPoints = null;
    }

    public static void main(String[] args) {
        launchGame(new TestCircleDemo02());
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.cyan);
        drawCircle(g2,circleDemo,false);
        drawCircle(g2,circleDemo02,false);

        if (collisionPoints != null)
        {
            for (Vector2D v2D : collisionPoints)
                drawCircle(g2,v2D,0.02,true);
        }
        g2.dispose();
    }
}
