package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TestCircle extends DiKaErPlus {

    Circle circle;
    boolean circleMoving,clicked,drag,collide,showCollidePoint,ccp;

    Line line;
    Vector2D[] vector2DS,v2s,ccs;
    Vector2D nr,testPoint;

    Circle circle02;
    Line[] ql;
    Line l1;

    Circle collideCircle;

    double rot,rotTheta;
    Vector2D point,pCopy;
    Vector2D farP;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        circle = new Circle(1,new Vector2D());
        circle02 = new Circle(1.32,new Vector2D(1,2));
        line = new Line(
                new Vector2D(),new Vector2D(3,3),Line.XIANSHI
        );
        v2s = circle02.collidePointInLine(line);
        testPoint = new Vector2D(3,5);
        rotTheta = Math.PI / 3;
        point = new Vector2D();
        pCopy =  point.clone();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += rotTheta * delta;
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,circle)) {
            circleMoving = true;
        }
        circleMoving = circleMoving && drag;
        if (circleMoving) {
            //TODO
            //如果移动点
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            circle.offset = circle.offset.add(d);
        }

        collide = circle.collisionLine(line);
        if (collide){
            vector2DS = circle.collidePointInLine(line);
        }else {
            vector2DS = null;
        }

        nr = circle.getNearestPoint(testPoint);
        ccp = circle.collideCircle(circle02);
        ccs = circle.collideCircleInPoint(circle02);

        ql = circle.getQLine(new Vector2D(1, 0));

        l1 = circle.getParaLine(line);

        collideCircle = circle.getCollideAreaInSmallestCircle(circle02);

        farP = circle.getFarthestPoint(testPoint);

        //仿射矩形
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        rotate = rotate.mul(Matrix3x3f.translate(circle.r,0));
        Vector2D center = circle.getCenter();
        Vector2D mulled = rotate.mul(center.inv());
//        point = rotate.mul(pCopy);
        point = mulled.add(center);

        clicked = false;
    }

    public static void main(String[] args) {
        launchGame(new TestCircle());
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.cyan);
//        Circle scaled = circle.getScaled(2);
//        drawCircle(g2,scaled,false);
//
//        Circle translated = circle.getTranslated(3, 2);
//        drawCircle(g2,translated,false);
//
//        Circle rotate = circle.getRotate(Math.PI / 2, line.getMoveD());
//        drawCircle(g2,rotate,false);
        if (collide) {
            g2.setPaint(Color.red);
            drawV(vector2DS,g2);
        }
        drawV(v2s,g2);
        if (ccp)
            g2.setPaint(Color.yellow);
        drawCircle(g2,testPoint,0.15,true);
        drawCircle(g2,circle,false);
        drawLine(g2,line);
        drawCircle(g2,nr,0.15,true);
        drawCircle(g2,circle02,false);
        drawLine(g2,l1);

        drawCircle(g2,farP,0.15,true);

        drawCircle(g2,point,0.08,true);
        if (ql != null) {
            for (Line l : ql)
                drawLine(g2, l);
        }
        if (ccs != null)
            drawV(ccs,g2);

        AABB aabb = circle.getAABB();
        drawAAbb(g2,aabb,false);

//        AABB aabb1 = circle.getAABB(Matrix3x3f.translate(1.02, .23));
//        drawAAbb(g2,aabb1,false);
        Stroke stroke = g2.getStroke();
        g2.setStroke(
                new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                        new float[]{3.0F,5.0F,3.0F},2)
        );
        drawLine(g2,circle.getCenter(),testPoint);
        drawLine(g2,circle.getCenter(),line.getMoveD());
//        drawLine(g2,rotate.getCenter(),line.getMoveD());

        if (collideCircle != null) {
            drawCircle(g2, collideCircle, false);
//            System.out.println(collideCircle);
        }

        g2.dispose();
    }


    private void drawV(Vector2D[] vector2DS,Graphics2D g2) {
        if(vector2DS == null)
            return;
        for (Vector2D v : vector2DS) {
            drawCircle(g2,v,0.15,true);
        }
    }

}
