package com.snl.test.vwctor;

import java.util.Arrays;

public class Matrix3x3f {
    double[][] mat = new double[3][3];

    public Matrix3x3f() {
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
        this.mat = d;
        return this;
    }

    //列乘行
    public Vector2D mul(Vector2D vec){
        return new Vector2D(
                vec.x * this.mat[0][0] //
                        + vec.y * this.mat[0][1] // V.x
                        + vec.w * this.mat[0][2],//
                vec.x * this.mat[1][0] //
                        + vec.y * this.mat[1][1] // V.y
                        + vec.w * this.mat[1][2],//
                vec.x * this.mat[2][0] //
                        + vec.y * this.mat[2][1] // V.w
                        + vec.w * this.mat[2][2] //
        );
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
                {Math.sin(rat),-Math.cos(rat),0.0},
                {Math.cos(rat),Math.sin(rat),0.0},
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] doubles : mat)
            s.append(Arrays.toString(doubles)).append("\n");
        return s.toString();
    }
}
