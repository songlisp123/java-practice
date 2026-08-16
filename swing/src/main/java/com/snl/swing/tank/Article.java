package com.snl.swing.tank;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class Article {
    Vector2D position;
    double speed;
    Vector2D direction;

    Paint paint;
    float alpha = 1.0f;

    boolean dead;

    public Article(Vector2D position, Vector2D direction,Paint paint, double speed) {
        this.position = position;
        this.direction = direction;
        this.paint = paint;
        this.speed = speed;
    }

    public void update(double delta) {
        position = position.add(
                direction.mul(speed * delta)
        );
    }

    public void draw(Graphics2D g2,DiKaErPlus gameFrame) {
        Paint origin = g2.getPaint();
        g2.setPaint(paint);
        gameFrame.drawCircle(g2,position,0.02,true);
        g2.setPaint(origin);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
