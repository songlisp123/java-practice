package com.snl.test.java2D.collision;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointInAABB extends DiKaErPlus {

    Vector2D pos01;
    boolean dragging,clicking;
    double r0;
    Vector2D min;
    Vector2D max;
    boolean c0Moving,recMoving;
    boolean clllision;

    public PointInAABB() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetView();
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
    protected void resetView() {
        viewMat = Matrix3x3f.translate(0,-wordHeight / 2.0);
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicking && pointInCircle(mouse,pos01,r0))
        {
            c0Moving = true;
        }
        if (clicking && pointInAABB(mouse,min,max))
        {
            recMoving = true;
        }
        c0Moving = c0Moving && dragging;
        if (c0Moving)
        {
            pos01 = mouse;
        }
        recMoving = recMoving && dragging;
        if (recMoving) {
            Matrix3x3f rev = getReverseScaleViewPortMat();
            Vector2D v = rev.mul(mouseDelta);
            min = min.add(v);
            max = max.add(v);
        }

        clllision = pointInAABB(pos01,min,max);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        drawAABB(g2,min,max);
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
