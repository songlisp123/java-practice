package com.snl.data.tree;

import java.util.NoSuchElementException;

public class stack {
    private int[] st;
    private int top = -1;
    private int size;

    public stack(int size) {
        st =  new int[size];
        this.size = size;
    }

    public int length() {
        return size;
    }

    public void push(int t) {
        if (this.top == size - 1)
            throw new StackOverflowError("栈溢出错误");
        this.st[++this.top] = t;
    }

    public int pop() {
        if (isEmpty())
            throw new NoSuchElementException("暂无元素");
        return this.st[this.top--];
    }

    public boolean isEmpty() {
        return this.top == -1;
    }
}
