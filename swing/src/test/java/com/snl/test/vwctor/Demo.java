package com.snl.test.vwctor;

public class Demo {
    public static void main(String[] args) {
        Matrix3x3f t = Matrix3x3f.translate(20, 26);
        System.out.println("t = " + t);
        //先平移后旋转
        Matrix3x3f r = Matrix3x3f.rotate(Math.PI / 4);
        System.out.println("r = " + r);
        //测试向量

        Vector2D v2 = new Vector2D(10,12);
        Vector2D v;

        Matrix3x3f s  =new Matrix3x3f(new double[][]{
                {1,1,1},
                {1,1,0},
                {0,1,1},
        });
        s.mul(t);

        Matrix3x3f f = new Matrix3x3f(new double[][]{
                {50,30,20},
                {30,30,40},
                {20,30,20},
        });

        f = f.mul(r);
        System.out.println(f);

    }
}
