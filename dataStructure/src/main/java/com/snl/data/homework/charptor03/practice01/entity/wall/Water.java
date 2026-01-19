package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Water extends Wall {

    public Water(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos + HEIGHT / 2.0, WEIGHT, HEIGHT, Color.cyan);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D) g.create();
        BufferedImage bi = this.getTexture();
        Rectangle2D r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        TexturePaint paint = new TexturePaint(bi,r);
        g2.setPaint(paint);
        g2.fill(getShape());
        g2.dispose();
    }

    private BufferedImage getTexture() {
        int szie = 20;
        BufferedImage bi = new BufferedImage(szie,szie,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(getColor());
        g2.fillRect(0,0,bi.getWidth(), bi.getHeight());
        g2.setColor(Color.WHITE);
        g2.fillOval(szie / 2 - 5,szie / 2 - 5,10,10);
        g2.dispose();
        return bi;
    }
}
