package com.snl.data.homework.charptor03;

public class TestProblem19 {
    public static void main(String[] args) {
        var s = new  Problem19<Integer>();
        Integer tail ;
        Integer head;
        Integer removed;
//        s.addFirst(12);
//        s.addFirst(20);
//        s.addFirst(52);
//        s.addFirst(98);

        s.addLast(25);
        s.addFirst(98);
        s.addLast(30);
        s.addFirst(40);
        s.addLast(96);

//        s.show();
//        tail = s.getTail();
//        System.out.println("tail = " + tail);
//        head = s.getHead();
//        System.out.println("head = " + head);
//        removed = s.removeLast();
//        System.out.println("removed = " + removed);
//        s.show();
//        removed = s.removeFirst();
//        System.out.println("removed = " + removed);
//        s.show();

        var p20 = new Problem20<Integer>();
        p20.addFirst(12);
        p20.addFirst(30);
        p20.addFirst(30);
        p20.addFirst(10);

        p20.addFirst(20);
        p20.addFirst(30);
        p20.addFirst(30);
        p20.addFirst(30);
        p20.addFirst(26);

        p20.show();

        boolean remove = p20.remove(30);
        System.out.println("remove = " + remove);
        p20.remove(30);
        p20.remove(30);
        p20.remove(30);
        p20.remove(30);
        p20.remove(30);
        p20.show();

    }
}
