package com.snl.swing.game.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.OrthographicCamera;
import com.snl.swing.game.curve.HermiteCurve;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;
import com.snl.swing.game.utils.Utils;
import com.snl.swing.game2d.util.Utility;

import java.awt.*;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

public class TestCarema extends DiKaErPlus implements TimingTarget {

    OrthographicCamera orthographicCamera,camera2;
    HermiteCurve hc;
    Vector2D p;
    Animator animator;
    float animate_time = 10000f;
    List<Vector2D> path,pp;
        List<Vector3D> pcopy;
    Vector3D pc;

    AABB aabb;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        orthographicCamera = new OrthographicCamera(3);
        orthographicCamera.setL(-3);
        orthographicCamera.setR(6);
        orthographicCamera.setB(-3);
        orthographicCamera.setT(6);
        orthographicCamera.setPosition(Vector3D.point(0,0,3));

        camera2 = new OrthographicCamera(4);
        camera2.setL(-6);
        camera2.setR(0);
        camera2.setT(0);
        camera2.setB(-6);
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



        p = Vector2D.originPoint;
        pc = Vector3D.Original_Point;

        path = new ArrayList<>();
        pcopy = new ArrayList<>();
        pp = new ArrayList<>();
        path.add(p);
        pcopy.add(pc);

        aabb = new AABB(
                new Vector2D(-5,-5),new Vector2D(-1,-1)
        );

        animator = new Animator((long) (t[t.length - 1] + 0.5f),this);
        animator.setRepeatCount(Animator.INFINITE);
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

        drawCircle(g2,new Vector2D(1,1),0.1,true);

        AABB viewBoundingBox = orthographicCamera.getViewBoundingBox();
        System.out.println("viewBoundingBox = " + viewBoundingBox);
        if (viewBoundingBox.containsPoint(1,1))
        {
            Vector2D vector2D = orthographicCamera.projectionToScreen(new Vector2D(1, 1).toVector3DinZisZero(), 100, 100, 500, 0);
            drawCircleInPixel(vector2D,g2);
        }

        if (viewBoundingBox.collisionAABB(aabb)){
            AABB aabb1 = viewBoundingBox.intersection(aabb);
            Vector2D v1 = orthographicCamera.projectionToScreen(aabb1.getMin().toVector3DinZisZero(), 100, 100, 500, 0);
            Vector2D v2 = orthographicCamera.projectionToScreen(aabb1.getMax().toVector3DinZisZero(), 100, 100, 500, 0);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v1.x, (int) v2.y);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v1.x, (int) v2.y);

        }

        drawPolyLine(g2,hc.getmPositions(),true);
        drawCircle(g2,p,0.1,false);
        drawCircle(g2,p,0.05,true);

        if (path.size() > 2)
            drawPolyLine(g2, path.toArray(Vector2D[]::new),false);

        //摄像机怎么看？？
        Vector2D M = orthographicCamera.projectionToScreen(pc, 100, 100, 500, 0);
        drawCircleInPixel(M,g2);

        Utils.drawText(g2,500,100,0,new TextLayout("摄像机视图A",g2.getFont(),g2.getFontRenderContext()));
        Utils.drawText(g2,500,110,0,new TextLayout("范围[%.2f,%.2f]".formatted(orthographicCamera.getL(),orthographicCamera.getR()),g2.getFont(),g2.getFontRenderContext()));

        drawAAbb(g2,aabb,true);

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

        orthographicCamera.setPosition(p.toVector3DinZisZero());
    }
}
