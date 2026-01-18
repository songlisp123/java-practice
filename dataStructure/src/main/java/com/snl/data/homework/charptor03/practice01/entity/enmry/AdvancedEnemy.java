package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.util.random.RandomGenerator;

public class AdvancedEnemy extends Enemy {
    //TODO 暂时不完成
    //第五章boss

    private final double MAX_Y_SPEED = 5.0;
    private final double MIN_Y_SPEED = 1.0;
    private final double MIN_X_SPEED = 1.0;
    private final double MAX_X_SPEED = 5.0;
    private final RandomGenerator generator =
            RandomGenerator.getDefault();

    public AdvancedEnemy(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        setLifePoints(1000);
        setxSpeed(5);
    }
}
