package com.snl.swing.geom;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game2026.dataStructure.Array;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Problem1_10 extends DiKaErPlus  implements TimingTarget {

    Vector2D posA,posB,N,move,pA;
    Animator animator;
    List<Vector2D> vector2DS;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        posA = new Vector2D();
        posB = new Vector2D(1.25,3);
        showAnti = true;
        move = Vector2D.polar(0,1);
        N = move.prep().norm();

        vector2DS = new ArrayList<>();
        animator = new Animator(5000,this);
        animator.start();

        showAnti = true;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            this.reset();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P)) {
            animator.stop();
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.CYAN);
        g2.drawString("已知两点A和B，求从点A向所有经过点B的直线所作垂线的垂足的集合",30,130);
        drawCircle(g2,posA,0.055,true);
        drawCircle(g2,posB,0.055,true);

        drawLine(g2,posB,posB.add(move));

        Vector2D ab = posB.sub(posA);
        Vector2D aa = N.scale(ab.dot(N));
        double d = ab.dot(N);
        pA = posA.add(N.mul(2 * d));

        vector2DS.add(pA);
        if (vector2DS.size() > 2) {
            //
            Vector2D first = vector2DS.getFirst();
            for (Vector2D v : vector2DS) {
                if (v == first)
                    continue;
                drawLine(g2,first,v);
                first = v;
            }
        }

        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new Problem1_10());
    }

    @Override
    protected void reset() {
        super.reset();
        animator.stop();
        animator.start();
        synchronized (this) {
            vector2DS.clear();
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
        move = Vector2D.polar(Math.PI * fraction,1);
        N = move.prep().norm();
    }
}
