package com.apress.bgn.char05.ArrayDemo;


import java.util.Arrays;
public class arrays {
    public static void main(String[] args) {
        int[] array = {4,2};
        System.out.println(Arrays.toString(array));
        //或者
        Arrays.stream(array).forEach(i -> System.out.println(i));
        Arrays.stream(array).forEach(System.out::println);
        Arrays.sort(array);
        Arrays.stream(array).forEach(System.out::println);

    }
}

//疑问一：