package com.snl.data.homework.charptor03.practice01.entity.booms;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class Boom extends Sprite {

    private final double SPEED = 5.0;
    private Direction direction;

    private BoomShape shape;
    private Color color;

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this(xPos, yPos, WEIGHT, HEIGHT,null,null);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction) {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,BoomShape.RECT);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction, BoomShape shape) {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,shape,Color.GREEN);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction, BoomShape shape, Color color) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.direction = direction;
        this.shape = shape;
        this.color = color;
    }

    @Override
    public void update(double delta, InputState state) {
        switch (direction) {
            case EAST -> move(SPEED,0);
            case NORTH -> move(0,-SPEED);
            case SOUTH -> move(0,SPEED);
            case WEST -> move(-SPEED,0);
        }
    }

    public void update(double delta, InputState state, Group group,Group destory,Group wall) {
        this.update(delta,state);
        Collection data = group.getData(); //敌人数据
        Iterator<Sprite> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            //遍历组中元素判断是否与炸弹碰撞
            Sprite next = iterator.next();
            if (this.isCrash(next))
            {
                //设置两者的活动状态
                System.out.println("相撞");
                setDead(true);
                next.setDead(true);
                destory.add(next);
            }
        }
        data = wall.getData();
        for (iterator = data.iterator();iterator.hasNext();) {
            //遍历组中元素判断是否与炸弹碰撞
            Sprite next = iterator.next();
            if (this.isCrash(next))
            {
                //子弹与墙相撞
                setDead(true);
            }
        }
        handleBeyondScene(GameConstants.Weight,GameConstants.Height);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(color);
        drawBoom(g2);
        g2.dispose();
    }

    private void drawBoom(Graphics2D g2) {
        switch (shape) {
            case RECT -> g2.fillRect((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
            case CIRCLE -> g2.fillOval((int) getxPos(), (int) getyPos(),getWEIGHT(),getHEIGHT());
            default -> System.out.println("其他轻装");
        }
    }

    @Override
    public void move(double xPos, double yPos) {
        setxPos(getxPos() + xPos);
        setyPos(getyPos() + yPos);
    }

    @Override
    public void handleBeyondScene(int width, int height) {
        if (isBeyondScene(width,height))
            setDead(true);
    }
}
