package com.snl.test.java2D.collision.LIineColiision;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LineAndLineDemo extends DiKaErPlus {

    Vector2D start01,end01;
    Vector2D min01,max01;
    Vector2D start02,end02;
    Vector2D min02,max02;
    boolean rec01Moving,rec02Moving;
    boolean clicked,dragging;
    Vector2D insert;
    double r = 0.1;

    public LineAndLineDemo() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetView();
        initialPos();
    }

    private void initialPos() {
        start01 = new Vector2D(-2,3);
        end01 = new Vector2D(3,5);

        start02 = new Vector2D(-4,4);
        end02 = new Vector2D(5,3);
    }

    @Override
    protected void resetView() {
        viewMat = Matrix3x3f.translate(0,-wordHeight / 2.0);
        axis.createAxis(getViewportTransform(),c,wordWidth);
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
        if (clicked && pointInAABB(mouse,start01,end01))
        {
            rec01Moving = true;
        }
        if (clicked && pointInAABB(mouse,new Vector2D(start02.getX(),end02.getY()),
                new Vector2D(end02.getX(),start02.getY())))
        {
            rec02Moving = true;
        }
        rec01Moving = rec01Moving && dragging;
        rec02Moving = rec02Moving && dragging;
        Matrix3x3f scale = getReverseScaleViewPortMat();
        Vector2D v = scale.mul(mouseDelta);
        if (rec01Moving)
        {
            start01 = start01.add(v);
            end01 = end01.add(v);
        }

        check(start01,end01,min01,max01);

        if (rec02Moving)
        {
            start02 = start02.add(v);
            end02 = end02.add(v);
        }

        //判断交点
        double dy01 = end01.getY() - start01.getY();
        double dx01 = end01.getX()  - start01.getX();
        double b1 = -dy01 * start01.getX() + dx01 * start01.getY();

        double dy02 = end02.getY() - start02.getY();
        double dx02 = end02.getX() - start02.getX();
        double b2 = -dy02 * start02.getX() + dx02 * start02.getY();

        double d = (-dy01 * dx02) - (dx01 * -dy02);
        if (d != 0)
        {
            //使用克莱姆法则求解交点
            double x = (b1 * dx02 - b2 * dx01) / d;
            double y = (-dy01 * b2 - (-dy02 * b1)) / d;
            insert = new Vector2D(x,y);
        }else {
            //这是什么意思？
            //线段平行
        }

    }

    private void check(Vector2D s, Vector2D e, Vector2D min, Vector2D max) {
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
        drawAABB(g2,start01,end01);
        drawAABB(g2,start02,end02);
        g2.setPaint(Color.RED);
        drawCircle(g2,insert,r,false);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    @Override
    protected void drawAABB(Graphics2D g2, Vector2D min, Vector2D max) {
        //一个 低效 的 做法
        double dy = max.getY() - min.getY();
        double dx = max.getX() - min.getX();
        double k = dy / dx;
        super.drawLine(g2,min,max);
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                new float[]{3,5,3},1));
        if (k < 0)
        {
            Vector2D m = new Vector2D(min.getX(),max.getY());
            Vector2D v = new Vector2D(max.getX(),min.getY());
            super.drawAABB(g2, m, v);
        }
        else
            super.drawAABB(g2, min, max);
        if (insert != null)
        {
            drawLine(g2,start01,insert);
            drawLine(g2,start02,insert);
        }
        g2.setStroke(stroke);

    }

    public static void main(String[] args) {
        launchGame(new LineAndLineDemo());
    }
}
