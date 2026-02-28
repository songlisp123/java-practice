package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointAndRec extends DiKaErPlus {

    Vector2D pos;
    boolean posMoving;
    boolean clicked,drag;
    double r0;
    double rot,theta;

    Vector2D min,minCopy,max,maxCopy;
    Vector2D[] poly,polyCopy;
    Vector2D center;
    boolean insert;

    public PointAndRec() throws HeadlessException {

    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        pos = new Vector2D();
        r0 = .1;
        theta = Math.PI / 3.0;
        min = new Vector2D(-1,-1);
        max = new Vector2D(1,1);
        poly = new Vector2D[] {
                new Vector2D(-1,1),new Vector2D(1,1),
                new Vector2D(1,-1),new Vector2D(-1,-1)
        };
        center = new Vector2D(-2,3);
        polyCopy = new Vector2D[poly.length];
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
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,pos,r0)) {
            posMoving = true;
        }
        posMoving = posMoving && drag;
        Matrix3x3f rev = getReverseScaleViewPortMat();
        Vector2D v = rev.mul(mouseDelta);
         if (posMoving) {
             pos = pos.add(v);
         }

         rot += theta * delta;
         Matrix3x3f rotate = Matrix3x3f.rotate(rot);
         for (int i = 0;i< polyCopy.length;i++) {
             polyCopy[i] = rotate.mul(poly[i]).add(center);
         }

         insert = pointInPoly(pos,polyCopy);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (insert)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        drawCircle(g2,pos,r0,true);
        drawPoly(g2,polyCopy,false);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new PointAndRec());
    }
}
