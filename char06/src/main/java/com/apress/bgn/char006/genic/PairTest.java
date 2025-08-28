package com.apress.bgn.char006.genic;

import java.time.LocalDate;

public class PairTest {
    public static void main(String[] args) {
        String[] str = {"Mary","has","a","little","lamb"};
        var p = ArrayAlgh.minmax(str);
        System.out.println("最小值："+p.getFirst());
        System.out.println("最大值："+p.getSecond());
        String s =ArrayAlgh.getMiddle(str);
        System.out.println("中间值是："+s);

        var i = ArrayAlgh.getMiddle(1,5,6,9,2,6,8,12,485);
        System.out.println("i="+i);
        var r = new Rectange[3];
        r[0] = new Rectange();
        r[1]= new Rectange(12,35);
        r[2]= new Rectange(20,24);

        try {
            var a = ArrayAlgh.min(r);
            System.out.println("最小值是:"+a);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        LocalDate[] birthDya = {
                LocalDate.of(1912,12,9),
                LocalDate.of(1815,12,9),
                LocalDate.of(1903,12,3),
                LocalDate.of(1910,6,22),
                LocalDate.of(1906,8,10),
        };

        Pair<LocalDate> b = ArrayAlgh.minmax(birthDya);
        System.out.println("最小值:"+b.getFirst());
        System.out.println("最大值："+b.getSecond());
    }

}


class ArrayAlgh {
    public static <T extends Comparable> Pair<T> minmax(T[] a) {
        if (a == null || a.length==0) return null;
        T min = a[0];
        T max = a[0];
        for (int i=1;i<a.length;i++) {
            if (min.compareTo(a[i])>0) min=a[i];
            if (max.compareTo(a[i])<0) max=a[i];
        }
        return new Pair<>(min,max);
    }
    public static <T> T getMiddle(T...a) {
        return  a[a.length/2];
    }

    public static <T extends Comparable> T min(T[] a) {
        if (a==null||a.length==0) return  null;
        T min = a[0];
        for (int i = 0; i < a.length; i++) {
            if (min.compareTo(a[i])>0) min = a[i];
        }
        return min;
    }
}