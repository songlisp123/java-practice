package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class Enemy extends Sprite {

    private double SPEED = 4.0;

    public Enemy() {
    }

    public Enemy(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
    }

    @Override
    public void update(double delta, InputState state) {
        //空实现
        move(0,SPEED);
        handleTouchWall(GameConstants.Weight,GameConstants.Height);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.red);
        g2.fillRect((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
    }

    @Override
    public void move(double xPos, double yPos) {
        //TODO
        setxPos(getxPos() + xPos);
        setyPos(getyPos() + yPos);
    }

    @Override
    public void handleTouchWall(int weight, int height) {
        //TODO
        if (touchUpBounder())
        {
            //到达上边界
            SPEED = -SPEED;
        }
        if (touchBottomBounder(height)) {
            SPEED = -SPEED;
        }

    }

    private boolean touchUpBounder() {
        return getyPos() <= 0;
    }

    private boolean touchBottomBounder(int height) {
        return (getyPos()+getHEIGHT()) >= height;
    }

    public double getSPEED() {
        return SPEED;
    }

    public void setSPEED(double SPEED) {
        this.SPEED = SPEED;
    }

}
