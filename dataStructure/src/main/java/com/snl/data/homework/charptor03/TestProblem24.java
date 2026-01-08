package com.snl.data.homework.charptor03;

public class TestProblem24 {
    public static void main(String[] args) {
        var s = new Problem24<Integer>(10);
        s.leftPush(20);
        s.leftPush(15);
        s.leftPush(12);
        s.leftPush(65);
        s.leftPush(14);

        s.rightPush(32);
        s.rightPush(32);
        s.rightPush(32);
        s.rightPush(32);
        s.rightPush(32);


        Integer i = s.leftPop();
        Integer i1 = s.rightPop();
        System.out.println("i = " + i);
        System.out.println("i1 = " + i1);



    }
}
