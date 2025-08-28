package com.apress.bgn.char006.intefaces;

import java.util.Arrays;
import java.util.Comparator;

//这个程序是比较comparator接口的
public class comparator {
    public static void main(String[] args) {
        var len = new lengthComputor();
        //未调用自定义比较器的输出
        String[] s = new String[]{"as","詹姆斯","王五新是","老六老ask"};
        Arrays.sort(s);
        Arrays.stream(s).forEach(e -> System.out.println(e));
        System.out.println();
        //使用自定义比较器后
        Arrays.sort(s,len);
        Arrays.stream(s).forEach(e -> System.out.println(e));

        //当然，有的时候我们想要按照字母大小写来排序对象


    }
}


class lengthComputor implements Comparator<String> {
    @Override
    public int compare(String first,String second) {
        return first.length() - second.length();
    }
}

/*
比较前：
as
王五新是
老六老ask
詹姆斯

比较后：
as
詹姆斯
王五新是
老六老ask

可以看见输出后字符按照长度排序！
 */