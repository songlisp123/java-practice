package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.animator.interpolator.DiscreteInterpolator;
import com.snl.swing.animator.interpolator.LinearInterpolator;
import com.snl.swing.animator.interpolator.SineInterpolator;
import com.snl.swing.animator.interpolator.SplineInterpolator;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestPlayerDemo extends DiKaErPlus implements TimingTarget {

    AABB aabb,copy,aabb01,aabb02;
    Vector2D v;
    Animator animator;
    double min,max;

    Circle circle;
    Vector2D v0,v0Copy;


    public static void main(String[] args) {
        launchGame(new TestPlayerDemo());
        //玩家
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        aabb = new AABB(new Vector2D(-1,-1),new Vector2D(1,1));
        v = new Vector2D(1,1);
        animator = new Animator(3600,this);
        animator.setRepeatCount(-1);
        animator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
//        animator.setInterpolator(DiscreteInterpolator.getInstance());//离散插值
//        animator.setInterpolator(LinearInterpolator.getInstance());//线性插值
//        animator.setInterpolator(new SplineInterpolator(0,0,1,1));//线性插值
//        animator.setInterpolator(new SplineInterpolator(0,1,0,1));//缓入插值
//        animator.setInterpolator(new SineInterpolator());//sin函数插值
        animator.setInterpolator(new SplineInterpolator(0.00f, 1.00f, 1.00f, 1.00f));//sin函数插值
        animator.setStartFraction(0.5);
        animator.start();

        min = -5;
        max = 5;

        circle = new Circle(0.56,new Vector2D());

        circle.translate(0.5,-0.15);

        v0 = new Vector2D(0,0.56);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_D))
        {
            double dx = v.x * delta;
            aabb.translate(dx,0);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_A))
        {
            double dx = - v.x * delta;
            aabb.translate(dx,0);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_W))
        {
            double dy = v.y * delta;
            aabb.translate(0,dy);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_S)){
            double dy = - v.y * delta;
            aabb.translate(0,dy);
        }

        if (animator.isRunning() && keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            animator.pause();
        } else if (!animator.isRunning() && keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            animator.resume();
        }


    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);

    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Color.CYAN);
        drawAAbb(g2,aabb,false);

        drawAAbb(g2,aabb01,true);
        drawAAbb(g2,aabb02,true);

        g2.setPaint(Color.GREEN);
        drawAAbb(g2,copy,true);


        drawCircle(g2,circle,false);

        g2.setPaint(Color.MAGENTA);
        drawCircle(g2,v0,0.1,true);

        drawLine(g2,v0,circle.getCenter());
        g2.dispose();
    }

    @Override
    public void begin() {
        System.out.println("开始播放动画");
    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {
        System.out.println("重复播放");
    }

    @Override
    public void timingEvent(double fraction) {
        //插值后的时间
        System.out.println("插值后："+fraction);
        double maxV = max - min;
        double dx = min + maxV * fraction;
        copy = aabb.getTranslated(dx,dx);
        aabb01 = aabb.getTranslated(0,dx);
        aabb02 = aabb.getTranslated(dx,0);

        //旋转
        double rot = 2 * Math.PI * fraction;
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        rotate = rotate.mul(Matrix3x3f.translate(circle.getCenter()));
//        rotate = rotate.mul(Matrix3x3f.translate(0.56,0));
        v0 = rotate.mul(new Vector2D()).add(circle.getCenter());
    }
}
