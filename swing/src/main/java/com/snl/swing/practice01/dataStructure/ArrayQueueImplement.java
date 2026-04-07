package com.snl.swing.practice01.dataStructure;

import com.snl.swing.practice01.entity.Sprite;

import java.util.AbstractQueue;
import java.util.Iterator;

public class ArrayQueueImplement<T extends Sprite> extends AbstractQueue<T> {

    private int nitems;
    private int front;
    private int end;
    private int maxSize;

    private T[] data;

    public ArrayQueueImplement(int size) {
        data = (T[]) new Sprite[size];
        this.maxSize = size;
        front = 0;
        end = -1;
        nitems = 0;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("操作异常");
    }

    @Override
    public int size() {
        return nitems;
    }

    @Override
    public boolean offer(T t) {
        if (isFull() || t == null)
            return false;
        if (end == maxSize - 1)
            end = -1;
        data[++end] = t;
        nitems++;
        return true;
    }

    @Override
    public T poll() {
        if (isEmpty())
            return null;
        if (front == maxSize)
            front = 0;
        T datum = data[front];
        data[front] = null;
        front++;
        nitems--;
        return datum;
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;
        return data[front];
    }

    public boolean isFull() {
        return nitems == maxSize;
    }

    @Override
    public boolean isEmpty() {
        return nitems == 0;
    }

}
