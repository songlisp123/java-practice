package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.geo.curve.Arc;
import com.snl.swing.game.math.geo.curve.CubicCurve;
import com.snl.swing.game.math.geo.curve.QuadCurve;
import com.snl.swing.game.math.geo.Path;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TestCurve extends DiKaErPlus {

    boolean drag,cMoving,clicked,c2M,c2MT;
    Arc arc;
    QuadCurve q;
    CubicCurve ce;

    Vector2D cc,c21,c22;
    QuadCurve q1 ;
    QuadCurve q2 ;

    CubicCurve c1,c2;

    Path path;



    @Override
    protected void gameInitial() {
        super.gameInitial();
        arc = new Arc(30,40,100,100,30,60,Arc.PIE);
        q = new QuadCurve(0,0,0.15,0.2,1,1);
        ce = new CubicCurve(0,0,1,0.15,-1,-2,1,1);
        q1 = new QuadCurve();
        q2 = new QuadCurve();

        c1 = new CubicCurve();
        c2 = new CubicCurve();
        CubicCurve.subdivide(ce,c1,c2);

        //路径
        path = new Path();
        path.moveTo(0,0);
        path.lineTo(1,1);
        path.quadTo(2,1,3,2);
        path.lineTo(5,6);
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
        cc = q.getControlPoint01();
        c21 = ce.getControlPoint01();
        c22 = ce.getControlPoint2();
        if (clicked && pointInCircle(mouse,cc,0.1))
            cMoving = true;

        cMoving = drag && cMoving;
        if (cMoving)
        {
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            q.setControlPoint(d);
        }

        if (clicked && pointInCircle(mouse,c21,0.1))
            c2M = true;
        if (clicked && pointInCircle(mouse,c22,0.1))
            c2MT = true;

        c2M = drag && c2M;
        c2MT = drag && c2MT;
        if (c2M)
        {
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            ce.setControlPoint01(d);
        }

        if (c2MT)
        {
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            ce.setControlPoint02(d);
        }

        QuadCurve.subdivide(q,q1,q2);
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawArc((int) arc.getX(), (int) arc.getY(),
                (int) arc.getW(), (int) arc.getH(), (int) arc.getStartAngle(), (int) arc.getExtent());
        g2.setPaint(Color.cyan);
        drawCurve(g2,q);
//        drawCurve(g2,cc);
        g2.setPaint(Color.yellow);
        drawCircle(g2,cc,0.05,false);
//        drawCircle(g2,c21,0.05,false);
//        drawCircle(g2,c22,0.05,false);
//        drawCurve(g2,ce);
        drawCurve(g2,q1);
        drawCurve(g2,q2);

        drawCurve(g2,ce);

        drawPath(g2,path);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestCurve());
    }
 }
