package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointInAABB extends DiKaErPlus {

    Vector2D pos01;
    boolean dragging,clicking;
    double r0;
    Vector2D min,minCopy;
    Vector2D max,maxCopy;
    boolean minMoving,maxMoving;
    boolean c0Moving;
    boolean clllision;

    public PointInAABB() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        pos01 = new Vector2D(2,2);
        r0 = .1;
        min = new Vector2D(-1,-1);
        max = new Vector2D(1,1);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicking = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicking && pointInCircle(mouse,pos01,r0))
        {
            c0Moving = true;
        }
        if (clicking && pointInCircle(mouse,minCopy,1))
        {
            minMoving = true;
        }
        if (clicking && pointInCircle(mouse,maxCopy,1))
        {
            maxMoving = true;
        }
        c0Moving = c0Moving && dragging;
        if (c0Moving)
        {
            pos01 = mouse;
        }
        Matrix3x3f rev = getReverseScaleViewPortMat();
        Vector2D v = rev.mul(mouseDelta);
        minMoving = minMoving && dragging;
        maxMoving = maxMoving && dragging;
        if (minMoving)
        {
            min = min.add(v);
        }
        if (maxMoving)
        {
            max = max.add(v);
        }
        check(min,max);
        clllision = pointInAABB(pos01,minCopy,maxCopy);
    }

    private void check(Vector2D s, Vector2D e) {
        double dy = e.getY() - s.getY();
        double dx = e.getX() - s.getX();
        double k = dy / dx;
        if (k < 0)
        {
            if (s.getY() > e.getY())
            {
                minCopy = new Vector2D(s.getX(),e.getY());
                maxCopy = new Vector2D(e.getX(),s.getY());
            }
            else {
                minCopy = new Vector2D(e.getX(),s.getY());
                maxCopy = new Vector2D(s.getX(),e.getY());
            }
        }else {
            if (s.getY() > e.getY())
            {
                minCopy = e;
                maxCopy = s;
            }
            else {
                minCopy = s;
                maxCopy = e;
            }
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        drawAABB(g2,minCopy,maxCopy);
        drawCircle(g2,minCopy,r0);
        drawCircle(g2,maxCopy,r0);
        if (clllision)
            g2.setColor(Color.RED);
        else
            g2.setPaint(Color.cyan);
        drawCircle(g2,pos01,r0);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new PointInAABB());
    }
}
