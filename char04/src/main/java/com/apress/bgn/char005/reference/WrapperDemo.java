package com.apress.bgn.char005.reference;

//import javax.swing.*;

public class WrapperDemo {
    public static void main(String[] args) {
        //int的上边界
        int max = Integer.MAX_VALUE;
        System.out.println(max);

        //自动装相
        int pmax = max;

        //自动装相 int -> interger
        Integer i0 = 10;

        //创建原始类型的方法
        //若字符串不是数字，则抛出异常
        int i1 = Integer.parseInt("11");
        System.out.println(i1);
        int i2 = Integer.valueOf("13");
        System.out.println(i2);


        //将整数转换为string
        String s0 = Integer.toString(1326);

        //创建数字的二进制表示字符串
        String s1 = Integer.toBinaryString(56);
        System.out.println(s1);

        //创建数字的八进制字符串
        String s2 = Integer.toOctalString(56);
        System.out.println(s2);

        //创建数字的十六进制
        String s3 = Integer.toHexString(256815);
        System.out.println(s3);

        // 将整数类型转为浮点类型
        float f = Integer.valueOf(23).floatValue();
        System.out.println("f="+f);

        //
        Integer i4 = Integer.parseUnsignedInt("15");
        System.out.println("i4="+i4);


    }
}
