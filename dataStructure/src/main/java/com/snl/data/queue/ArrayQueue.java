package com.snl.data.queue;

import java.util.AbstractQueue;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayQueue<T> extends AbstractQueue<T> {

    private int maxSize;
    private int front;
    private int rear;
    private int items;
    private T[] data;
    private int modCount;

    public ArrayQueue(int size) {
        this.maxSize = size;
        data = (T[]) new Object[maxSize];
        front = 0;
        rear = -1;
        items = 0;
        modCount = 0;
    }


    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    @Override
    public int size() {
        return items;
    }

    @Override
    public boolean offer(T t) {
        //检查是否队列是否充满
        if (isFull() || t == null)
            return false;
        //不能插入null元素
        //否则判断是否尾部索引到达数组末端
        if (rear == maxSize - 1)
            rear = -1;
        data[++rear] = t;
        items++;
        modCount++;
        return true;
    }

    @Override
    public T poll() {
        if (isEmpty())
            return null;
        if (front == maxSize)
            front = 0;
        int head = front;
        T datum = data[head];
        data[head] = null;
        front++;
        items--;
        modCount++;
        return datum;
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;
        return data[front];
    }

    private boolean isFull() {
        return items == maxSize;
    }

    @Override
    public boolean isEmpty() {
        return items == 0;
    }

    /**
     * 不需要移除节点操作
     */
    class ListIterator implements Iterator<T> {
        int exceptedCount = modCount;
        int counts = 0;
        int head = front;

        @Override
        public boolean hasNext() {
            return counts < items;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("暂无跟多元素");
            }
            if (exceptedCount != modCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (head == maxSize)
                head = 0;
            T datum = data[head];
            head++;
            counts++;
            return datum;
        }
    }

    /*
    您应该显示从插入的第一个项目到最后一个项目的队列内容，
    而不要向查看者表明序列是否因围绕数组末尾而中断。注意，一个项目和没有项目显示正确，无论前面和后面
     */

    public void show() {
        int f = front;
        int c = 0;
        while (c < items)
        {
            System.out.println(data[f]);
            //否则……
            f++;
            if (f == maxSize) f = 0;
            c++;
        }
    }

}
