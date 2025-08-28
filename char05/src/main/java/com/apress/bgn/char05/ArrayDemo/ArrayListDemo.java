package com.apress.bgn.char05.ArrayDemo;

import java.util.ArrayList;
import java.util.Random;
public class ArrayListDemo {
    public static void main(String[] args) {
        //这是一个Arraylist的演示
        //生命一个数组列表
        ArrayList<Integer> arrayList = new ArrayList<>();
        Random arandom = new Random();
        int num = 0;
        //调用add方法
        //System.out.println(arrayList.size());
        //填充数组
        for (int i=0;i<100;i++) {
            int n = arandom.nextInt(100)+1;
            arrayList.add(n);
        }
        System.out.println(arrayList.size());
        for (int a:arrayList) {
            if (num<10) {
                System.out.printf(a+"\t");
                num++;
            } else {
                num = 0;
                System.out.println();
            }
        }
        System.out.println(arrayList.get(12));

    }
}
