package com.apress.bgn.char005.colletion;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
public class GenericListDemo {
    public static void main(String[] args) {
        List<String> sl = new ArrayList<>();
        //sl = new LinkedList<String>();
        sl = List.of("3");
        sl.forEach(element -> System.out.println(element));
        System.out.println(sl.hashCode());
    }
}
