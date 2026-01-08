package com.snl.data.homework.charptor03.practice01.entity.goods;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class Coin extends Sprite {

    private final double Speed = 2.5;
    private double xOrigin;
    private double yOrigin;

    public Coin() {

    }

    public Coin(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.xOrigin = xPos;
        this.yOrigin = yPos;
    }

    @Override
    public void update(double delta, InputState state) {

    }

    @Override
    public void paint(Graphics g, InputState state) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.yellow);
        g2.fillOval((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
    }

    @Override
    public void move(double xPos, double yPos) {
        //需要用到波函数
    }

    @Override
    public void handleTouchWall(int weight, int height) {
        //不用实现
    }

}
