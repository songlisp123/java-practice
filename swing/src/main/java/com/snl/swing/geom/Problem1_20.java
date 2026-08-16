package com.snl.swing.geom;

import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.SegMent;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class Problem1_20 extends ProblemSolver  {

    Circle circle;
    Vector2D p,N,center;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        circle = new Circle(
                1.5,new Vector2D()
        );
        p = new Vector2D(3,5);
        showAnti = true;
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    void drawContent(Graphics2D g2) {
        drawCircle(g2,circle,false);
        drawCircle(g2,p,0.05,true);
        if (N != null) {
            drawCircle(g2, N, 0.05, true);
            drawLine(g2,N,p);
            center = N.add(p).div(2);
            drawCircle(g2,center,0.05,true);
        }
    }

    @Override
    protected void drawContext(Graphics2D g2) {
        g2.drawString("一个圆和一个点 L 给定于平面内。求线段 LN 中点的轨迹，其中 N 是圆上的任意一点。",20,130);
    }

    @Override
    public void timingEvent(double fraction) {
        double f = Math.PI * 2 * fraction;
        N = Vector2D.polar(f,circle.r);
    }

    public static void main(String[] args) {
        launchGame(new Problem1_20());
    }
}
