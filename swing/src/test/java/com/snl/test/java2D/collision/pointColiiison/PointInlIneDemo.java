package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointInlIneDemo extends DiKaErPlus {

    Vector2D start01,end01; //直线
    Vector2D min,max;
    Vector2D pos; //点

    boolean posMoving,lineMoving,startMoving,endMoving;
    boolean collision;
    boolean clicked,dragging;
    double r0;

    public PointInlIneDemo() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        start01 = new Vector2D(-4,-4);
        end01 = new Vector2D(2,3);
        pos = new Vector2D(3,3);
        r0 = 0.1;
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
        if (clicked && pointInCircle(mouse,pos,r0))
        {
            posMoving  = true;
        }

        if (clicked && pointInCircle(mouse,start01,r0))
        {
            startMoving = true;
        }

        if (clicked && pointInCircle(mouse,end01,r0))
        {
            endMoving = true;
        }

        if (clicked && pointInAABB(mouse,start01,end01))
        {
            lineMoving = true;
        }

        posMoving = posMoving && dragging;
        lineMoving = lineMoving && dragging;
        startMoving = startMoving && dragging;
        endMoving = endMoving && dragging;
        Matrix3x3f re = getReverseScaleViewPortMat();
        Vector2D v = re.mul(mouseDelta);
        if (posMoving) {
            //
            pos = pos.add(v);
        }

        if (startMoving)
        {
            start01 = start01.add(v);
        }

        if (endMoving)
        {
            end01 = end01.add(v);
        }

        if (lineMoving)
        {
            start01 = start01.add(v);
            end01 = end01.add(v);
        }

        check(start01,end01);

    }

    private void check(Vector2D s, Vector2D e) {
        double dy = e.getY() - s.getY();
        double dx = e.getX() - s.getX();
        double k = dy / dx;
        if (k < 0)
        {
            if (s.getY() > e.getY())
            {
                min = new Vector2D(s.getX(),e.getY());
                max = new Vector2D(e.getX(),s.getY());
            }
            else {
                min = new Vector2D(e.getX(),s.getY());
                max = new Vector2D(s.getX(),e.getY());
            }
        }else {
            if (s.getY() > e.getY())
            {
                min = e;
                max = s;
            }
            else {
                min = s;
                max = e;
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
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,
                1,new float[]{3,5,3},2));
        drawAABB(g2,min,max);
        drawLine(g2,start01,pos);
        g2.setStroke(stroke);
        g2.setPaint(Color.cyan);
        drawLine(g2,start01,end01);
        drawCircle(g2,pos,r0);
        drawCircle(g2,start01,r0);
        drawCircle(g2,end01,r0);
        g2.setPaint(Color.WHITE);

        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new PointInlIneDemo());
    }
}
