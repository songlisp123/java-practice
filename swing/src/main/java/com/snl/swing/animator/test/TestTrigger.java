package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.animator.interpolator.LinearInterpolator;
import com.snl.swing.animator.interpolator.SineInterpolator;
import com.snl.swing.animator.interpolator.SplineInterpolator;
import com.snl.swing.animator.keyframe.KeyFrames;
import com.snl.swing.animator.keyframe.KeyValues;
import com.snl.swing.animator.keyframe.PropertySetter;
import com.snl.swing.animator.trigger.ActionTrigger;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TestTrigger extends DiKaErPlus implements TimingTarget {

    AABB aabb;
    ActionTrigger actionTrigger;
    Animator animator,a2,a3;
    Color c;
    Circle circle;
    boolean drawCircle;
    double fractor;

    List<ActionListener> ac = new ArrayList<>();

    @Override
    protected void gameInitial() {
        super.gameInitial();
        showAnti = true;
        aabb = new AABB(
                new Vector2D(-1,-1),new Vector2D(1,1)
        );
        drawCircle = true;
        circle = new Circle(0.25,new Vector2D());
        fractor = 0.5;

        KeyValues<Color> values = KeyValues.create(Color.MAGENTA,Color.pink,Color.WHITE);
        KeyFrames kf  = new KeyFrames(values);
        animator = PropertySetter.createAnimator(2000,this,"c",kf);
        ActionTrigger at = ActionTrigger.addTrigger(this, animator);

//
//        KeyValues<Vector2D> v2 = KeyValues.create(new Vector2D(),new Vector2D(6,6));
//        KeyFrames kf2 = new KeyFrames(v2);
//        a2 = PropertySetter.createAnimator(5000,circle,"center",kf2);
//        ActionTrigger.addTrigger(this,a2);


        KeyValues<Vector2D> v3 = KeyValues.create(new Vector2D(),new Vector2D(6,6));
        KeyFrames kf3 = new KeyFrames(v3,LinearInterpolator.getInstance());
        a2 = PropertySetter.createAnimator(5000,circle,"center",kf3);
        ActionTrigger.addTrigger(this,a2);

//        KeyValues<Double> v4 = KeyValues.create(0.2,0.3,0.5,0.6,1.1,1.3,1.5,1.6);
//        kf = new KeyFrames(v4);
//       a3 =  PropertySetter.createAnimator(2500,circle,"r",kf);
//        ActionTrigger.addTrigger(this,a3);

        a3 = new Animator(2500,this);
        a3.setInterpolator(new SineInterpolator());
    }


    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(c);
        drawAAbb(g2,aabb,true);
        drawCircle(g2,circle,drawCircle);
        drawCircle(g2,circle.getScaled(fractor),drawCircle);
        drawCircle(g2,circle.getScaled(Math.pow(fractor,2)),drawCircle);
        g2.dispose();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            fireEvent();
    }

    public static void main(String[] args) {
        launchGame(new TestTrigger());
    }


    @Override
    protected void animation(double delta) {
        if (circle.getCenter().x > 4.5 ) {
            //触发另一个事件，领一个动画
            if (a2.isRunning())
                a2.stop();
            if (!a3.isRunning())
                a3.start();
            drawCircle = false;
        }

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
        System.out.println("fraction = " + fraction);
        circle.r = 0.1 + 1.4 * fraction;
        fractor = 0.1 + 0.9 * fraction;
    }

    public void addActionListener( ActionTrigger at) {
        synchronized (this) {
            if (!ac.contains(at))
                ac.add(at);
        }
    }

    public void fireEvent() {
        synchronized (this) {
            for (ActionListener l : ac)
                l.actionPerformed(null);
        }
    }


    public void setC(Color c) {
        this.c = c;
    }
}
