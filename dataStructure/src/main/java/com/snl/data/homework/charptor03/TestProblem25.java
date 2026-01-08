package com.snl.data.homework.charptor03;

public class TestProblem25 {
    public static void main(String[] args) {
        var s = new Problem25<Integer>();
        s.push(10);
        s.push(20);
        s.push(35);

        Integer min = s.findMin();
        System.out.println("min = " + min);
    }
}
