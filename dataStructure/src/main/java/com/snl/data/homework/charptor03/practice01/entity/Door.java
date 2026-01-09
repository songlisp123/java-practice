package com.snl.data.homework.charptor03.practice01.entity;

import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class Door extends Sprite {

    public Door() {
    }

    public Door(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
    }

    @Override
    public void update(double delta, InputState state) {
        //无实现
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D)g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        float radius = 20;
        float[] dist = {0.0f,1.0f};
        Color[] colors = {
                Color.WHITE,Color.BLACK
        };
        RadialGradientPaint paint = new RadialGradientPaint(getPoint(), radius, dist, colors);
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,0.8F
        ));
        g2.setPaint(paint);
        g2.fillOval((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
        g2.dispose();
    }

}
