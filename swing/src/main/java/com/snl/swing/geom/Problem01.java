package com.snl.swing.geom;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Problem01 extends DiKaErPlus  implements TimingTarget {

    Vector2D posA,posB,nr;
    Line line;
    Animator animator;
    Vector2D v;
    List<Vector2D> vector2DS;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        posA = new Vector2D();
        posB = new Vector2D(1,-1);
        v = new Vector2D(1,1);
        animator = new Animator(5000,this);
        animator.setIntRepeatCount(false);
        animator.setRepeatCount(1);
        animator.start();
        vector2DS = new ArrayList<>();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P))
            animator.pause();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R))
            animator.resume();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            animator.stop();
            animator.start();
            vector2DS.clear();
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.CYAN);
        g2.drawString("已知两点A和B，求从点A向所有经过点B的直线所作垂线的垂足的集合",30,130);
        line = new Line(posB,v,Line.YINGSHI);
        drawLine(g2,posB,posB.add(v));
        nr = line.nearestPoint(posA);
        if (!vector2DS.contains(nr)) {
            vector2DS.add(nr);
        }
        drawLine(g2,posA,nr);
        if (vector2DS.size() > 2) {
            Vector2D f = vector2DS.getFirst();
            for (Vector2D n : vector2DS) {
                if  (n == f)
                    continue;
                drawLine(g2,f,n);
                f = n;
            }
        }
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new Problem01());
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
        v = Vector2D.polar(2 * Math.PI * fraction,1);
    }
}
