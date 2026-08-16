package com.snl.swing.game.math;

import java.util.Arrays;

public class Matrix4x4f {
    //看起来我们还是希望使用列向量
    private double[][] data;

    public static  Matrix4x4f Identity;

    public Matrix4x4f() {
        data = new double[4][4];
        setToIdentity();
    }

    public static Matrix4x4f translate(Vector3D v3) {
        Matrix4x4f m4 = new Matrix4x4f();
        m4.data[0][3] = v3.x;
        m4.data[1][3] = v3.y;
        m4.data[2][3] = v3.z;
        return m4;
    }

    //设置列向量
    public void setColumn(int column, Vector3D v3) {
        this.setColumn(column,
                v3.x, v3.y, v3.z, v3.w);
    }

    private void setColumn(int column, double x, double y, double z, double w) {
        checkColumnIndex(column);
        this.data[0][column] = x;
        this.data[1][column] = y;
        this.data[2][column] = z;
        this.data[3][column] = w;
    }

    private void checkColumnIndex(int columnIndex) {
        if (columnIndex < 0 || columnIndex > 3)
            throw new IndexOutOfBoundsException("请确定"+columnIndex + "处于[0,3]区间内");
    }


    public void setToIdentity() {
        for (int  i = 0; i < 4 ; i ++) {
            Arrays.fill(data[i],.0);
        }
        for (int i = 0;i<4;i++) {
            data[i][i] = 1.0;
        }
    }

    public static Matrix4x4f identity() {
        if (Identity == null) {
            Identity = new Matrix4x4f();
        }
        return Identity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix4x4f other))
            return false;
        if (other == this)
            return true;
        for (int i = 0;i< 4;i++) {
            for (int j = 0;j < 4;j++) {
                if (this.data[i][j] != other.data[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] doubles : data)
            s.append(Arrays.toString(doubles)).append("\n");
        return s.toString();
    }

    public Vector3D getColumn(int columnIndex) {
        checkColumnIndex(columnIndex);
        return new Vector3D(
                data[0][columnIndex],data[1][columnIndex],data[2][columnIndex],
                data[3][columnIndex]
        );
    }


    public void swapColumns(int columnIndex01,int columnIndex02) {
        checkColumnIndex(columnIndex01);
        checkColumnIndex(columnIndex02);
        double temp;
        for (int  i= 0;i<4;i++) {
            temp = data[i][columnIndex01];
            data[i][columnIndex01] = data[i][columnIndex02];
            data[i][columnIndex02] = temp;
        }
    }

    //矩阵算式性质
    //1、相加
    public void add(Matrix4x4f mat) {
        if (mat == null)
            return;
        int col,row;
        for (row = 0;row < 4;row ++) {
            for (col = 0; col < 4 ; col ++ ) {
                this.data[row][col] += mat.data[row][col];
            }
        }
    }

    //2 、 相减
    public void subtract(Matrix4x4f mat) {
        if (mat == null)
            return;
        int col,row;
        for (row = 0;row < 4;row ++) {
            for (col = 0; col < 4 ; col ++ ) {
                this.data[row][col] -= mat.data[row][col];
            }
        }
    }

    //3、标量乘
    public void mul(double factor) {
        int col,row;
        for (row = 0;row < 4;row ++) {
            for (col = 0; col < 4 ; col ++ ) {
                this.data[row][col] *= factor;
            }
        }
    }

    //4、矩阵乘
    public void mul(Matrix4x4f mat) {

        if (mat == null)
            return;

        double[][] result = new double[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                double sum = 0.0;

                for (int k = 0; k < 4; k++) {

                    sum += this.data[row][k] * mat.data[k][col];

                }

                result[row][col] = sum;
            }
        }

        this.data = result;
    }

    //齐次方程 - 矩阵乘向量
    public Vector3D  mul(Vector3D v3) {
        if (v3 == null)
            return null;
        Vector3D r = new Vector3D();
        //列乘行
        r.x = v3.x * data[0][0] + data[0][1] * v3.y + data[0][2] * v3.z
                + data[0][3] * v3.w;
        r.y = v3.x * data[1][0] + data[1][1] * v3.y + data[1][2] * v3.z
                + data[1][3] * v3.w;
        r.z = v3.x * data[2][0] + data[2][1] * v3.y + data[2][2] * v3.z
                + data[2][3] * v3.w;
        r.w = v3.x * data[3][0] + data[3][1] * v3.y + data[3][2] * v3.z
                + data[3][3] * v3.w;
        return r;
    }

    //del值
    public double delta() {
        double det = 0.0;
        for (int col = 0; col < 4; col++) {
            det += data[0][col] * cofactor(0, col);
        }
        return det;
//        ????
    }

    public double get(int row,int col) {
        checkColumnIndex(row);
        checkColumnIndex(col);
        return data[row][col];
    }

    public void set(double value,int row,int col) {
        checkColumnIndex(row);
        checkColumnIndex(col);
        data[row][col] = value;
    }

    //伴随余子式
    public double[][] minorMatrix(int rowRemoved,int colRemoved) {
        double[][] result=new double[3][3];

        int r=0;

        for(int i=0;i<4;i++){

            if(i==rowRemoved)
                continue;

            int c=0;

            for(int j=0;j<4;j++){

                if(j==colRemoved)
                    continue;

                result[r][c]=data[i][j];
                c++;
            }

            r++;
        }

        return result;
    }

    private double determinant3x3(double[][] m){

        return

                m[0][0]*m[1][1]*m[2][2]

                        +

                        m[0][1]*m[1][2]*m[2][0]

                        +

                        m[0][2]*m[1][0]*m[2][1]

                        -

                        m[0][2]*m[1][1]*m[2][0]

                        -

                        m[0][1]*m[1][0]*m[2][2]

                        -

                        m[0][0]*m[1][2]*m[2][1];
    }

    private double minor(int row,int col){

        return determinant3x3(
                minorMatrix(row,col)
        );
    }

    private double cofactor(int row,int col){

        double m=minor(row,col);

        if(((row+col)&1)==1)
            m=-m;

        return m;
    }

    //转置矩阵
    public Matrix4x4f transpose() {

        Matrix4x4f t = new Matrix4x4f();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                t.data[col][row] = this.data[row][col];
            }
        }

        return t;
    }


    //伴随余子式
    public Matrix4x4f cofactorMatrix() {

        Matrix4x4f c = new Matrix4x4f();

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                c.data[row][col] = cofactor(row, col);

            }

        }

        return c;
    }


    //伴随矩阵
    public Matrix4x4f adjoint() {

        return cofactorMatrix().transpose();

    }

    public Matrix4x4f copy() {

        Matrix4x4f m = new Matrix4x4f();

        for (int row = 0; row < 4; row++) {

            System.arraycopy(this.data[row], 0, m.data[row], 0, 4);

        }

        return m;
    }


    //逆矩阵
    public Matrix4x4f inverse() {
        double det = delta();
        if (Math.abs(det) < 1E-10) {
            throw new ArithmeticException("Matrix is singular, cannot invert.");
        }
        Matrix4x4f adj = adjoint();
        Matrix4x4f inv = new Matrix4x4f();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                inv.data[row][col] = adj.data[row][col] / det;
            }
        }

        return inv;
    }
    //三维变换
}
