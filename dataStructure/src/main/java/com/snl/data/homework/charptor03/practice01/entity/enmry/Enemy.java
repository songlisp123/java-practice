package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.util.Collection;

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
    }

    public void update(double delta,InputState state, Group group) {
        move(0,SPEED);
        //与墙体的关系
        touchWall(group.getData());
        //处理屏幕边界情况
        handleBeyondScene(GameConstants.Weight,GameConstants.Height);
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



}
