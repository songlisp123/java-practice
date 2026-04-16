package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.geo.curve.Arc;

import java.awt.*;

public class TestArc extends DiKaErPlus {

    boolean drag,cMoving,clicked,collision;
    Arc arc;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        arc = new Arc(30,40,100,100,30,60,Arc.PIE);
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.yellow);
        g2.drawArc((int) arc.getX(), (int) arc.getY(),
                (int) arc.getW(), (int) arc.getH(), (int) arc.getStartAngle(), (int) arc.getExtent());
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestArc());
 }
