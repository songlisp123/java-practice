package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class Enemy extends Sprite {

    /**
     * 玩家生命
     */
    private double lifePoints;

    /**
     * 玩家初始生命点数
     */
    private double originLifePoints;

    /**
     * 玩家的生命条框
     */
    private Shape shape;

    /**
     * 玩家生命槽
     */
    private Shape lifeShape;

    /**
     * 玩家与生命槽之间的距离
     */
    private final double GAP = 12;

    /**
     * 生命槽高度
     * @apiNote 生命槽宽度等于初始化生命值
     */
    private final int LIFE_HEIGHT = 15;

    private double ySpeed = 4.0;
    private double xSpeed;

    private double textXPos;
    private double textYPos;
    private Color lifeColor;
    private boolean hasShing;
    private long startShing;

    public Enemy() {
    }

    public Enemy(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        originLifePoints = 100.0;
        lifePoints = originLifePoints;
        lifeColor = Color.RED;
        calculateLife();
    }



    @Override
    public void update(double delta, InputState state) {
        //空实现
    }

    public void update(double delta,InputState state, Group group) {
        move(xSpeed,ySpeed);
        //与墙体的关系
        touchWall(group.getData());
        //处理屏幕边界情况
        handleBeyondScene(GameConstants.Weight,GameConstants.Height+240);
        //计算生命槽
        calculateLife();
    }


    public void calculateLife() {
        double left_x = getxPos() - (originLifePoints - getWEIGHT()) / 2;
        double left_y = getyPos() - GAP - LIFE_HEIGHT;
        shape = new Rectangle2D.Double(left_x,left_y,originLifePoints,LIFE_HEIGHT);
        var s = new Rectangle2D.Double(left_x,left_y,lifePoints,LIFE_HEIGHT);
        var temp = new Rectangle2D.Double(left_x,left_y,0,0);
        lifeShape = s;
        //计算渲染文本值
        textXPos = left_x + originLifePoints + 10;
        textYPos = left_y;
        //渐变因子
        double f = lifePoints / originLifePoints;
        //频繁闪烁
        if (f <= 0.5 && !hasShing) {
            startShing = System.currentTimeMillis();
            hasShing = true;
            lifeShape = temp;
        }
        long now = System.currentTimeMillis();
        if (hasShing && now - startShing >= Math.pow(f,2)*1_000) {
            hasShing = false;
            lifeShape = s;
        }
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.red);
        g2.fillRect((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
        //绘制生命槽
        paintLifePoints(g2);
        g2.dispose();
    }

    private void paintLifePoints(Graphics2D g2) {
        //TODO 如何绘制生命条？
        g2.setPaint(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        g2.setColor(lifeColor);
        g2.fill(lifeShape);
        g2.drawString("%.2f / %.2f%%".formatted(lifePoints,originLifePoints),
                (int) textXPos, (int) textYPos);
    }

    @Override
    public void move(double xPos, double yPos) {
        //TODO
        setxPos(getxPos() + xPos);
        setyPos(getyPos() + yPos);
    }

    @Override
    public void handleBeyondScene(int width, int height) {
        //TODO 仅仅是一个简单的实现
        if (touchUpBounder() || touchBottomBounder(height))
            ySpeed = -ySpeed;
        if (touchRightBounder(width) || touchLeftBounder(0)) {
            xSpeed = -xSpeed;
        }
    }

    private boolean touchUpBounder() {
        return getyPos() <= 0;
    }

    private boolean touchBottomBounder(int height) {
        return (getyPos()+getHEIGHT()) >= height;
    }

    private boolean touchLeftBounder(double x) {
        return getxPos() <= x;
    }

    private boolean touchRightBounder(double weight) {
        return (getxPos()+getWEIGHT()) >= weight;
    }

    public void touchWall(Collection<? extends Sprite> sprites) {
        if (sprites == null || sprites.isEmpty())
            return;
        for (Sprite sprite : sprites)
        {
            if (isCrash(sprite))
            {
                handleCollide(sprite);
            }
        }
    }

    public void handleCollide(Sprite sprite) {
        if (sprite == null)
            return;
        double dxLeft   = Math.abs(getRight() - sprite.getLeft());
        double dxRight  = Math.abs(getLeft() - sprite.getRight());
        double dyTop    = Math.abs(getBottom() - sprite.getTop());
        double dyBottom = Math.abs(getTop() - sprite.getBottom());

        double min = Math.min(Math.min(dxLeft, dxRight), Math.min(dyTop, dyBottom));

        if (min == dxLeft) {
            xSpeed = -xSpeed;;
        } else if (min == dxRight) {
            xSpeed = -xSpeed;;
        } else if (min == dyTop) {
            ySpeed = -ySpeed;
        } else {
            ySpeed = - ySpeed;
        }
    }

    //************************* 生命系统 *********************//
    public double getLifePoints() {
        return lifePoints;
    }

    public void decreaseLifePoint (double decreased) {
        lifePoints = Math.max(0,lifePoints - decreased);
    }

    public void addLifePoints(double added) {
        lifePoints = Math.min(100,lifePoints + added);
    }

    public void resetLifePoints() {
        lifePoints = originLifePoints;
    }

    @Override
    public void reset() {
        super.reset();
        resetLifePoints();
    }

    public void setLifePoints(double lifePoints) {
        this.lifePoints = lifePoints;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setOriginLifePoints(double originLifePoints) {
        this.originLifePoints = originLifePoints;
        this.lifePoints = originLifePoints;
    }
}
