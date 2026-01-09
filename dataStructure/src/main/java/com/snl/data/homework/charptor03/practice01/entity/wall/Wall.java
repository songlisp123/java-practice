package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.entity.Collideable;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * 墙壁主要作为阻挡玩家的脚步,只负责重绘
 */
public class Wall extends Sprite  {

    private Color color;
    private Shape shape;

    /**
     * 无参构造器
     */
    public Wall() {
    }

    public Wall(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this(xPos, yPos, WEIGHT, HEIGHT,Color.lightGray);
    }

    public Wall(double xPos, double yPos, int WEIGHT, int HEIGHT, Color color) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.color = color;
        shape = new Rectangle2D.Double(xPos,yPos,WEIGHT,HEIGHT);
    }

    @Override
    public void update(double delta, InputState state) {
        //无实现
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D) g.create();
        g2.setColor(color);
        g2.fill(shape);
    }

}
