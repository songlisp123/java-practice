package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Grass extends Wall {

    public Grass(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT,GameConstants.Grass);
    }

    @Override
    public void update(double delta, InputState state) {
        Color temp =  getColor();
        Color color = InputState.changeIngColor ? GameConstants.Grass : GameConstants.Grass02;
        if (temp == color)
        {
            return;
        }
        setColor(color);
        super.getTexture();
        super.initPaint();
    }

    @Override
    public BufferedImage getTexture() {
        int szie = 20;
        BufferedImage bi = new BufferedImage(szie,szie,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        //青草
        g2.setColor(getColor());
        g2.fillRect(0,0,szie,szie / 3);
        g2.setColor(InputState.changeIngColor?GameConstants.tuRang02 : GameConstants.tuRang);
        g2.fillRect(0,szie / 3,szie,szie - szie / 3);
        g2.dispose();
        return bi;
    }
}
