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
        v = t.mul(v2);
        System.out.println("v = " + v);

        v = r.mul(v2);
        System.out.println("v = " + v);

        v = r.mul(t).mul(v2);
        System.out.println("v = " + v);

        v = t.mul(r).mul(v2);
        System.out.println("v = " + v);
    }
}
