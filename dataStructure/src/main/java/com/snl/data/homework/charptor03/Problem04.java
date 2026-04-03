package com.snl.data.homework.charptor03;

import com.snl.data.array.HasReplicateArrayDemo;

import java.util.ArrayList;
import java.util.random.RandomGenerator;

//TODO 4、5已经完成
public class Problem04 {
    public static void main(String[] args) {
        //暴力算法,给定条件
        var generator = RandomGenerator.getDefault();
        int N = 10;
        var l1 = new HasReplicateArrayDemo<Integer>(N);
        var l2 = new HasReplicateArrayDemo<Integer>(N);
        for (int i =0;i<N;i++) {
            l1.add(generator.nextInt(0,N));
            l2.add(generator.nextInt(0,N));
        }

        l1.sort();
        l2.sort();

        l1.show();
        l2.show();
        long start;
        long end;
        var l = new ArrayList<Integer>();

        start = System.currentTimeMillis();
        //TODO 简单解决方法，算法时间接近O(N²)
//        for (int i = 0;i<N;i++) {
//            Integer i1 = l2.get(i);
//            if (l1.search(i1)) l.add(i1);
//        }
        /*
        考虑到列表已经排序了这一事实，我们该怎么优化呢？
         */
        /**
         * 以下是一个O（N）的运行时间
         */
        int index1 = 0;
        int index2 = 0;
        int i2;
        int i1;
//        while (index1 < l1.length() &&  index2 < l2.length()) {
//            i2 = l2.get(index2);
//            i1 = l1.get(index1);
//            if (i1 > i2) {
//                index2++;
//            } else if (i2 == i1) {
//                l.add(i1);
//                index1++;
//                index2++;
//            }else {
//                index1++;
//            }
//        }
        //TODO 第五题合并两个已经排序的列表,与上面的基本一样
        while (index1 < l1.length() &&  index2 < l2.length()) {
            i2 = l2.get(index2);
            i1 = l1.get(index1);
            if (i1 > i2) {
                index2++;
                l.add(i2);
            } else if (i1 == i2) {
                l.add(i2);
                index1++;
                index2++;
            }else {
                l.add(i1);
                index1++;
            }
        }

        // 把剩余的直接拷贝进去
        while (index1 < l1.length()) {
            l.add(l1.get(index1++));
        }

        while (index2 < l2.length()) {
            l.add(l2.get(index2++));
        }


        end = System.currentTimeMillis();


        System.out.printf("运行时间：%d ms%n" , end-start);
        System.out.println("l = " + l);

        //问题5 的解决方法与上面相同，但是我们可以使用简单的算法实现这个问题

    }
}
