package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
/**
 * 树干
 */
public class TreeStem extends Wall {

    public TreeStem(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT, GameConstants.tuRang);
    }
}
