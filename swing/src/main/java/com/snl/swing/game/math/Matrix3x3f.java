package com.snl.swing.game.math;


import com.snl.swing.game.utils.Utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class Matrix3x3f {
    double[][] mat = new double[3][3];

    public Matrix3x3f() {
    }

    /**
     * 按照行向量的标准初始化矩阵
     * @param c 行向量数组
     */
    public Matrix3x3f(double[] c) {
        this.mat[0][0] = c[0];
        this.mat[0][1] = c[1];
        this.mat[0][2] = 0;

        this.mat[1][0] = c[2];
        this.mat[1][1] = c[3];
        this.mat[1][2] = 0;

        this.mat[2][0] = c[4];
        this.mat[2][1] = c[5];
        this.mat[2][2] = 1;
    }

    public Matrix3x3f(double[][] mat) {
        this.mat = mat;
    }

    public Matrix3x3f(Matrix3x3f otherMat) {
        this.mat = otherMat.mat;
    }

    /**
     * 矩阵相加
     * @param otherMat 另一个矩阵
     * @return 当前矩阵
     */
    public Matrix3x3f add(Matrix3x3f otherMat) {
        int j,i;
        for (j=0;j< mat.length;j++) {
            //行
            for (i = 0;i<mat[0].length;i++) {
                //列
                mat[j][i] += otherMat.mat[j][i];
            }
        }
        return this;
    }

    /**
     * 矩阵相减
     * @param otherMat 另一个矩阵
     * @return 当前矩阵
     */
    public Matrix3x3f subtract(Matrix3x3f otherMat)
    {
        if (otherMat == this)
            //如果减去自身
            return new Matrix3x3f(new double[3][3]);
        //否则
        int j,i;
        for (j=0;j< mat.length;j++) {
            //行
            for (i = 0;i<mat[0].length;i++) {
                //列
                mat[j][i] -= otherMat.mat[j][i];
            }
        }
        return this;
    }

    /**
     * 矩阵相乘
     * @param otherMat 另一个矩阵
     * @return 当前矩阵
     */
    public Matrix3x3f mul(Matrix3x3f otherMat) {
        //否则
        int j,i;
        double[][] d = new double[3][3];
        for (j=0;j< mat.length;j++) {
            //行
            for (i = 0;i<mat[0].length;i++) {
                //列
                //行乘列
                double v = mat[j][0] * otherMat.mat[0][i] +
                        mat[j][1] * otherMat.mat[1][i] +
                        mat[j][2] * otherMat.mat[2][i];
                d[j][i] = v;
            }
        }
        return new Matrix3x3f(d);
    }

    //列乘行
    public Vector2D mul(Vector2D vec){
        return new Vector2D(
                this.mat[0][0] * vec.x +  this.mat[0][1] * vec.y +  this.mat[0][2] * vec.w,
                this.mat[1][0] * vec.x + this.mat[1][1] * vec.y + this.mat[1][2] * vec.w,
                this.mat[2][0] * vec.x + this.mat[2][1] * vec.y +  this.mat[2][2] * vec.w
        );
    }

    public Point2D mul(Point2D p) {
        Vector2D v = mul(new Vector2D(p.getX(), p.getY()));
        return Utils.vectorCovertToPoint(v);
    }


    public static Matrix3x3f translate(Vector2D v) {
        return translate(v.x,v.y);
    }

    public static Matrix3x3f translate(double dx,double dy) {
        return new Matrix3x3f(new double[][]{
                {1.0,0.0,dx},
                {0.0,1.0,dy},
                {0.0,0.0,1.0},
        });
    }

    public static Matrix3x3f scale(Vector2D v) {
        return scale(v.x,v.y);
    }

    public static Matrix3x3f scale(double sx,double sy) {
        return new Matrix3x3f(new double[][]{
                {sx,0.0,0.0},
                {0.0,sy,0.0},
                {0.0,0.0,1.0},
        });
    }

    public static  Matrix3x3f rotate(double rat) {
        return new Matrix3x3f(new double[][]{
                {Math.cos(rat),-Math.sin(rat),0.0},
                {Math.sin(rat),Math.cos(rat),0.0},
                {0.0,0.0,1.0}
        });
    }

    public static Matrix3x3f shear(Vector2D v) {
        return shear(v.x,v.y);
    }

    public static Matrix3x3f shear(double sx,double sy){
        return new Matrix3x3f(new double[][]{
                {1.0,sx,0.0},
                {sy,1.0,0.0},
                {0.0,0.0,1.0},
        });
    }

    /**
     * 归零矩阵
     * @return 零矩阵
     */
    public static Matrix3x3f zero() {
        return new Matrix3x3f(new double[][] {
                {0.0,0.0,0.0},
                {0.0,0.0,0.0},
                {0.0,0.0,0.0},
        });
    }

    /**
     * 单位矩阵
     * @return 单位矩阵
     */
    public static Matrix3x3f identity() {
        return new Matrix3x3f(new double[][]{
                {1.0,0.0,0.0},
                {0.0,1.0,0.0},
                {0.0,0.0,1.0},
        });
    }

    /**
     * 反转y轴
     * @return 翻转Y轴
     */
    public static Matrix3x3f flipYAix() {
        return new Matrix3x3f(new double[][]{
                {-1.0,.0,.0},
                {.0,1.0,.0},
                {.0,.0,1.0},
        });
    }

    /**
     * 反转x轴
     * @return x轴镜像
     */
    public static Matrix3x3f flipXAix() {
        return new Matrix3x3f(new double[][]{
                {1.0,.0,.0},
                {.0,-1.0,.0},
                {.0,.0,1.0},
        });
    }

    /**
     * 逆矩阵
     * @return 该矩阵的逆
     * @implNote 采用的是伴随余子式
     */
    public Matrix3x3f inverse() {
        double a  = mat[0][0];
        double b  = mat[0][1];
        double tx = mat[0][2];

        double c  = mat[1][0];
        double d  = mat[1][1];
        double ty = mat[1][2];

        double det = a * d - b * c;

        if (Math.abs(det) < 1e-8) {
            throw new ArithmeticException("矩阵必须可逆");
        }

        double invDet = 1.0 / det;

        Matrix3x3f inv = new Matrix3x3f();
        inv.mat[0][0] =  d * invDet;
        inv.mat[0][1] = -b * invDet;
        inv.mat[0][2] = (b * ty - d * tx) * invDet;

        inv.mat[1][0] = -c * invDet;
        inv.mat[1][1] =  a * invDet;
        inv.mat[1][2] = (c * tx - a * ty) * invDet;

        inv.mat[2][0] = 0;
        inv.mat[2][1] = 0;
        inv.mat[2][2] = 1;

        return inv;
    }

    public static AffineTransform convertIntoAffineTransform(Matrix3x3f m) {
        return new AffineTransform(
                m.mat[0][0],m.mat[1][0],m.mat[0][1],m.mat[1][1],m.mat[0][2],m.mat[1][2]
        );
    }

    public Matrix3x3f getReverseTranslation() {
        double a  = mat[0][0];
        double b  = mat[0][1];
        double tx = -mat[0][2];

        double c  = mat[1][0];
        double d  = mat[1][1];
        double ty = -mat[1][2];

        Matrix3x3f r = new Matrix3x3f();
        r.mat[0][0] = a;
        r.mat[0][1] = b;
        r.mat[0][2] = tx;

        r.mat[1][0] = c;
        r.mat[1][1] = d;
        r.mat[1][2] = ty;

        r.mat[2][0] = 0;
        r.mat[2][1] = 0;
        r.mat[2][2] = 1;
        return r;

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] doubles : mat)
            s.append(Arrays.toString(doubles)).append("\n");
        return s.toString();
    }
}
