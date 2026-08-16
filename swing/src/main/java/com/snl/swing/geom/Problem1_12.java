package com.snl.swing.geom;

import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class Problem1_12 extends ProblemSolver {

    Line l1;
    Circle c;
    double offset;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        l1 = new Line(new Vector2D(),new Vector2D(1,3),Line.XIANSHI);
        c = new Circle(1,new Vector2D(3,4));
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    void drawContent(Graphics2D g2) {
        drawLine(g2,l1);
        drawCircle(g2,c.center,c.r,false);
    }

    public static void main(String[] args) {
        launchGame(new Problem1_12());
    }
}
