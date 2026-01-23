package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class Level extends Wall {

    public Level(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT, GameConstants.Grass);
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
}
