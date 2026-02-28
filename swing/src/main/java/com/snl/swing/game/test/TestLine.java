package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Line;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class TestLine extends DiKaErPlus {

    Line line;
    Line l2;
    boolean insert;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        line = new Line(
                new Vector2D(0,0),new Vector2D(1,1),Line.XIANSHI
        );
        l2 = new Line(
                new Vector2D(-1,5),new Vector2D(1,2),Line.XIANSHI
        );
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        insert = line.collision(l2);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.WHITE);
        drawLine(g2,line);
        drawLine(g2,l2);
        Vector2D c = line.collisionPoint(l2);
        if (c != null)
            drawCircle(g2,c,.1,true);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestLine());
    }
}
