package com.snl.data.linkedList;

import java.time.LocalDateTime;
import java.util.Iterator;

public class StartApp {
    public static void main(String[] args) {
//        var link = new MyLinedListImplement<Integer>();
//
//        link.add(12);
//        link.add(18);
//        link.add(25);
//        link.add(36);
//        link.add(89);
//        link.show();
//
//        var iterator = link.iterator();
//        while (iterator.hasNext()) {
//            var next = iterator.next();
//            if (next == 36) {
//                iterator.remove();
//                break;
//            }
//        }
//
//        link.show();
//        boolean contains = link.contains(25);
//        System.out.println("contains = " + contains);
        /**
         * 以上是基本类型的,但是对于java类呢?
         */

        var link = new MyLinedListImplement<StudentDemo>();
        StudentDemo zsCopy;
        var zs = new StudentDemo(12.5, LocalDateTime.now(), 25, "张三");
        zsCopy = zs;
        var ls = new StudentDemo(12.5, LocalDateTime.now(), 25, "李四");
        var ww = new StudentDemo(12.5, LocalDateTime.now(), 25, "王五");
        var zl = new StudentDemo(12.5, LocalDateTime.now(), 25, "赵六");
        link.add(ls);
        link.add(zs);
        link.add(ww);
        link.add(zsCopy);
        link.show();

        boolean contains = link.contains(zsCopy);
        System.out.println("contains = " + contains);

        link.set(1,ls);
        link.show();

        int size = link.size();
        System.out.println("size = " + size);
        link.swap(1,2);
        link.show();


//        var s = new SingleLinkedListImplement<StudentDemo>();
//        s.add(zs);
//        s.add(ls);
//        s.add(ww);
//        s.add(zl);
//        s.show();
//
//        StudentDemo remove = s.remove(1);
//        System.out.println("remove = " + remove);
//
//        s.show();
//
//        int size = s.size();
//        System.out.println("size = " + size);
//
//        s.add(2,zl);
//        s.show();
//
//        contains = s.contains(zl);
//        System.out.println("contains = " + contains);
//
//        s.swap(2,4);
//        s.show();
    }
}
