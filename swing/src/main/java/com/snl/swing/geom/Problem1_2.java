package com.snl.swing.geom;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class Problem1_2 extends ProblemSolver {

    AABB aabb01,aabb02;
    Polygon p;

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        sb.append("这是怎么回事？？");
        super.openTextPanel();

        aabb01 = new AABB(
                new Vector2D(),new Vector2D(4,4)
        );

        aabb02 = new AABB(
                new Vector2D(1,-2),new Vector2D(3,6)
        );
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    void drawContent(Graphics2D g2) {
        drawPolyGon(g2,p,false);
        drawAAbb(g2,aabb02,false);
    }

    public static void main(String[] args) {
        launchGame(new Problem1_2());
    }

    @Override
    public void timingEvent(double fraction) {
        p =  aabb01.getRotateInstance(Math.PI * fraction,aabb01.getCenter());
    }
}
