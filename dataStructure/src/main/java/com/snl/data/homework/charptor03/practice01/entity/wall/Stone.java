package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Stone extends Wall {

    public Stone(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
    }

    @Override
    public BufferedImage getTexture() {
        int size = 20;
        BufferedImage bi = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(GameConstants.Stone);
        g2.fillRect(0,0,size,size / 3);
        g2.setColor(GameConstants.tuRang);
        g2.fillRect(0,size / 3,size,size - size / 3);
        g2.dispose();
        return bi;
    }
}
