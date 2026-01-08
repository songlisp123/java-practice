package com.snl.data.queue;

import com.snl.data.linkedList.StudentDemo;

import java.time.LocalDateTime;
import java.util.Iterator;

public class StartApp {
    public static void main(String[] args) {
        var s = new LinkedListImplementOfDouble<Integer>();
        var h = new LinkedQueueImplement<Integer>();
        var l = new LinkedImplementOfSingle<Integer>();
        var a = new ArrayQueue<Integer>(5);
        Integer pop;
        boolean isEmpty;
        Integer head;
        h.add(32);

        a.offer(25);
        head = a.element();
        System.out.println("head = " + head);
        a.offer(35);
        a.offer(45);
        a.offer(55);
        a.offer(85);

        a.show();



//        l.add(25);
//        l.add(85);
//        l.add(78);
//        l.add(56);
//
//        var iterator = l.iterator();
//
//        iterator.next();
//        iterator.next();
//        iterator.next();
//        iterator.remove();
//        iterator.remove();
//        iterator.remove();
//        iterator.remove();


//        l.offer(25);
//
//        pop = l.poll();
//        System.out.println("pop = " + pop);
//
//        l.add(56);
//
//        pop = l.poll();
//        System.out.println("pop = " + pop);
//        pop = h.remove();
//        System.out.println("pop = " + pop);
//
//        isEmpty = h.isEmpty();
//        System.out.println("isEmpty = " + isEmpty);

//        s.push(12);
//        s.push(20);
//        s.push(18);
//        s.push(15);
//        int size = s.size();
//        System.out.println("size = " + size);
//
//        s.show();
//
//        int inPop;
//        inPop = s.pop();
//        s.pop();
//        s.show();
//
//        s.push(25);
//        inPop = s.pop();
//        System.out.println("inPop = " + inPop);
//
//        var t = new LinkedImplementOfSingle<Integer>();
//        t.push(45);
//        t.push(25);
//        t.push(75);
//        t.show();
//
//        inPop = t.pop();
//        System.out.println("inPop = " + inPop);
//        t.show();
//        t.pop();
//        t.pop();
//        System.out.println("inPop = " + inPop);
//        t.show();
//
//        t.push(250);
//        t.show();
//
//        StudentDemo student;
//        StudentDemo zsCopy;
//        var zs = new StudentDemo(12.5, LocalDateTime.now(), 25, "张三");
//        zsCopy = zs;
//        zsCopy.setName("张三副本");
//        var ls = new StudentDemo(12.5, LocalDateTime.now(), 25, "李四");
//        var ww = new StudentDemo(12.5, LocalDateTime.now(), 25, "王五");
//
//        var p = new LinkedImplementOfSingle<StudentDemo>();
//        p.push(zs);
//        p.push(ls);
//        p.push(ww);
//
//        boolean contains = p.contains(zs);
//        System.out.println("contains = " + contains);
//
//        p.show();
//        student = p.pop();
//        System.out.println("student = " + student);
//
//        contains = p.contains(ls);
//        System.out.println("contains = " + contains);
//
//        boolean empty = p.isEmpty();
//        System.out.println("empty = " + empty);
//
//        p.pop();
//
//        student = p.pop();
//        System.out.println("student = " + student);
//
//        p.show();
//
//        empty = p.isEmpty();
//        System.out.println("empty = " + empty);
//
//        contains = p.contains(ww);
//        System.out.println("contains = " + contains);
    }
}
