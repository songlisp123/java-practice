package com.snl.data.stack;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayStack<T> {
    private int topOfStack = -1;
    private T[] data;

    public ArrayStack() {
        data = (T[]) new Object[1];
    }

    public void  push(T t) {
        topOfStack++;
        data = Arrays.copyOf(data,topOfStack+1);
        data[topOfStack] = t;
    }

    public T pop() {
        if (topOfStack == -1) {
            throw new NoSuchElementException("暂未有剩余元素");
        }
        var  t = data[topOfStack];
        //关键是是否是截断还是赋值为null
        data = Arrays.copyOf(data,topOfStack);
        topOfStack--;
        return t;
    }

    public void show() {
        if (topOfStack == -1) {
            System.out.println("[ ]");
        }
        else
            Arrays.stream(data).forEach(System.out::println);
    }
    public int length() {
        return topOfStack+1;
    }

    public boolean isEmpty() {
        return topOfStack == -1;
    }

    @Override
    public String toString() {
        return "ArrayStack{" +
                "topOfStack=" + topOfStack +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
