package com.snl.swing.game2026.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import java.awt.*;

public abstract class TestFrame extends DiKaErPlus {

    @Override
    protected void gameInitial() {
        super.gameInitial();
        drawAxis = false;
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.CYAN);
        drawContent(g2);
        g2.dispose();
    }

     public abstract void drawContent(Graphics2D g2);

}
