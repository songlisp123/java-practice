package com.snl.swing.game.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.OrthographicCamera;
import com.snl.swing.game.curve.HermiteCurve;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestCarema extends DiKaErPlus implements TimingTarget {

    OrthographicCamera orthographicCamera;
    HermiteCurve hc;
    Vector2D p;
    Animator animator;
    float animate_time = 10000f;
    List<Vector2D> path,pp;
        List<Vector3D> pcopy;
    Vector3D pc;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        orthographicCamera = new OrthographicCamera(3);
        orthographicCamera.setPosition(Vector3D.point(0,0,5));
        hc = new HermiteCurve();


        Vector2D[] coord = {Vector2D.originPoint,new Vector2D(1,1),new Vector2D(3,5),new Vector2D(6,3)};
        Vector2D[] ins = new Vector2D[]{new Vector2D(1,3),new Vector2D(3,8),new Vector2D(1,1)};
        Vector2D[] outs = ins;
        float[] t = new float[]{0.0f,1000,5000f,animate_time};
//        hc.initialize(coord,ins,outs,t,coord.length);
//
//        hc.initializeClamped(coord,t,coord.length,new Vector2D(-1,1),new Vector2D(3,1));
//
        hc.initializeNatural(coord,t,coord.length);
        orthographicCamera.setL(0);
        orthographicCamera.setR(6);
        orthographicCamera.setB(0);
        orthographicCamera.setT(6);


        p = Vector2D.originPoint;
        pc = Vector3D.Original_Point;

        path = new ArrayList<>();
        pcopy = new ArrayList<>();
        pp = new ArrayList<>();
        path.add(p);
        pcopy.add(pc);

        animator = new Animator((long) (t[t.length - 1] + 0.5f),this);
        animator.start();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        Vector3D c = Vector3D.point(0,0,0);
         c = Vector3D.point(0,0,1);
//        drawCircle(g2,c,1);

        //投影
        Vector2D center = orthographicCamera.projectionToScreen(c, 150,150,0,0);
        drawCircle(g2,center,0.5,true);

        drawPolyLine(g2,hc.getmPositions(),true);
        drawCircle(g2,p,0.1,false);
        drawCircle(g2,p,0.05,true);

        if (path.size() > 2)
            drawPolyLine(g2, path.toArray(Vector2D[]::new),false);

        //摄像机怎么看？？
        Vector2D M = orthographicCamera.projectionToScreen(pc, 50, 50, 550, 0);
        drawCircleInPixel(M,g2);

        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestCarema());
    }

    private void drawCircle(Graphics2D g2,Vector3D c,double r) {
        drawCircle(g2,new Vector2D(c.x,c.y),r,true);
    }

    private void drawCircleInPixel(Vector2D pos,Graphics2D g2) {
        g2.drawOval((int) (pos.x - 5), (int) (pos.y - 5),10,10);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        p = hc.evaluate((float) (fraction * animate_time));
        path.add(p);
        pc = Vector3D.point(p.x,p.y,0);
        pcopy.add(pc);
    }
}
