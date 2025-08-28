package com.apress.bgn.char005.colletion;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDateTime;

public class CollectionBasicDemo {
    public static void main(String[] args) {

        LocalDateTime today = LocalDateTime.now();
        List a = new ArrayList();
        a.add("字符串测试");

        a.add(Integer.getInteger("20"));
        System.out.println(Integer.valueOf(23));
        a.add(Integer.valueOf("250"));
        a.add(today.toLocalDate());
        a.add(today.toLocalTime());

        System.out.println(a);

        for (Object obj : a) {
            if (obj instanceof String){
                System.out.println("字符串对象："+obj);
            } else if (obj instanceof Integer) {
                Integer i = (Integer) obj;
                System.out.println("integer对象:"+obj.toString());
            } else if (obj instanceof LocalDate) {
                System.out.println("LocalDate对象:"+obj);
            } else if (obj instanceof LocalTime) {
                System.out.println("localtime对象："+obj);
            } else {
                System.out.println("未知的数据类型！");
            }
        }
    }
}
