package com.snl.swing.game.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.OrthographicCamera;
import com.snl.swing.game.curve.Linear;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.utils.Utils;
import com.snl.swing.tank.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;

public class TestLinearCarame extends DiKaErPlus implements TimingTarget {

    Linear linear;
    float time_to_anim;
    OrthographicCamera camera;

    Animator animator;
    AABB aabb;
    AABB aabb02;
    Vector2D p;

    SimpleTank tank;

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P))
        {
            if (animator.isRunning())
                animator.pause();
            else
                animator.resume();
        }

        boolean kewDown_A = keyBoardEvent.keyDown(KeyEvent.VK_A);
        boolean kewDown_W = keyBoardEvent.keyDown(KeyEvent.VK_W);
        boolean kewDown_D = keyBoardEvent.keyDown(KeyEvent.VK_D);
        boolean kewDown_S = keyBoardEvent.keyDown(KeyEvent.VK_S);
        boolean kewDown_U = keyBoardEvent.keyDown(KeyEvent.VK_U);
        boolean kewDown_I = keyBoardEvent.keyDown(KeyEvent.VK_I);
        boolean keyDown_Space = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
        boolean keyDown_UP = keyBoardEvent.keyDown(KeyEvent.VK_UP);
        boolean keyDown_DOWN = keyBoardEvent.keyDown(KeyEvent.VK_DOWN);
        boolean keyDown_Tab = keyBoardEvent.keyDown(KeyEvent.VK_C);

        if (kewDown_W)
            tank.rotateClockWise(delta);
        if (kewDown_S)
            tank.rotateClock(delta);
        if (kewDown_A) {
            tank.backward(delta, 1.5);
            tank.getWheel().rotateLocalClockWise(delta);
        }
        if (kewDown_D) {
            tank.forward(delta, 3);
            tank.getWheel().rotateLocalOnClock(delta);
        }

        if (keyDown_UP)
            tank.rotatePaoTaiClockWise(delta);
        if (keyDown_DOWN)
            tank.rotatePaoTaiClock(delta);

        if (keyDown_Space)
            if (!tank.isFiring())
                tank.setFiring(true);

        tank.setFiring(tank.isFiring() && keyDown_Space);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();

        time_to_anim = 6_000;
        linear = new Linear();
        Vector2D[] s = new Vector2D[]{
                new Vector2D(-2,-3),new Vector2D(-1,2),Vector2D.originPoint,new Vector2D(3,1),new Vector2D(4,4)
        };
        p = s[0];
        float[] times = new float[]{.0f,2000.f,3000.f,4000.f,5000.f,time_to_anim};
        linear.Initialize(s,times,s.length);

        camera = new OrthographicCamera(3);
        camera.setL(-2);
        camera.setR(2);
        camera.setB(-2);
        camera.setT(3);
        aabb = new AABB(new Vector2D(-1,-1),new Vector2D(1,1));
        aabb02 = new AABB(new Vector2D(1,-1),new Vector2D(6,4));

        createTank();


        animator = new Animator((long) time_to_anim,this);
        animator.setRepeatCount(Animator.INFINITE);
        animator.start();
    }

    public static void main(String[] args) {
        launchGame(new TestLinearCarame());
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
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        tank.update(delta);

        camera.setPosition(tank.getCenter().toVector3DinZisZero());
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);

        drawPolyLine(g2,linear.getSample_points(),true);
        drawCircle(g2,p,0.1,false);
        drawCircle(g2,p,0.05,true);


        AABB viewBoundingBox = camera.getViewBoundingBox();

        if (viewBoundingBox.collisionAABB(aabb)){
            AABB aabb1 = viewBoundingBox.intersection(aabb);
            Vector2D v1 = camera.projectionToScreen(aabb1.getMin().toVector3DinZisZero(), 100, 100, 480, 20);
            Vector2D v2 = camera.projectionToScreen(aabb1.getMax().toVector3DinZisZero(), 100, 100, 480, 20);
            g2.setColor(Color.red);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v1.x, (int) v2.y);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v1.x, (int) v2.y);

        }

        if (viewBoundingBox.collisionAABB(aabb02)){
            AABB aabb1 = viewBoundingBox.intersection(aabb02);
            Vector2D v1 = camera.projectionToScreen(aabb1.getMin().toVector3DinZisZero(), 100, 100, 480, 20);
            Vector2D v2 = camera.projectionToScreen(aabb1.getMax().toVector3DinZisZero(), 100, 100, 480, 20);
            g2.setColor(Color.green);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v1.x, (int) v2.y);
            g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v2.x, (int) v1.y);
            g2.drawLine((int) v2.x, (int) v2.y, (int) v1.x, (int) v2.y);

        }

        if (viewBoundingBox.containsPoint(p)) {
            Vector2D vector2D = camera.projectionToScreen(p.toVector3DinZisZero(), 100, 100, 480, 20);
            g2.fillOval((int) (vector2D.x - 5), (int) (vector2D.y - 5),10,10);
        }


        Bullet bullet = tank.getBullet();
        if (bullet!=null) {
            if (viewBoundingBox.containsPoint(bullet.getPosition())) {
                Vector2D vector2D = camera.projectionToScreen(bullet.getPosition().toVector3DinZisZero(), 100, 100, 480, 20);
                g2.fillOval((int) (vector2D.x - 5), (int) (vector2D.y - 5), 10, 10);
            }
        }


        Vector2D[] samplePoints = linear.getSample_points();
        for (int i = 0; i< samplePoints.length - 1;i++) {
            if (viewBoundingBox.collisionLineSegment(samplePoints[i],samplePoints[i + 1]))
            {
                Vector2D v1 = camera.projectionToScreen(samplePoints[i].toVector3DinZisZero(), 100, 100, 480, 20);
                System.out.println("v1 = " + v1);
                Vector2D v2 = camera.projectionToScreen(samplePoints[i + 1].toVector3DinZisZero(), 100, 100, 480, 20);
                g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
            }
        }
//
//        if (viewBoundingBox.containsPoint(tank.getCenter())) {
//            Vector2D[] outlines = tank.getBase().getOutlines();
//            for (Vector2D v : outlines) {
//                Vector2D v3 = camera.projectionToScreen(v.toVector3DinZisZero(), 100, 100, 100, 20);
//                g2.drawOval((int) v3.x, (int) v3.y,2,2);
//            }
//        }
//        tank.draw(g2,this   );
//        Utils.drawText(g2,500,110,0,new TextLayout("范围[%.2f,%.2f]".formatted(orthographicCamera.getL(),orthographicCamera.getR()),g2.getFont(),g2.getFontRenderContext()));
        Utils.drawText(g2,500,150,0,new TextLayout("摄像机视图A",g2.getFont(),g2.getFontRenderContext()));


        drawAAbb(g2,aabb,false);
        drawAAbb(g2,aabb02,false);
        tank.draw(g2,this);
        g2.dispose();
    }

    @Override
    public void timingEvent(double fraction) {
        float f = (float) fraction * time_to_anim;
        p = linear.Evaluate(f);
    }

    public void createTank() {
        tank = new SimpleTank();
        Base base = new Base();
        base.setOutlines(
                new Vector2D[]{
                        new Vector2D(-1.5,-1.25),new Vector2D(1.5,-1.25),new Vector2D(1.5,1.25),new Vector2D(-1.5,1.25)
                }
        );

        base.setAnglerSpeed(Math.PI);
        base.setPaint(Color.green);

        tank.setBase(base);

        Wheel wheel = new Wheel(10,0.5);
        wheel.setAnglerSpeed(Math.PI);
        wheel.setShowPattern(true);

        PaoTai paoTai = new PaoTai(12,1);
        paoTai.setAnglerSpeed(Math.PI);


        Gun gun = new Gun();
        gun.setOutlines(new Vector2D[] {
                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
        });

        tank.setWheel(wheel);
        tank.setPaoTai(paoTai);
        tank.setGun(gun);
    }
}
