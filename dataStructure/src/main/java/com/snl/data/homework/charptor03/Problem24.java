package com.snl.data.homework.charptor03;

import java.util.NoSuchElementException;

public class Problem24<T> {
    private T[] data;
    private int leftTop = -1;
    private int rightTop;
    private int size;

    public Problem24(int size) {
        data = (T[]) new Object[size];
        rightTop = size;
        this.size = size;
    }

    public void leftPush(T t) {
        //是先插入数据还是先移动索引？
        leftTop++;
        data[leftTop] = t;
        //判断栈顶是否重叠？
        if (isPushed())
            throw new StackOverflowError("栈溢出");
    }

    public void rightPush(T t) {
        rightTop--;
        data[rightTop] = t;
        if (isPushed())
            throw new StackOverflowError("栈溢出");

    }

    public T leftPop() {
        if (leftTop == -1)
            throw new NoSuchElementException("暂无该元素");
        T datum = data[leftTop];
        data[leftTop] = null;
        leftTop--;
        return datum;
    }

    public T rightPop () {
        if (rightTop >= size())
            throw new NoSuchElementException("暂无该元素");
        T datum = data[rightTop];
        data[rightTop] = null;
        rightTop++;
        return datum;
    }

    public int size() {
        return size;
    }

    private boolean isPushed() {
        if (rightTop == size)
            return leftTop>=size;
        if (leftTop == -1)
            return rightTop < 0;
        return leftTop == rightTop;
    }

}
