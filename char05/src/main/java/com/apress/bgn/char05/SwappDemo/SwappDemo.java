package com.apress.bgn.char05.SwappDemo;

public class SwappDemo {
    public static void main(String[] args) {
        /**
         * 这是一个详细的说明，用来演示java虚拟机内存中
         * 基本数据结构的值的交换的实例
         */
        int a = 42;
        int b = 44;
        swapp(a,b);
        System.out.println("a="+a);
        System.out.println("b="+b);

        }
        static void swapp(int x , int y) {
            int temp = x;
            x = y;
            y = temp;
        }
}

