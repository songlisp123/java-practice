package com.snl.data.stack;

import com.snl.data.linkedList.StudentDemo;

import java.time.LocalDateTime;

public class StartApp {
    public static void main(String[] args) {
        var s = new LinkedListStack<Integer>();

        var a = new StackImplementInDoubleLinked<Integer>();

        Integer pop;
        Integer distance;

        a.push(35);
        a.push(24);
        a.push(16);
        a.push(98);
        a.push(120);


        //测试边界情况
        a.pop();
        a.pop();
        a.pop();
        a.pop();
        a.pop();

        //测试刚插入
        a.push(25);

        Integer peek = a.peek();
        pop = a.pop();

        System.out.println("peek = " + peek);
        System.out.println("pop = " + pop);

        boolean isEmpty;
        isEmpty = a.isEmpty();

        System.out.println("isEmpty = " + isEmpty);

//        s.push(12);
//        s.push(16);
//        s.push(18);
//        s.push(25);
//
//        s.show();
//        Integer ps = s.pop();
//        System.out.println("pop = " + ps);
//        s.show();
//        s.pop();
//        ps = s.pop();
//        System.out.println("pop = " + ps);
//
//        s.show();
//        ps =s.pop();
//
//        System.out.println("pop = " + ps);
//
//        s.show();

//        var h  = new LinkedListStack<StudentDemo>();
//        StudentDemo zsCopy;
//        StudentDemo pop ;
//        var zs = new StudentDemo(12.5, LocalDateTime.now(), 25, "张三");
//        zsCopy = zs;
//        zsCopy.setName("张三副本");
//        var ls = new StudentDemo(12.5, LocalDateTime.now(), 25, "李四");
//        var ww = new StudentDemo(12.5, LocalDateTime.now(), 25, "王五");
//
//        h.push(zs);
//        h.push(ls);
//        h.push(ww);
//        h.push(zsCopy);
//
//        h.show();
//
//        pop = h.pop();
//        System.out.println("pop = " + pop);
//        pop= h.pop();
//        System.out.println("pop = " + pop);
//        pop =h.pop();
//        System.out.println("pop = " + pop);
//
//        pop = h.pop();
//        System.out.println("pop = " + pop);
//
//        h.pop();

//        var t = new ArrayStack<Integer>();
//        Integer pop;
//        t.push(12);
//        t.push(25);
//        t.push(35);
//        t.push(36);
//        t.show();
//
//        pop =t.pop();
//        System.out.println("pop = " + pop);
//        pop=t.pop();
//        System.out.println("pop = " + pop);
//        pop = t.pop();
//        System.out.println("pop = " + pop);
//        pop = t.pop();
//        System.out.println("pop = " + pop);
//        pop = t.pop();
//        System.out.println("pop = " + pop);

//        var p = new ArrayStack<StudentDemo>();
//        p.push(zs);
//        p.push(ls);
//        p.push(zsCopy);
//        p.push(ww);
//
//        p.show();
//
//        pop = p.pop();
//        System.out.println("pop = " + pop);
//
//        pop = p.pop();
//        System.out.println("pop = " + pop);
//
//        p.show();
//
//        pop= p.pop();
//        System.out.println("pop = " + pop);
//
//        p.show();
//
//        pop = p.pop();
//        System.out.println("pop = " + pop);
//
//        int length = p.length();
//        System.out.println("length = " + length);
//
//        p.show();
//
//        var t = new SingleLinkedListStack<StudentDemo>();
//
//        t.push(zs);
//        int length1;
//        t.push(ls);
//        t.push(ww);
//
//        t.show();
//
//        pop = t.pop();
//        System.out.println("pop = " + pop);
//
//        t.show();
//
//        pop = t.pop();
//        System.out.println("pop = " + pop);
//
//        t.pop();
//        length1 = t.length();
//        System.out.println("length1 = " + length1);
//
//        t.show();
    }
}
