package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointCollisionPoint extends DiKaErPlus {

    Vector2D pos01,pos02;
    boolean dragging,clicking;
    double r0,r1;
    boolean c0Moving,c1Moving;
    boolean clllision;

    public PointCollisionPoint() throws HeadlessException {
        super();
    }

    public static void main(String[] args) {
        launchGame(new PointCollisionPoint());
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetView();
        pos01 = new Vector2D(2,2);
        pos02 = new Vector2D(0,0);
        r0 = r1 = .1;
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
        if (clicking && pointInCircle(mouse,pos02,r1))
        {
            c1Moving = true;
        }
        c0Moving = c0Moving && dragging;
        if (c0Moving)
        {
            pos01 = mouse;
        }
        c1Moving = c1Moving && dragging;
        if (c1Moving)
        {
            pos02 = mouse;
        }
        clllision = CollisionPoint(pos01, pos02);
    }

    private boolean CollisionPoint(Vector2D pos01, Vector2D pos02) {
        return pos01.equals(pos02);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        if (clllision)
            g2.setColor(Color.RED);
        else
            g2.setPaint(Color.cyan);
        drawCircle(g2,pos01,r0);
        drawCircle(g2,pos02,r1);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
    }
}
