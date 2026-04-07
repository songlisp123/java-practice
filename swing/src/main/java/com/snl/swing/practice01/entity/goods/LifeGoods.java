package com.snl.swing.practice01.entity.goods;

import com.snl.swing.practice01.entity.Group;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class LifeGoods extends AbstractGoods {

    private double recovery;

    public LifeGoods(double xPos, double yPos, int WEIGHT, int HEIGHT, double recovery) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        //创建形状
        this.recovery = recovery;
        createShape();
    }

    private void createShape() {
        if (getShape() == null)
            shape = new Rectangle2D.Double(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
    }

    @Override
    public void update(double delta, Group aGroup) {
        if (ySpeed == 0)
            return;
        super.update(delta, aGroup);
        updateShape();
    }

    private void updateShape() {
        //更新
        setyPos(getyPos() + ySpeed);
        ((RectangularShape)shape).setFrame(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
    }

    @Override
    public void paint(Graphics g, InputState state) {
        //绘制形状
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.yellow);
        g2.fill(shape);
        g2.dispose();
    }

    public static LifeGoods getInstance(double x,double y,int Weight,int Height,double recovery) {
        return new LifeGoods(x,y,Weight,Height,recovery);
    }

    public double getRecovery() {
        return recovery;
    }

    public void setRecovery(double recovery) {
        this.recovery = recovery;
    }
}
