package com.snl.test.java2D.game;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.UTIL.Utils;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PrototypeAsteroid {

    public enum Size {
        small,middle,large
    };

    private PolygonWrapper wrapper;
    private Size size;
    private double rot,theta;
    private Vector2D[] polygon;
    private Vector2D pos;
    private Vector2D vel;
    private List<Vector2D[]> renderList;

    public PrototypeAsteroid(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
        renderList = new ArrayList<>();
        double vx = RandomGeneratorClass.random(-0.3, 0.3);
        double vy = RandomGeneratorClass.random(-0.3,0.3);
        vel = new Vector2D(vx,vy);
        theta = getRandomTheta();
    }

    private double getRandomTheta() {
        return RandomGeneratorClass.random(-Math.PI / 2,Math.PI/2);
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Vector2D[] getPolygon() {
        return polygon;
    }

    public void setPolygon(Vector2D[] polygon) {
        this.polygon = polygon;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public void  update(double delta) {
        pos = pos.add(vel.mul(delta));
        pos = wrapper.wrapPos(pos);
        rot += theta * delta;
        renderList.clear();
        Vector2D[] world = transform();
        renderList.add(world);
        wrapper.wrapPolygon(world,renderList);
    }

    private Vector2D[] transform() {
        Matrix3x3f r = Matrix3x3f.rotate(rot);
        return transform(polygon,r);
    }

    private Vector2D[] transform(Vector2D[] polygon, Matrix3x3f r) {
        Vector2D[] copy = new Vector2D[polygon.length];
        for (int i=0;i<copy.length;i++)
        {
            copy[i] = r.mul(polygon[i]).add(pos);
        }
        return copy;
    }

    public void draw(Graphics2D g2,Matrix3x3f mat) {
        for (Vector2D[] v : renderList)
        {
            Vector2D[] p = new Vector2D[v.length];
            for (int i=0;i< v.length;i++)
            {
                p[i] = mat.mul(v[i]);
            }
            Utils.drawPolygon(g2,p);
        }
    }

    public boolean contains(Vector2D pos)
    {
        for (Vector2D[] s : renderList)
        {
            if (pointInPolygon(pos,s))
                return true;
        }
        return false;
    }

    private boolean pointInPolygon(Vector2D pos,Vector2D[] poly) {
        if (poly.length <= 2)
            return false;
        int inside = 0;
        Vector2D s = poly[polygon.length-1];
        boolean start = pos.getY() > s.getY();
        for (Vector2D e : poly) {
            boolean end = pos.getY() > e.getY();
            if (start != end) {
                double k = (e.getY() - s.getY()) / (e.getX() - s.getX());
                double x = s.getX() + (pos.getY() - s.getY()) / k;
                if (x > pos.getX())
                    inside++;
            }

            start = end;
            s = e;
        }
        return inside % 2 != 0;
    }
}
