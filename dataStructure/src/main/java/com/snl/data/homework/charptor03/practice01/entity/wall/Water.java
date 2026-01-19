package com.snl.data.homework.charptor03.practice01.entity.wall;

import java.awt.*;

public class Water extends Wall {

    public Water(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos + HEIGHT / 2.0, WEIGHT, HEIGHT, Color.cyan);
    }
}
