package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TestLine extends DiKaErPlus {

    Line line;
    Line l2;
    boolean insert;
    Vector2D p;
    boolean pMoving,clicking,drag;
    Vector2D n1,n2;
    double dn1,dn2;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        line = new Line(
                new Vector2D(0,0),new Vector2D(1,1),Line.XIANSHI
        );
        l2 = new Line(
                new Vector2D(-1,5),new Vector2D(1,2),Line.XIANSHI
        );
        p = new Vector2D();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicking = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        insert = line.collision(l2); //交点
        if (clicking && pointInCircle(mouse,p,0.25)) {
            pMoving = true;
        }
        pMoving = pMoving && drag;
        if (pMoving) {
            //如果移动点
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            p = p.add(d);
        }
        //获取最近点
        n1 = line.nearestPoint(p);
        n2 = l2.nearestPoint(p);

        //获取距离
        dn1 = line.distanceOfPoint(p);
        dn2 = l2.distanceOfPoint(p);

        clicking = false;
    }



    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.WHITE);
        g2.drawString("dn1=%.2f".formatted(dn1),30,150);
        g2.drawString("dn2=%.2f".formatted(dn2),30,170);
        drawLine(g2,line);
        drawLine(g2,l2);
        Vector2D c = line.collisionPoint(l2);
        drawCircle(g2,p,0.25,false);
        if (c != null)
            drawCircle(g2,c,.1,true);
        drawCircle(g2,n1,0.15,true);
        drawCircle(g2,n2,0.15,true);
        Stroke stroke = g2.getStroke();
        g2.setStroke(
                new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                        new float[]{3.0F,5.0F,3.0F},2)
        );
        drawLine(g2,n2,p);
        drawLine(g2,n1,p);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        p = new Vector2D();
    }

    public static void main(String[] args) {
        launchGame(new TestLine());
    }
}
