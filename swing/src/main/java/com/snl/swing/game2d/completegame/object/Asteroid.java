package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid {
    public enum Size {
        Large,
        Medium,
        Small;
    }

    private PolygonWrapper wrapper;
    private Size size;
    private Sprite sprite;
    private double rotation;
    private double rotationDelta;
    private Vector2D[] polygon;
    private Vector2D position;
    private Vector2D velocity;
    private ArrayList<Vector2D[]> collisionList;
    private ArrayList<Vector2D> positionList;

    public Asteroid(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
        collisionList = new ArrayList<Vector2D[]>();
        positionList = new ArrayList<Vector2D>();
        velocity = getRandomVelocity();
        rotationDelta = getRandomRotationDelta();
    }

    private Vector2D getRandomVelocity() {
        double angle = getRandomRadians(0, 360);
        double radius = getRandomdouble(0.06f, 0.3f);
        return Vector2D.polar(angle, radius);
    }

    private double getRandomRadians(int minDegree, int maxDegree) {
        int rand = new Random().nextInt(maxDegree - minDegree + 1);
        return (double) Math.toRadians(rand + minDegree);
    }

    private double getRandomRotationDelta() {
        double radians = getRandomRadians(5, 45);
        return new Random().nextBoolean() ? radians : -radians;
    }

    private double getRandomdouble(double min, double max) {
        double rand = new Random().nextDouble();
        return rand * (max - min) + min;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setPolygon(Vector2D[] polygon) {
        this.polygon = polygon;
    }

    public Vector2D[] getPolygon() {
        return polygon;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public void update(double time) {
        position = position.add(velocity.mul(time));
        position = wrapper.wrapPosition(position);
        rotation += rotationDelta * time;
        collisionList.clear();
        Vector2D[] world = transformPolygon();
        collisionList.add(world);
        wrapper.wrapPolygon(world, collisionList);
        positionList.clear();
        positionList.add(position);
        wrapper.wrapPositions(world, position, positionList);
    }

    private Vector2D[] transformPolygon() {
        Matrix3x3f mat = Matrix3x3f.rotate(rotation);
        mat = mat.mul(Matrix3x3f.translate(position));
        return transform(polygon, mat);
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] copy = new Vector2D[poly.length];
        for (int i = 0; i < poly.length; ++i) {
            copy[i] = mat.mul(poly[i]);
        }
        return copy;
    }

    public void draw(Graphics2D g, Matrix3x3f view) {
        for (Vector2D pos : positionList) {
            sprite.render(g, view, pos, rotation);
        }
    }

    public boolean contains(Vector2D point) {
        for (Vector2D[] polygon : collisionList) {
            if (pointInPolygon(point, polygon)) {
                return true;
            }
        }
        return false;
    }

    private boolean pointInPolygon(Vector2D point, Vector2D[] polygon) {
        boolean inside = false;
        Vector2D start = polygon[polygon.length - 1];
        boolean startAbove = start.y >= point.y;
        for (int i = 0; i < polygon.length; ++i) {
            Vector2D end = polygon[i];
            boolean endAbove = end.y >= point.y;
            if (startAbove != endAbove) {
                double m = (end.y - start.y) / (end.x - start.x);
                double x = start.x + (point.y - start.y) / m;
                if (x >= point.x) {
                    inside = !inside;
                }
            }
            startAbove = endAbove;
            start = end;
        }
        return inside;
    }
}
