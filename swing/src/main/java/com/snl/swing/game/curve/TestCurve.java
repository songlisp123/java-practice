package com.snl.swing.game.curve;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.curve.Bezier.Bezier;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Epsilon;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class TestCurve extends DiKaErPlus implements TimingTarget {

    float t;
    Vector2D c;
    Animator animator,animator02;

    Vector2D[] points,b01, bezier02,bezier03,bezier04;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        t = 0.0F;
        c = Hermite.create(Vector2D.originPoint,new Vector2D(3,1),new Vector2D(0,1),new Vector2D(1,0),t);

        animator = new Animator(3000L,this);
        animator.setRepeatCount(1);
        animator.start();

        animator02 = new Animator(3000L,this);
        animator02.setRepeatCount(1);

        int length = 50;
        points = new Vector2D[length];
        b01 = new Vector2D[length];
        bezier02 = new Vector2D[length];
        bezier03 = new Vector2D[length];
        bezier04 = new Vector2D[length];

        int j = 0;
        float min = 0.f,max = 1.0F;
        float i = min;
        while (i < max) {
            float beziered = Bezier.bezier_base(0, i,min,max);
            Vector2D temp = new Vector2D();
            temp.x = i;
            temp.y = beziered;
            if (j == length)
                j--;
            points[j] = temp;


            Vector2D b02 = new Vector2D();
           beziered =  Bezier.bezier_base(1,i,min,max);
           b02.x = i;
            b02.y = beziered;
            b01[j] = b02;

            Vector2D b03 = new Vector2D();
            beziered =  Bezier.bezier_base(2,i,min,max);
            b03.x = i;
            b03.y = beziered;
            bezier02[j] = b03;

            Vector2D b04 = new Vector2D();
            beziered =  Bezier.bezier_base(3,i,min,max);
            b04.x = i;
            b04.y = beziered;
            bezier03[j] = b04;

            Vector2D b05 = new Vector2D();
            beziered =  Bezier.bezier_base(4,i,min,max);
            b05.x = i;
            b05.y = beziered;

            bezier04[j++] = b05;
            i +=  (max - min) / length ;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (animator.isRunning())
            c = Hermite.create(Vector2D.originPoint,new Vector2D(1,1),new Vector2D(1,0),new Vector2D(1,0),t);
        else
            if (!animator02.isRunning())
                animator02.start();

        if (animator02.isRunning())
            c = Hermite.create(new Vector2D(1,1),new Vector2D(-3,-2),new Vector2D(1,0),new Vector2D(-2,-1),t);
        if (!animator.isRunning())
            animator.start();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(Color.green);
        drawCircle(g2,c,0.1,true);
        drawPolyLine(g2,points);
        drawPolyLine(g2,b01);
        drawPolyLine(g2,bezier02);
        drawPolyLine(g2,bezier03);
        drawPolyLine(g2,bezier04);
        g2.dispose();

    }

    public static void main(String[] args) {
        launchGame(new TestCurve());
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
        t = (float) fraction;
    }
}
