package com.snl.data.homework.charptor03;

import java.util.Arrays;

public class Problem21<T> {
    /**
     * 数据模型索引，默认为-1
     */
    private int topOfStack = -1;
    /**
     * 底层数据模型
     */
    private T[] data;

    public Problem21() {
        data = (T[]) new Object[1];
    }

    public boolean isEmpty() {
        return topOfStack == -1;
    }

    public void push(T t) {
        topOfStack++;
        data = Arrays.copyOf(data,topOfStack + 1);
        data[topOfStack] = t;
    }

    public T pop() {
        T datum = data[topOfStack];
        data = Arrays.copyOf(data,topOfStack);
        topOfStack--;
        return datum;
    }
}
