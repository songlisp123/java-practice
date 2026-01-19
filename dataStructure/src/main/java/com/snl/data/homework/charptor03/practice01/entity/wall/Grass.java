package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grass extends Wall {

    public Grass(double xPos, double yPos, int WEIGHT, int HEIGHT, Color color) {
        super(xPos, yPos, WEIGHT, HEIGHT, color);
    }

    @Override
    public BufferedImage getTexture() {
        int szie = 20;
        BufferedImage bi = new BufferedImage(szie,szie,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        //青草
        g2.setColor(GameConstants.Grass);
        g2.fillRect(0,0,szie,szie / 3);
        g2.setColor(GameConstants.tuRang);
        g2.fillRect(0,szie / 3,szie,szie - szie / 3);
        g2.dispose();
        return bi;
    }
}
