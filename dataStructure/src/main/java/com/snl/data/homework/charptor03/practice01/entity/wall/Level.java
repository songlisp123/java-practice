package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;

public class Level extends Wall {

    public Level(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT, GameConstants.Grass);
    }

}
