package com.snl.data.homework.charptor03.practice01.entity.wall;


import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;


public class Water extends Wall {

    public Water(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos + HEIGHT / 2.0, WEIGHT, HEIGHT, GameConstants.Water);
    }

}
