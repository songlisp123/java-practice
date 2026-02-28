package com.snl.test.java2D.collision.LIineColiision;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

//线与AABB
public class LineCollideAABB extends DiKaErPlus {

    private Vector2D min,minCopy,max,maxCopy;
    private boolean minMoving,maxMoving,rectMoving;
    private Vector2D startPos,startPosCopy,endPos;
    private boolean startMoving,endMoving;
    private boolean clicking,dragging;
    private double r0,r1;
    private boolean insection;
    private double rot,rotheta;

    public LineCollideAABB() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        r0 = .1;
        min = new Vector2D(-1,-1);
        max = new Vector2D(1,1);
        startPos = new Vector2D(2,2);
        startPosCopy = new Vector2D(2,2);

        rot = 0;
        rotheta = Math.PI;
        r1 = 2.5;
        endPos = new Vector2D(startPos.getX() - r1,startPos.getY());
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
        if (clicking && pointInCircle(mouse,min,1))
        {
            minMoving = true;
        }
        if (clicking && pointInCircle(mouse,max,1))
        {
            maxMoving = true;
        }
        if (clicking && pointInAABB(mouse,min,max)) {
            rectMoving = true;
        }
        Matrix3x3f rev = getReverseScaleViewPortMat();
        Vector2D v = rev.mul(mouseDelta);
        minMoving = minMoving && dragging;
        maxMoving = maxMoving && dragging;
        rectMoving = rectMoving && dragging;
//        check(min,max);
        if (minMoving)
        {
            min = min.add(v);
        }
        if (maxMoving)
        {
            max = max.add(v);
        }

        if (rectMoving) {
            min = min.add(v);
            max = max.add(v);
        }

        rot += rotheta * delta;
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        startPosCopy = rotate.mul(startPos).add(endPos);


        //碰撞检查
        insection = lineInsertAABB(min,max,startPosCopy,endPos);

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
        if (insection)
            g2.setPaint(Color.RED);
        else
            g2.setColor(Color.green);
        drawAABB(g2,min,max);
        drawCircle(g2,min,r0);
        drawCircle(g2,max,r0);
        drawLine(g2,startPosCopy,endPos);
        drawCircle(g2,startPosCopy,r0,false);
        drawCircle(g2,endPos,r0,true);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        minCopy = min;
        maxCopy = max;
    }

    public static void main(String[] args) {
        launchGame(new LineCollideAABB());
    }
}
