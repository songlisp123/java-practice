package com.snl.data.homework.charptor03.practice01.entity.goods;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.time.LocalDateTime;

public class Coin extends Sprite {

    private final double Speed = 2.5;
    private double xOrigin;
    private double yOrigin;
    private int width;
    private int height;
    private final  int renderTime = 1;
    private LocalDateTime startTime;

    public Coin() {

    }

    public Coin(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.xOrigin = xPos;
        this.yOrigin = yPos;
        this.width = WEIGHT;
        this.height = HEIGHT;
        startTime = LocalDateTime.now();
    }

    @Override
    public void update(double delta, InputState state) {
        double sin = Math.max(Math.sin(Math.random() * Math.PI),0.85);
        LocalDateTime now = LocalDateTime.now();
        if ((now.getSecond() - startTime.getSecond()) < renderTime) {
            //循环开始
            setHEIGHT((int) (sin * width));
            setWEIGHT((int) (sin * height));
        }else {
            //超过渲染时间
            startTime = now;
            setHEIGHT(height);
            setWEIGHT(width);
        }

    }

    @Override
    public void paint(Graphics g, InputState state) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.YELLOW);
        g2.fillOval((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
    }

}
