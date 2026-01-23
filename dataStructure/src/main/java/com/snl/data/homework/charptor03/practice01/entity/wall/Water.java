package com.snl.data.homework.charptor03.practice01.entity.wall;


import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;


public class Water extends Wall {

    public Water(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos + HEIGHT / 2.0, WEIGHT, HEIGHT, GameConstants.Water);
    }

    @Override
    public void update(double delta, InputState state) {
        setColor(InputState.changeIngColor?GameConstants.Water:GameConstants.Water02);
        super.getTexture();
        super.initPaint();
    }
}
