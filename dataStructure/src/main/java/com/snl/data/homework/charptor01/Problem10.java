package com.snl.data.homework.charptor01;

public class Problem10 {
    public static void main(String[] args) {
        long f = 1;
        for (int i=0;i<100;i++) {
            f = f * 2;
        }
        System.out.println("f = " + f);
        System.out.println(f % 5);
    }
}
