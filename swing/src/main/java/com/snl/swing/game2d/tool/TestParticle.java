package com.snl.swing.game2d.tool;

import java.awt.*;

public class TestParticle {
    private Vector2D pos;
    private Vector2D curPos;
    private Vector2D vel;
    private Vector2D curVel;
    private Color color;
    private double lifeSpan;
    private double time;
    private double radius;

    public TestParticle() {

    }

    public void setPosition(Vector2D pos) {
        this.pos = pos;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setVector(double angle, double r) {
        vel = Vector2D.polar(angle, r);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLifeSpan(double lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public void update(double delta) {
        time += delta;
        curVel = vel.mul(time);
        curPos = pos.add(curVel);
    }

    public void draw(Graphics2D g, Matrix3x3f view) {
        g.setColor(color);
        Vector2D topLeft = new Vector2D(curPos.x - radius, curPos.y + radius);
        topLeft = view.mul(topLeft);
        Vector2D bottomRight = new Vector2D(curPos.x + radius, curPos.y
                - radius);
        bottomRight = view.mul(bottomRight);
        int circleX = (int) topLeft.x;
        int circleY = (int) topLeft.y;
        int circleWidth = (int) (bottomRight.x - topLeft.x);
        int circleHeight = (int) (bottomRight.y - topLeft.y);
        g.fillOval(circleX, circleY, circleWidth, circleHeight);
    }

    public boolean hasDied() {
        return time > lifeSpan;
    }
}
