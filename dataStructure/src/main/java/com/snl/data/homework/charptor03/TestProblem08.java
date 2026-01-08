package com.snl.data.homework.charptor03;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestProblem08 extends JPanel {

    public static void main(String[] args) {
        var p8 = new Problem08<Integer>();
        p8.add(0);
        p8.add(10);
        p8.add(12);
        p8.add(2);
        p8.add(3);
        p8.add(5);
        p8.add(8);
        p8.add(9);
        p8.add(71);
        p8.add(20);

        p8.show();

//        Object remove = p8.remove();
//        System.out.println("remove = " + remove);
//        remove = p8.remove();
//        System.out.println("remove = " + remove);
        int size = p8.size();
        System.out.println("size = " + size);

        Object remove = p8.remove(true);
        System.out.println("remove = " + remove);
        p8.show();

        remove = p8.remove(false);
        System.out.println("remove = " + remove);
        remove = p8.remove(true);
        System.out.println("remove = " + remove);

        p8.show();
        size = p8.size();
        System.out.println("size = " + size);

        var last = p8.getLast();
        System.out.println("last = " + last);

        var first = p8.getFirst();
        System.out.println("first = " + first);

        p8.remove(3);
        p8.show();

        var s = new Problem08<Student>();

        Student pop;
        var zf = new Student("张飞",26);
        var zy = new Student("赵云",36);
        var lb = new Student("刘备",26);
        var gy = new Student("关羽",42);
        var mc = new Student("马超",36);
        var jz = new Student("贱人",26);
        var s1 = new Student("我",30);
        var s2 = new Student("你",78);
        var s3 = new Student("他",41);

        List<Student> iterable = new ArrayList<>();
        iterable.add(zf);
        iterable.add(s1);
        iterable.add(zy);
        iterable.add(mc);


        s.add(zf);
        s.add(s2);
        s.add(zy);
        s.add(lb);
        s.add(gy);
        s.add(mc);

        s.show();

        s.removeAll(iterable);

        s.show();



//        s.remove(zf);

//        s.show();
//
//        s.add(jz);
//
//
//        s.show();
//
//        s.show();
//
//        Student sf = s.getFirst();
//        System.out.println("sf = " + sf);
//
//        Student sl = s.getLast();
//        System.out.println("sl = " + sl);
//
//        s.add(s1);
//        s.add(s2);
//        s.add(s3);
//
//        s.show();
//
//        s.swap(1,2);
//
//        s.show();
//        System.out.println("sl = " + sl);




    }

}
