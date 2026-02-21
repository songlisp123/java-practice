package com.snl.swing.game.components;

import com.snl.swing.game.input.MouseInputEvent;

import java.awt.*;

public abstract class CollideObj {
    //包围矩形
    protected double leftX,leftY;
    protected double totalW,totalH;

    public CollideObj(double leftX, double leftY, double totalW, double totalH) {
        this.leftX = leftX;
        this.leftY = leftY;
        this.totalW = totalW;
        this.totalH = totalH;
    }

    public abstract void processInput(MouseInputEvent mouseInputEvent);

    public abstract void update(double delta);

    public abstract void draw(Graphics g);
}
