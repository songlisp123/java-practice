package com.apress.bgn.char006.Lambda;

import java.util.Comparator;
import java.util.Arrays;
public class lamda {
    public static void main(String[] args) {
        var s = new String[]{"赵云唱腔一战","马超牛鼻","关羽","黄忠飞鸟","张一的"};
        Comparator<String> cmp = (first,second) -> {
            return first.length() - second.length();
        };

        //按照字典排序前
        Arrays.sort(s);
        System.out.println(Arrays.toString(s));
        //按照长度排序
        Arrays.sort(s,cmp);
        System.out.println(Arrays.toString(s));
    }
}
