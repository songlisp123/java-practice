package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;

/**
 * 树干
 */
public class TreeStem extends Wall {

    public TreeStem(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT, GameConstants.tuRang02);
    }

    @Override
    public void update(double delta, InputState state) {
        setColor(InputState.changeIngColor?GameConstants.tuRang02:GameConstants.tuRang);
        super.getTexture();
        super.initPaint();
    }
}
