package com.snl.swing.game.curve;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game2026.dataStructure.Array;

import java.awt.*;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

public class TestHermite extends DiKaErPlus implements TimingTarget {

    HermiteCurve hc;
    Vector2D p;
    Animator animator;
    float animate_time = 10000f;
    List<Vector2D> path;

    @Override
    protected void gameInitial() {
        super.gameInitial();

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

        path = new ArrayList<>();
        path.add(p);

        animator = new Animator((long) (t[t.length - 1] + 0.5f),this);
        animator.start();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        drawPolyLine(g2,hc.getmPositions(),true);
        drawCircle(g2,p,0.1,false);
        drawCircle(g2,p,0.05,true);

        if (path.size() > 2)
            drawPolyLine(g2, path.toArray(Vector2D[]::new),false);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestHermite());
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
    }
}
