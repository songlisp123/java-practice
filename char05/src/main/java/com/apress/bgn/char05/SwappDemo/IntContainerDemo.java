package com.apress.bgn.char05.SwappDemo;

public class IntContainerDemo {
    public static void main(String[] args) {
        IntContainer a = new IntContainer(45);
        IntContainer b = new IntContainer(42);
        System.out.println("交换前：");
        System.out.println("a="+a.getValue());
        System.out.println("b="+b.getValue());
        swap(a,b);

        System.out.println("交换后：");
        System.out.println("a="+a.getValue());
        System.out.println("b="+b.getValue());
    }

    static void swap(IntContainer x,IntContainer y) {
        IntContainer temp = x;
        x = y;
        y = temp;
    }
}


  //   交换前：
  //   a=45
  //   b=42
  //   交换后：
  //   a=45
  //   b=42
    //为什么会发生这种情况？
//因为java交换的是值，而不是引用，谨记这一点！