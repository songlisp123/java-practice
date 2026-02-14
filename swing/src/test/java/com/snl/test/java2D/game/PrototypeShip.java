package com.snl.test.java2D.game;

import com.snl.test.java2D.UTIL.Utils;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PrototypeShip {

    private PolygonWrapper wrapper;
    private Vector2D[] poly;
    private List<Vector2D[]> renderList;

    private double angle;
    private double acceleration;
    private double friction;
    private double maxVel;
    private double rotateTheta;
    private double curAcc;
    private Vector2D position;
    private Vector2D speed;

    double width = 0.15;

    public PrototypeShip(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
        friction = .25;
        rotateTheta = Math.PI;
        acceleration = 1.0f;
        maxVel = 1.5;
        speed = new Vector2D();
        position = new Vector2D();
        poly = new Vector2D[] {
                new Vector2D(width, 0.0f),
                new Vector2D(-width, -width),
                new Vector2D(0.0f, 0.0f),
                new Vector2D(-width, width),
        };
        renderList = new ArrayList<>();
    }

    public void update(double delta) {
        updatePos(delta);
        renderList.clear();
        Vector2D[] world =  transform();
        renderList.add(world);
        wrapper.wrapPolygon(world,renderList);
    }

    private Vector2D[] transform() {
        Matrix3x3f mat = Matrix3x3f.rotate(angle);
        return transform(poly, mat);
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] r = new Vector2D[poly.length];
        for (int i=0;i<r.length;i++) {
            r[i] = mat.mul(poly[i]).add(position);
        }
        return r;
    }

    public PrototypeBullet launchBullets() {
        Vector2D p = position.add(Vector2D.polar(angle,width));
        return new PrototypeBullet(p,angle);
    }

    private void updatePos(double delta) {
        Vector2D accel = Vector2D.polar(angle,curAcc);
        speed = speed.add(accel.mul(delta));
        double maX = Math.min(maxVel / speed.len(),1.0);
        speed = speed.mul(maX);
        double slowDown = 1.0 - friction * delta;
        speed = speed.mul(slowDown);
        position = position.add(speed.mul(delta));
        position = wrapper.wrapPos(position);
    }

    public void  draw(Graphics2D g2,Matrix3x3f mat) {
        for (Vector2D[] poly : renderList) {
            for (int i = 0; i < poly.length; ++i) {
                poly[i] = mat.mul(poly[i]);
            }
            Utils.drawPolygon(g2, poly);
        }
    }

    public void rotateLeft(double delta) {
        angle += rotateTheta * delta;
    }

    public void  setThrustion(boolean t ) {
        curAcc = t ?  acceleration : 0.0;
    }

    public void rotateRight(double delta) {
        angle -= rotateTheta * delta;
    }

    public Vector2D getPosition() {
        return position;
    }
}
