package com.snl.swing.practice01.entity.booms;

import com.snl.swing.practice01.CONSTANTS.GameConstants;
import com.snl.swing.practice01.entity.Group;
import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.enmry.Enemy;
import com.snl.swing.practice01.state.Direction;
import com.snl.swing.practice01.state.InputState;

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


    /******************************  更新  ****************************/
    @Override
    public void update(double delta, InputState state) {
        move(xSpeed,ySpeed);
    }

    /**
     * 更新子弹行为
     * @param delta 时间间隔
     * @param state 输入状态
     * @param group 敌人组
     * @param destory 毁灭的敌人组
     * @param wall 墙壁组
     * @param damage 伤害？？
     */
    public void update(double delta, InputState state, Group group,Group destory,Group wall,double damage) {
        this.update(delta,state);
        handleEnmry(group,destory,damage);
        //判断与墙壁的相对位置
        handleWall(wall);
        //处理超过屏幕的情况
        handleBeyondScene(GameConstants.Weight,GameConstants.Height);
    }

    private void handleWall(Group wall) {
        if (wall != null && !wall.isEmpty()) {
            Collection<Sprite> data = wall.getData();
            Iterator<Sprite> iterator;
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
    }

    private void handleEnmry(Group group, Group destory, double damage) {
        if (group != null && !group.isEmpty()) {
            Collection<Sprite> data = group.getData(); //敌人数据
            //判断与敌人的状态(如果不为null且空)
            Iterator<Sprite> iterator;
            for (iterator = data.iterator(); iterator.hasNext();)
            {
                //遍历组中元素判断是否与炸弹碰撞
                Sprite next = iterator.next();
                if (this.isCrash(next))
                {
                    //设置两者的活动状态
                    logger.warning("射击敌人！");
                    setDead(true); //子弹死亡
                    ((Enemy)next).decreaseLifePoint(damage); //敌人减少生命
                    if (((Enemy) next).getLifePoints() == 0 ){
                        //如果生命归零，清楚该敌人
                        next.setDead(true);
                        //在破坏组中加入刚刚破坏的元素
                        destory.add(next);
                    }
                }
            }
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
//            Music.bulletsCrashWall();
        }
    }

    /******************************  绘制  ****************************/
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
