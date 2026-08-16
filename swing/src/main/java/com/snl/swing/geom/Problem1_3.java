package com.snl.swing.geom;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.utils.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Problem1_3 extends ProblemSolver {

    Vector2D c0,c1;
    double r0,r1;

    Matrix3x3f LToWorld;


    boolean dragging,clicking;

    double theta,rot;


    @Override
    protected void gameInitial() {
        super.duration = 5000L;
        super.gameInitial();
        sb.append("制造一个坦克小demo");


        r0 = 0.75;
        r1 = 0.5;

        super.openTextPanel();

        theta = Math.PI * 2;
        rot = 0;
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }


    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicking = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1) && containsPoint();
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        dragging = clicking && dragging;
        if (dragging) {
            c0 = c0.add(mouseDelta);
        }
    }

    @Override
    void drawContent(Graphics2D g2) {

        Matrix3x3f rotate= Matrix3x3f.rotate(rot);
        Matrix3x3f world = Matrix3x3f.translate(1,1);
        LToWorld = world.mul(rotate);
        c0 = LToWorld.mul(new Vector2D());
        drawCircle(g2,c0,r0,false);
//        Vector2D c1Local = new Vector2D(2,0);
        Matrix3x3f mat = Matrix3x3f.translate(2,0).mul(LToWorld);
        Matrix3x3f p = Matrix3x3f.translate(r1,0).mul(mat).mul(rotate);
        c1 = mat.mul(new Vector2D());
        drawCircle(g2,c1,r1,false);
        Vector2D mulled = p.mul(new Vector2D());
        drawCircle(g2,mulled,0.2,true);

        double r,d,theta;
        r = Math.abs(r0 - r1);
        d = c0.x - c1.x;
        theta = Math.acos(r / d);
        Vector2D t1 = new Vector2D();

        Vector2D t2 = new Vector2D();
        t1.x = r0 * Math.cos(theta);
        t1.y = r0 * Math.sin(theta);

        Vector2D t3 = new Vector2D(t1.x,-t1.y);

        t1 = LToWorld.mul(t1);
        t3 = LToWorld.mul(t3);

        t2.x = c1.x -  r1 * Math.cos(theta);
        t2.y = c1.y -  r1 * Math.sin(theta);

        Vector2D t4 = new Vector2D(t2.x,-t2.y);
        t4 = LToWorld.mul(t4);
        t2 = LToWorld.mul(t2);

        drawLine(g2,t1,t2);
        drawLine(g2,t3,t4);


    }

    public static void main(String[] args) {
        launchGame(new Problem1_3());
    }

    @Override
    public void timingEvent(double fraction) {
        rot = fraction * theta;
    }

    private boolean containsPoint() {
        Vector2D mouse = getMousePointInVector();
        double sr = (mouse.x - c0.x) * (mouse.x - c0.x) +
                (mouse.y - c0.y) * (mouse.y - c0.y);
        return sr > r0 * r0;
    }
}
