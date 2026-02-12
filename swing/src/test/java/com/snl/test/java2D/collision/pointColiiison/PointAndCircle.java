package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PointAndCircle extends DiKaErPlus {

    Vector2D c0,c0pos;
    double r;
    Vector2D pos;
    boolean coMoving,posMoving;
    boolean collision;
    boolean clicked,dragging;

    public PointAndCircle() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        c0 = new Vector2D();
        r = 2.3;
        pos = new Vector2D(5,5);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
            r ++;
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
            r--;
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,c0,r))
            coMoving = true;
        if (clicked && pointInCircle(mouse,pos,.1))
            posMoving = true;
        coMoving = coMoving && dragging;
        posMoving = posMoving && dragging;
        Matrix3x3f scale = getReverseScaleViewPortMat();
        Vector2D v = scale.mul(mouseDelta);
        if (coMoving)
        {
            c0 = c0.add(v);
        }
        if (posMoving)
        {
            pos = pos.add(v);
        }

        collision = pointInCircle(pos,c0,r);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        drawCircle(g2,c0,r);
        if (collision)
            g2.setPaint(Color.RED);
        else
            g2.setPaint(Color.cyan);
        drawCircle(g2,pos,.1);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new PointAndCircle());
    }
}
