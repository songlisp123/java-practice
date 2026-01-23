package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

/**
 * 树干
 */
public class TreeStem extends Wall {

    public TreeStem(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT, GameConstants.tuRang02);
    }

    @Override
    public void update(double delta, InputState state) {
        Color temp =  getColor();
        Color color = InputState.changeIngColor ? GameConstants.tuRang02 : GameConstants.tuRang;
        if (temp == color)
        {
            return;
        }
        setColor(color);
        super.getTexture();
        super.initPaint();
    }
}
