package com.snl.swing.tank;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

public abstract class CircleObject extends RotateComponent  {


    private int edge;
    private double radius;


    public CircleObject() {
        this(8);
    }

    public CircleObject(int edge) {
        this(edge,1.0);
    }

    public CircleObject(int edge, double radius) {
        super();
        this.edge = edge;
        this.radius = radius;
        createVertices();
    }

    public void createVertices() {
        if (edge < 3)
            throw new IllegalArgumentException("非法参数异常");
        if (radius <= 0)
            throw new IllegalArgumentException("参数异常");


        double angle = 360.0 / edge;
        double theta = angle / 180 * Math.PI;
        outlines = new Vector2D[edge];
        for (int i = 0; i < edge ; i++) {
            //TODO
            double rot = i * theta;
            double x = radius * Math.cos(rot);
            double y = radius * Math.sin(rot);
            outlines[i] = new Vector2D(x,y);
        }
    }

    public int getEdge() {
        return edge;
    }

    public double getRadius() {
        return radius;
    }


    public void setEdge(int edge) {
        this.edge = edge;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void update(double delta,Component parentComponent) {
        if (copy == null)
            copy = new Vector2D[getEdge()];

        update(parentComponent);
        if(getLastRot() != getRot())
            rotateForm = Matrix3x3f.rotate(getRot());
        Matrix3x3f WORLDtRANSFORM = modelToWorld();
        //绘制大圆
        for (int i = 0; i < outlines.length; i++) {
            copy[i] = WORLDtRANSFORM.mul(outlines[i]);
        }
    }

    public Vector2D[] getScaled(double scale) {
        double newRadius = scale * radius;
        double angle = 360.0 / edge;
        double theta = angle / 180 * Math.PI;
        Vector2D[] results = new Vector2D[edge];
        for (int i = 0; i < edge ; i++) {
            //TODO
            double rot = i * theta;
            double x = newRadius * Math.cos(rot);
            double y = newRadius * Math.sin(rot);
            results[i] = new Vector2D(x,y);
        }
        return results;
    }

}
