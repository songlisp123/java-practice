package com.snl.data.array;

public class StartDemo {
    public static void main(String[] args) {
        var demo = new HasReplicateArrayDemo<Integer>(5);
        demo.show();

        demo.add(12);
        demo.add(45);
        demo.add(56);
        demo.show();

        var item = demo.remove();
        System.out.println("item = " + item);

        demo.add(52);
        demo.show();

        boolean search = demo.search(52);
        System.out.println("search = " + search);

        Integer item2 = demo.remove(1);
        System.out.println("item2 = " + item2);
        demo.show();

        demo.remove(1);
        demo.show();

//        demo.remove();
        demo.remove();
        demo.show();

        demo.add(10);
        demo.add(30);
        demo.add(25);
        demo.add(32);
        demo.show();

        Integer remove = demo.remove();
//        Integer remove1 = demo.remove(3);
        System.out.println("remove = " + remove);
        demo.show();

        demo.add(1,25);
        demo.show();

        demo.add(38);
        demo.show();

        long length = demo.length();
        System.out.println("length = " + length);

        Integer modify = demo.modify(2, 87);
        System.out.println("modify = " + modify);
        demo.show();

    }
}
