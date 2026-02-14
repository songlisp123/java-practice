package com.snl.test.java2D.game;

import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;

public class PrototypeBullet {

    private Vector2D pos;
    private Vector2D speed;
    private Color color;
    private  double r;

    public PrototypeBullet(Vector2D pos,double rad) {
        this.pos = pos;
        speed = Vector2D.polar(rad,2.5);
        r = 0.05;
        color = Color.GREEN;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public void  update(double delta) {
        pos = pos.add(speed.mul(delta));
    }

    public void draw(Graphics2D g2, Matrix3x3f view) {
        //TODO
        g2.setColor(color);
        Vector2D topLeft = new Vector2D(pos.getX() - r, pos.getY()
                + r);
        topLeft = view.mul(topLeft);
        Vector2D bottomRight = new Vector2D(pos.getX() + r, pos.getY()
                - r);
        bottomRight = view.mul(bottomRight);
        int circleX = (int) topLeft.getX();
        int circleY = (int) topLeft.getY();
        int circleWidth = (int) (bottomRight.getX() - topLeft.getX());
        int circleHeight = (int) (bottomRight.getY() - topLeft.getY());
        g2.fillOval(circleX, circleY, circleWidth, circleHeight);
    }
}
