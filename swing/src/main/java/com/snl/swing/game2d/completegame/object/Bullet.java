package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;

import java.awt.*;

public class Bullet {
    private Vector2D velocity;
    private Vector2D position;
    private Color color;
    private double radius;

    public Bullet(Vector2D position, double angle) {
        this.position = position;
        velocity = Vector2D.polar(angle, 1.0f);
        radius = 0.006f;
        color = Color.GREEN;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void draw(Graphics2D g, Matrix3x3f view) {
        g.setColor(color);
        Vector2D topLeft = new Vector2D(position.x - radius, position.y
                + radius);
        topLeft = view.mul(topLeft);
        Vector2D bottomRight = new Vector2D(position.x + radius, position.y
                - radius);
        bottomRight = view.mul(bottomRight);
        int circleX = (int) topLeft.x;
        int circleY = (int) topLeft.y;
        int circleWidth = (int) (bottomRight.x - topLeft.x);
        int circleHeight = (int) (bottomRight.y - topLeft.y);
        g.fillOval(circleX, circleY, circleWidth, circleHeight);
    }

    public void update(double time) {
        position = position.add(velocity.mul(time));
    }
}
