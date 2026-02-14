package com.snl.test.java2D.collision.LIineColiision;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LineCollideEllipse extends DiKaErPlus {

    Vector2D c0;
    double ra,rb;
    Vector2D start,end;
    boolean clicked,dragging;
    boolean startMoving,endMoving,c0Moving;
    boolean collision;
    Vector2D u,v,f,p;

    public LineCollideEllipse() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        c0 = new Vector2D(3,0);
        ra = 1.35;
        rb = 0.7;
        start = new Vector2D(-4,3);
        end = new Vector2D(1,2);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,start,.1))
        {
            startMoving = true;
        }

        if (clicked && pointInCircle(mouse,end,.1))
        {
            endMoving = true;
        }
        if  (clicked && pointInCircle(mouse,c0, ra))
            c0Moving = true;

        startMoving = startMoving && dragging;
        endMoving = endMoving && dragging;
        Matrix3x3f rev = getReverseScaleViewPortMat();
        Vector2D v1 = rev.mul(mouseDelta);
        if (startMoving)
        {
            start = start.add(v1);
        }
        if (endMoving)
            end = end.add(v1);

        c0Moving = c0Moving && dragging;
        if (c0Moving)
        {
            c0 = c0.add(v1);
        }
        //判断距离
        //变换到椭圆坐标系
        Vector2D start01 = start.div(ra, rb);
        Vector2D end01 = end.div(ra, rb);
        Vector2D c1 = c0.div(ra, rb);
        v = c1.sub(start01);
        u = end01.sub(start01);
        //dianji
        double dot = v.dot(u);
        double dotU = u.dot(u);
        double t = Math.max(0,Math.min(dot/dotU,1));
        //投影向量
        p = start01.add(u.scale(t));
        f = c1.sub(p);
        collision = f.lenSqr() < 1;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        if (collision)
            g2.setPaint(Color.RED);
        else
            g2.setPaint(Color.WHITE);
        drawEllipse(g2,c0, ra,rb,false);
        drawLine(g2,start,end);
        drawCircle(g2,start,.1,true);
        drawCircle(g2,end,.1,true);
        drawCircle(g2,c0,.1,true);
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                new float[]{3,5,3},1));
        drawLine(g2,c0,start);
        drawLine(g2,c0,end);
        drawCircle(g2,p.scale(ra,rb),.1);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new LineCollideEllipse());
    }
}
