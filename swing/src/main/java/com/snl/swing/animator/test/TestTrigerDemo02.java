package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.random.RandomGenerator;

public class TestTrigerDemo02 extends DiKaErPlus implements TimingTarget {


    Star[] stars,copy,index;

    Animator animator;



    RandomGenerator generator = RandomGenerator.getDefault();

    @Override
    protected void gameInitial() {
        super.gameInitial();

        stars = new Star[1000];
        copy = new Star[1000];
        for (int i = 0;i<1000;i++) {
//            stars[i] = new Star(
//                    new Vector2D(generator.nextDouble(-wordWidth / 2.0,wordWidth / 2.0),
//                            generator.nextDouble(-wordHeight / 2.0,wordHeight / 2.0))
//                    ,0.03,Color.lightGray
//            );
            double index;
            if (i < 250) {
                index = 1;
            } else if (i < 500) {
                index = 3;
            } else if (i < 750) {
                index = 5;
            } else {
                index = 7;
            }
            stars[i] = new Star(
                    Vector2D.polar(generator.nextDouble(0,Math.PI * 2),generator.nextDouble(index,index + 1)),0.03,Color.lightGray
            );
        }

        animator = new Animator(3600,this);
        animator.setIntRepeatCount(true);
        animator.setRepeatBehavior(Animator.RepeatBehavior.LOOP);
        animator.setRepeatCount(Animator.INFINITE);
        animator.start();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        for (Star s : index)
        {
            g2.setColor(s.color);
            drawCircle(g2,s.c,true);
        }

        g2.setColor(Color.ORANGE);
        drawCircle(g2,new Vector2D(),0.25,true);
        g2.dispose();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            //空格键
            if (!animator.isRunning()) {
                animator.start();
            }
        }
    }

    public static void main(String[] args) {
        launchGame(new TestTrigerDemo02());
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
        for (int i = 0;i<1000;i++) {
            Vector2D v;
            Star star = stars[i];
            Vector2D center = star.c.getCenter();
            Vector2D inv = center.inv();
//            v = center.add(inv.mul(fraction));
            Matrix3x3f mat = Matrix3x3f.rotate(2 * Math.PI * fraction);
            mat = mat.mul(Matrix3x3f.shear(fraction,0))
                    .mul(Matrix3x3f.scale(fraction,fraction));
//            v = (i % 2 == 0) ? mat.mul(center) : center;
            v = mat.mul(center);
            copy[i] = new Star(
                    v,star.c.r,star.color
            );
        }

        if (index == null || index == stars)
            index = copy;
    }

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (!animator.isRunning())
            index = stars;
    }

    class Star {
        Circle c;
        Color color;

        public Star(Vector2D center,double r , Color color) {
            this.c = new Circle(r,center);
            this.color = color;
        }


    }
}
