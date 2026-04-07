package com.snl.swing.practice01.entity;

import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;

public abstract class Sprite implements AbstractSprite {

    private double xPos;
    private double yPos;
    private int WEIGHT;
    private int HEIGHT;

    private double xOrigin;
    private double yOrigin;
    private boolean isDead;
    private int originalWeight;
    private int originalHeight;

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
        this.originalWeight = WEIGHT;
        this.originalHeight = HEIGHT;
    }

//************************* 碰撞逻辑 ******************************//
    /**
     * 测试元素是否相撞
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
     * 判断是否该精灵触碰其他精灵元素
     * @param sprites 墙壁元素集合
     * @return 如果发生碰撞，返回{@code true} ，否则f返回{@code false}
     * @apiNote 注意：对于静态元素，你保持默认实现就行，对于动态元素，则有不同的实现
     */
    public boolean isTouch(Collection<? extends Sprite> sprites) {
        return false;
    }

    /**
     * 判断该元素是否到达屏幕边界
     * @return 如果元素触碰到屏幕边界，返回 {@code true} ,否则，返回"{@code false}
     */
    public boolean isBeyondScene(double width,double height) {
        return xPos <= 0 || xPos + this.WEIGHT >= width ||
                yPos <= 0 || yPos + this.HEIGHT >= height;
    }


    /**
     * 实现该函数，确保不同的精灵实现不同
     * 目前为空实现，你需要为不同精灵实现此方法
     * @apiNote 对于静态元素，不用实现此方法
     */
    public void handleBeyondScene(int width,int height) {
        //不同子类实现
    }


    //****************************** 处理接口 ****************************//

    /**
     * 这个方法是重载的paint方法，主要是因为除了角色外，所有的精灵都不更随输入状态
     * @param g 绘制上下文
     */
    public void paint(Graphics g) {
        this.paint(g,null);
    }

    /**
     * 失败后，将当前精灵重置为初始位置（暂时不实现状态）
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


    /**
     * 此函数在第一帧移动以给定的距离异动精灵
     * @param xPos x轴位移
     * @param yPos y轴位移
     */
    public void move(double xPos, double yPos) {
        //空实现，你必须为不同精灵实现不同
    }

    public boolean isDead() {
        return isDead;
    }

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

    //******************  边界线  *******************//

    @Override
    public double getLeft() {
        return getxPos();
    }

    @Override
    public double getRight() {
        return getxPos() + getWEIGHT();
    }

    @Override
    public double getTop() {
        return getyPos();
    }

    @Override
    public double getBottom() {
        return getyPos() + getHEIGHT();
    }

    public void setWEIGHT(int WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public int getOriginalWeight() {
        return originalWeight;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }
}
