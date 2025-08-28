package com.apress.bgn.char05.ArrayDemo;


//import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
public class arraysListDemo {
    public static void main(String[] args) {
        //java1.8的时候引用了范型操作，我们可以通过在声明类型的时候
        //通过限定类型来创建数组列表。当添加不一样的数据类型的时候
        //抛出类型不兼容错误
        //下方是一个限定为字符串的list应用数据类型
        List<String> sl = new ArrayList<>();
        sl.add("123");
        sl.add("zheshi1");
        sl.add("这时我的母亲！");

        //一个传统的迭代方法
        //for (String e : sl) {
        //    System.out.println(e);
        //}


        //java1.8中为集合实现了iterable接口
        //这样你就可以在每个实例中调用foreach方法
        //类似于上面的for循环
        sl.forEach(i -> System.out.println(i));


        //ArrayList继承了List接口，默认接口内的方法是
        //public abstract
        //public 代表着对于所有继承list接口都可以访问
        //abstarct 代表着继承list接口的类必须提供具体实现

        //List接口内的add代码
        //boolean add(E e);

        //ArrayList类的实现方式
        //将元素添加到数组列表末尾
        //public boolean add(E e) {
        //    modCount++;
        //    add(e, elementData, size);
        //    return true;
        //}

        //类ArrayList实现了List接口
        //public class ArrayListDemo<E> extends AbstractList<E>
        //        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
        //

        //

    }
}
