package com.snl.swing.tank;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TestBase extends DiKaErPlus implements PropertyChangeListener , TimingTarget {

    Base base;
    Vector2D center;
    boolean coMoving;
    boolean clicked,dragging;


    Wheel wheel;
    PaoTai pt;
    Gun gun;
    boolean firing;
    Bullet bullet;
    Animator fireingAnimator,collisionAnimator;
    AABB aabb;



    @Override
    protected void gameInitial() {
        super.gameInitial();
        base = new Base();
        base.setOutlines(
                new Vector2D[]{
                        new Vector2D(-1.5,-1.25),new Vector2D(1.5,-1.25),new Vector2D(1.5,1.25),new Vector2D(-1.5,1.25)
                }
        );

        base.setAnglerSpeed(Math.PI);
        base.setPaint(Color.green);

        center = new Vector2D();



        wheel = new Wheel(10,0.5);
        wheel.setAnglerSpeed(Math.PI);
        wheel.setShowPattern(true);
//        wheel.setListener(this);

        pt = new PaoTai(12,1);
        pt.setAnglerSpeed(Math.PI);


        gun = new Gun();
        gun.setOutlines(new Vector2D[] {
                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
        });
//        gun.setOutlines(new Vector2D[]{
//
//                new Vector2D(-0.45,-1),
//                new Vector2D( 0.45,-1),
//
//                new Vector2D( 0.28,-0.7),
//                new Vector2D( 0.18,0.5),
//
//                new Vector2D(0.25,0.8),
//                new Vector2D(0.5,0.9),
//
//                new Vector2D(0.5,1.15),
//                new Vector2D(-0.5,1.15),
//
//                new Vector2D(-0.5,0.9),
//                new Vector2D(-0.25,0.8),
//
//                new Vector2D(-0.18,0.5),
//                new Vector2D(-0.28,-0.7)
//
//        });


        aabb = new AABB(
                new Vector2D(-1,3),new Vector2D(1,6)
        );
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        boolean kewDown_A = keyBoardEvent.keyDown(KeyEvent.VK_A);
        boolean kewDown_W = keyBoardEvent.keyDown(KeyEvent.VK_W);
        boolean kewDown_D = keyBoardEvent.keyDown(KeyEvent.VK_D);
        boolean kewDown_S = keyBoardEvent.keyDown(KeyEvent.VK_S);
        boolean kewDown_U = keyBoardEvent.keyDown(KeyEvent.VK_U);
        boolean kewDown_I = keyBoardEvent.keyDown(KeyEvent.VK_I);
        boolean keyDown_Space = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
        boolean keyDown_UP = keyBoardEvent.keyDown(KeyEvent.VK_UP);
        boolean keyDown_DOWN = keyBoardEvent.keyDown(KeyEvent.VK_DOWN);

        Vector2D mouse = getMousePointInVector();

        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);


        if (kewDown_W) {
//            base.rotateClockWise(delta);
            base.rotateLocalClockWise(delta);
        }
        if (kewDown_S)
//            base.rotateClock(delta);

            base.rotateLocalOnClock(delta);
        if (kewDown_A) {
            base.backward(delta, 1.5);
            wheel.rotateLocalClockWise(delta);
        }
        if (kewDown_D) {
            base.forward(delta, 3);
            wheel.rotateLocalOnClock(delta);
        }
        if (kewDown_U)
            base.scale(delta);
        if (kewDown_I)
            base.deScale(delta);

        if (keyDown_UP)
            pt.rotateLocalClockWise(delta);
        if (keyDown_DOWN)
            pt.rotateLocalOnClock(delta);

        if (keyDown_Space)
            if (!firing)
                firing = true;

        firing = firing && keyDown_Space;
        if (firing) {
            createBullet(gun.modelToWorld());
            createrAnimator();
            showAnimator();
        }

    }

    private void showAnimator() {
        if (fireingAnimator.isRunning())
            fireingAnimator.stop();
        fireingAnimator.start();
    }

    private void createrAnimator() {
        if (fireingAnimator == null) {
            fireingAnimator = new Animator(1000L,this);
            fireingAnimator.setRepeatCount(1);
            fireingAnimator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
            fireingAnimator.setEndBehavior(Animator.EndBehavior.HOLD);
        }
    }

    private void createBullet(Matrix3x3f parentTransform) {
        //炮口世界坐标
        Vector2D position =
                parentTransform.mul(new Vector2D(0,1));
        //炮管方向
        Vector2D end =
                parentTransform.mul(new Vector2D(0,1));

        Vector2D start =
                parentTransform.mul(new Vector2D(0,0));
        Vector2D direction =
                end.sub(start).norm();
        bullet = Bullet.createBullet(position,direction,3.4);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicked && pointInCircle(mouse,center,.1))
            coMoving = true;

        coMoving = coMoving && dragging;
        Matrix3x3f scale = getReverseScaleViewPortMat();
        Vector2D v = scale.mul(mouseDelta);
        if (coMoving)
        {
            center = center.add(v);
        }
        base.update(delta,null);
        wheel.update(delta,base);
        pt.update(delta,base);
        gun.update(delta,pt);

        if (bullet != null)
            bullet.update(delta);

        //碰撞检测
        if (bullet != null)
        {
            boolean collsion = aabb.collisionCircle(bullet.position, 0.1);

            if (collsion) {
                if (collisionAnimator == null)
                {
                    collisionAnimator = new Animator(3000L,new Ani());
                }
                collisionAnimator.stop();
                collisionAnimator.start();
            }
            else {
                if (collisionAnimator != null)
                    collisionAnimator.stop();

            }


        }
    }


    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);

        base.draw(g2,this);

        g2.setColor(Color.WHITE);
        drawCircle(g2,base.modelToWorld().mul(center),0.1,true);

        wheel.draw(g2,this);

        pt.draw(g2,this);

        gun.draw(g2,this);

        if (bullet != null)
            drawCircle(g2,bullet.position,0.1,true);

        drawAAbb(g2,aabb,true);


        g2.setColor(Color.green);
        if (collisionAnimator != null &&collisionAnimator.isRunning())

            drawCircle(g2,bullet.position,0.2,true);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestBase());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
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
        Vector2D e2 = gun.rotateForm.getColumn(1);
        Vector2D e1 = gun.rotateForm.getColumn(0);

        double dy =  -0.05 + 0.05 * fraction;
        double s = 0.95 + 0.05 * fraction;
        gun.transFrom = Matrix3x3f.translate(e2.mul(0,dy));
        gun.scaleForm = Matrix3x3f.scale(s,s);
    }


    class Ani implements TimingTarget {

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

        }
    }
}
