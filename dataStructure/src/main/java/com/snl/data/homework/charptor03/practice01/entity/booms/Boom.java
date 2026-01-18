package com.snl.data.homework.charptor03.practice01.entity.booms;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class Boom extends Sprite {

    private double xSpeed;
    private double ySpeed;
    private Direction direction;
    private BoomShape shape;
    private Color color;

    private static final Logger logger = Logger.getLogger("game");

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this(xPos, yPos, WEIGHT, HEIGHT,null,null);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction) {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,BoomShape.RECT);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction, BoomShape shape) {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,shape,Color.GREEN);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT,
                Direction direction, BoomShape shape, Color color)
    {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,shape,Color.GREEN,
                GameConstants.PISTOL_ORIGIN_SHOOT_SPEED,0);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction,double xSpeed,double ySpeed) {
        this(xPos, yPos, WEIGHT, HEIGHT,direction,BoomShape.RECT,Color.CYAN,xSpeed,ySpeed);
    }

    public Boom(double xPos, double yPos, int WEIGHT, int HEIGHT,
                Direction direction, BoomShape shape, Color color,double xSpeed,double ySpeed)
    {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.direction = direction;
        this.shape = shape;
        this.color = color;
        this.xSpeed = xSpeed;
        //默认情况下，子弹不受重力影响
        this.ySpeed = ySpeed;
    }


    @Override
    public void update(double delta, InputState state) {
        move(xSpeed,ySpeed);
    }

    public void update(double delta, InputState state, Group group,Group destory,Group wall,double damage) {
        this.update(delta,state);
        Collection data;
        Iterator<Sprite> iterator;
        if (group != null && !group.isEmpty()) {
            data = group.getData(); //敌人数据
            //判断与敌人的状态(如果不为null且空)
            for (iterator = data.iterator();iterator.hasNext();)
            {
                //遍历组中元素判断是否与炸弹碰撞
                Sprite next = iterator.next();
                if (this.isCrash(next))
                {
                    //设置两者的活动状态
                    logger.warning("射击敌人！");
                    setDead(true);
                    ((Enemy)next).decreaseLifePoint(damage);
                    if (((Enemy) next).getLifePoints() == 0 ){
                        next.setDead(true);
                        destory.add(next);
                    }
                }
            }
        }

        //判断与墙壁的相对位置
        if (wall != null && !wall.isEmpty()) {
            data = wall.getData();
            for (iterator = data.iterator(); iterator.hasNext(); ) {
                //遍历组中元素判断是否与炸弹碰撞
                Sprite next = iterator.next();
                if (this.isCrash(next)) {
                    //子弹与墙相撞
                    setDead(true);
//                    Music.bulletsCrashWall();
                }
            }
        }
        //处理超过屏幕的情况
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
        if (isBeyondScene(width,height)) {
            setDead(true);
            Music.bulletsCrashWall();
        }
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }
}
