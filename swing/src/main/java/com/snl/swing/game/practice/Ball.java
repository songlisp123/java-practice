package com.snl.swing.game.practice;

import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ball implements Cloneable {
    private static final double GRAVITY = 9.98;
    Vector2D pos,posCopy;
    Vector2D speed;
    Vector2D offset;
    double r;
    double oldYSpeed;
    List<Vector2D> olds = new ArrayList<>();

    public Ball(Vector2D pos,double r) {
        this.pos = pos;
        this.posCopy = pos;
        this.r = r;
    }

    public Ball(Ball ball) {
        this.pos = ball.pos;
        this.speed = ball.speed;
        this.offset = ball.offset;
        this.r = ball.r;
        this.oldYSpeed = ball.oldYSpeed;
        this.posCopy = ball.posCopy;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }

    public void animation(double delta) {
        double dx = getSpeed().getX() * delta;
        oldYSpeed = getSpeed().getY();
        double newYSpeed = oldYSpeed - GRAVITY * delta;
        double dy = (oldYSpeed + newYSpeed) * delta / 2;
        speed.setY(newYSpeed);
        offset = offset.add(new Vector2D(dx,dy));
        speed.setX(speed.getX());

        pos = posCopy.add(offset);
        olds.add(pos);
    }

    public List<Vector2D> getOlds() {
        return olds;
    }

    @Override
    protected Ball clone() {
        return new Ball(this);
    }
}
