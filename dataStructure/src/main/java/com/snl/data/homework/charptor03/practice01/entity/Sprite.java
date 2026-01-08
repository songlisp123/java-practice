package com.snl.data.homework.charptor03.practice01.entity;

import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Sprite {

    private double xPos;
    private double yPos;
    private int WEIGHT;
    private int HEIGHT;

    private double xOrigin;
    private double yOrigin;
    private boolean isDead;

    /**
     * 无参构造函数
     */
    public Sprite() {
    }

    /**
     * 构造函数
     * @param xPos 左上角
     * @param yPos 左上角y坐标
     * @param WEIGHT 精灵矩形框宽度
     * @param HEIGHT 精灵矩形框高度
     */
    public Sprite(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this.xOrigin = xPos;
        this.yOrigin = yPos;
        this.xPos = xPos;
        this.yPos = yPos;
        this.WEIGHT = WEIGHT;
        this.HEIGHT = HEIGHT;
    }

    /**
     * 测试元素是相撞
     * @param sprite 其他的精灵
     * @return 如果两个精灵相撞，则为true；否则返回false;
     */
    public boolean isCrash(Sprite sprite) {
        return (this.xPos + WEIGHT) > sprite.xPos &&
                this.xPos < sprite.xPos + sprite.WEIGHT &&
                (this.yPos + HEIGHT) > sprite.yPos &&
                this.yPos < (sprite.yPos +sprite.HEIGHT);
    }

    /**
     * 判断精灵是否触及"墙壁",注意这个函数可以优化，墙壁可以是固定的场景元素
     * 目前为止，这主要是屏幕宽高度
     * @param wight  屏幕宽度
     * @param height 屏幕高度
     * @return 如果发生相撞，返回true；否则返回false；
     */
    public boolean isTouchWall(double wight,double height) {
        return xPos <= 0 || xPos + this.WEIGHT >= wight ||
                yPos <= 0 || yPos + this.HEIGHT >= height;
    }

    /**
     * 这个方法是重载的paint方法，主要是因为除了角色外，所有的精灵都不更随输入状态
     * @param g 绘制上下文
     */
    public void paint(Graphics g) {
        this.paint(g,null);
    }

    /**
     * 失败后，将当前精灵重置为初始状态，
     * 注意对于硬币来说有点难
     */
    public  void reset() {
        isDead = false;
        setxPos(xOrigin);
        setyPos(yOrigin);
    }

    /**
     * 更新当前精灵的坐标
     * @param delta 时间间隔
     * @param state 输入状态
     */
    public abstract void update(double delta, InputState state);


    /**
     * 该方法让元素自身绘制自身
     * @param g 绘制上下文
     * @param state 状态机制，在这个程序中是用户的状态
     */
    public abstract void paint(Graphics g ,InputState state);

    public boolean isDead() {
        return isDead;
    }

    /**
     * 此函数在第一帧移动以给定的距离异动精灵
     * @param xPos x轴位移
     * @param yPos y轴位移
     */
    public abstract void move(double xPos, double yPos);

    /**
     * 该抽象方法让实现该方法的类处理边界问题
     * @param weight 屏幕宽度
     * @param height 屏幕高度
     */
    public abstract void handleTouchWall(int weight, int height);

    public Point2D getPoint() {
        return new Point2D.Double(getxPos(),getyPos());
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public int getWEIGHT() {
        return WEIGHT;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "xOrigin=" + xOrigin +
                ", yOrigin=" + yOrigin +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", WEIGHT=" + WEIGHT +
                ", HEIGHT=" + HEIGHT +
                '}';
    }
}
