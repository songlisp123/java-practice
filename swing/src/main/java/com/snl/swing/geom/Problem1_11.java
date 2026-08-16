package com.snl.swing.geom;

import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Problem1_11 extends ProblemSolver {

    private Vector2D move;
    Line l1,l2;
    List<Vector2D> vs;
    int index,ci;
    List<Circle> circles;

    boolean showCircle;

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        move = Vector2D.polar(0,1);
        vs = new ArrayList<>();
        circles = new ArrayList<>();
    }

    @Override
    void forwardUp() {
        range();
        if (ci < index) {
            move = vs.get(ci);
            ci++;
        }
    }

    private void range() {
        if (ci > index) {
            ci = index;
        }
        if (ci < 0) {
            ci = 0;
        }
    }

    @Override
    void backUp() {
        range();
        if (ci > 0) {
            move = vs.get(ci-1);
            --ci;
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_F)) {
            showCircle = !showCircle;
        }
    }

    public static void main(String[] args) {
        launchGame(new Problem1_11());
    }

    @Override
    void drawContent(Graphics2D g2) {

        l1 = new Line(
                new Vector2D(),move,Line.XIANSHI
        );

        l2 = new Line(
                new Vector2D(3,5),move,Line.XIANSHI
        );
        drawLine(g2,l1);
        drawLine(g2,l2);
        Vector2D norm = move.prep().norm();
        Vector2D ab = l2.getPos().sub(l1.getPos());
        Vector2D aa = norm.mul(ab.dot(norm));
        double d = aa.len();

        double r = d / 2;
        Vector2D center = l1.getPos().add(norm.mul(r));

        drawCircle(g2,center,r,false);

        Circle c;

        if (animator.isRunning())
        {
            //如果动画存在
            c = new Circle(r,center);
            circles.add(c);
        }

        if (showCircle) {
            for (Circle circle : circles) {
                drawCircle(g2, circle, false);
            }

            Circle circle = circles.get(ci);
            g2.setColor(Color.red);
            drawCircle(g2,circle,false);
        }
    }

    @Override
    protected void reset() {
        super.reset();
        synchronized (this) {
            vs.clear();
            circles.clear();
        }
        ci=index=0;
    }

    @Override
    public void timingEvent(double fraction) {
        index++;
        ci = index;
        double rad = 2 * Math.PI * fraction;
        move = Vector2D.polar(rad,1);
        vs.add(move);
    }
}
