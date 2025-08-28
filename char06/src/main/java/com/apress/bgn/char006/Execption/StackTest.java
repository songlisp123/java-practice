package com.apress.bgn.char006.Execption;

import java.util.Scanner;
import java.lang.StackWalker;
public class StackTest {
    public static void main(String[] args) {
        try (var in = new Scanner(System.in)) {
            System.out.println("输入n：");
            int n = in.nextInt();
            factorial(n);
        }
    }

    static int factorial(int n) {
        var walker = StackWalker.getInstance();
        int r;
        if (n <= 1) r=1;
        else r = n * factorial(n -1);
        System.out.println("返回值："+r);
        return r;
    }
}
