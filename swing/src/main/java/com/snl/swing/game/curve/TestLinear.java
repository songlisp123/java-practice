package com.snl.swing.game.curve;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class TestLinear extends DiKaErPlus implements TimingTarget {

    Linear linear;
    Animator animator;
    Vector2D p;
    float timeToAnime = 100000.f;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        linear = new Linear();

        float[] times = {0.0f,10000.0f,30000.f,timeToAnime};
        linear.Initialize(new Vector2D[]{
                Vector2D.originPoint,new Vector2D(1,1),new Vector2D(3,5),new Vector2D(5,8)
        },times,4);

        p = Vector2D.originPoint;

        animator = new Animator( (long)( times[times.length -1] + 0.5f),this);
        animator.start();
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
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestLinear());
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
       float f = (float) (fraction * timeToAnime);
        p = linear.Evaluate(f);
    }
}
