package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class Enemy extends Sprite {

    private double SPEED = 4.0;
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
        move(0,SPEED);
        //与墙体的关系
        touchWall(group.getData());
        //处理屏幕边界情况
        handleBeyondScene(GameConstants.Weight,GameConstants.Height);
        //计算生命槽
        calculateLife();
    }


    private void calculateLife() {
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

    public void touchWall(Collection<? extends Sprite> sprites) {
        if (sprites == null || sprites.isEmpty())
            return;
        for (Sprite sprite : sprites)
        {
            if (isCrash(sprite))
            {
                this.handleCollide(sprite);
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
            SPEED = -SPEED;;
        } else if (min == dxRight) {
            SPEED = -SPEED;;
        } else if (min == dyTop) {
            SPEED = -SPEED;
        } else {
            SPEED = -SPEED;
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
}
