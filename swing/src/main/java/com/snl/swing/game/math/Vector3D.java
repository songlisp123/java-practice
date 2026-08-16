package com.snl.swing.game.math;

import java.io.Serializable;

public class Vector3D implements Cloneable , Serializable{
    public double x,y,z,w;
    public static final Vector3D Original_Point = Vector3D.point(0,0,0);

    public static final Vector3D I  = Vector3D.direction(1,0,0);
    public static final Vector3D J  = Vector3D.direction(0,1,0);
    public static final Vector3D K  = Vector3D.direction(0,0,1);

    public static final long S_Id = -4215612513665L;

    public Vector3D() {
        this(0.0,0.0,0.0);
    }

    public Vector3D(double x, double y, double z) {
        this(x,y,z,1.0);
    }

    //我不确定这个是否正确？？
    public Vector3D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    public Vector3D(Vector3D other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    //方向向量
    public Vector3D subtract(Vector3D other) {
         double xv = x - other.x;
        double yv = y - other.y;
        double zv =  z -  other.z;
        double wv = w - other.w;
        return new Vector3D(xv,yv,zv,wv);
    }

    public Vector3D  add(Vector3D other) {
        double xv,yv,zv,wv;
        xv = x + other.x;
        yv = y + other.y;
        zv = z + other.z;
        wv = w + other.w;
        return new Vector3D(xv,yv,zv,wv);
    }

    public Vector3D mul(double factor) {
        double xv,yv,zv,wv;
        xv = x * factor;
        yv = y * factor;
        zv = z * factor;
        wv = w * factor;
        return new Vector3D(xv,yv,zv,wv);
    }

    public Vector3D inv() {
        return new Vector3D(-x,-y,-z);
    }

    public double length() {
        return Math.sqrt(
                x * x + y * y + z * z
        );
    }

    public double lengthInSquare() {
        return x * x + y * y + z * z;
    }

    public Vector3D norm() {

        return  mul(1.0 / length());

    }

    //坐标变换
    //算了ai实现算了


    @Override
    protected Vector3D clone() throws CloneNotSupportedException {
        return new Vector3D(this);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3D other))
            return false;
        if (other == this)
            return true;
        return this.x == other.x &&
                this.y == other.y
                && this.z == other.z &&
                this.w == other.w;
    }

    public double dot(Vector3D vector3D) {
        return x * vector3D.x + y * vector3D.y + z * vector3D.z;
    }

    public Vector3D crossDot(Vector3D v3) {
        double x = this.y * v3.z - z * v3.y;
        double y = this.z * v3.x - this.x * v3.z;
        double z = this.x * v3.y - this.y * v3.x;
        return new Vector3D(x,y,z,0.0);
    }

    public static Vector3D point(double x,double y,double z) {
        return new Vector3D(x,y,z,1.0);
    }

    public static Vector3D direction(double x,double y,double z) {
        return new Vector3D(x,y,z,0.0);
    }

    @Override
    public String toString() {
        return "[" + "\n" +
                x + "\n" +
                y + "\n" +
                z + "\n" +
                w + "]";
    }

    public boolean isZero() {
        if (this.x != 0)
            return false;
        else if (this.y != 0) {
            return false;
        } else return this.z == 0;
    }

    public void perspectiveDivide() {
        if (w == 0) {
            return;
        }
        this.x /= w;
        this.y /= w;
        this.z /= w;
        this.w = 1;
    }
}
