package com.snl.data.queue;

import java.util.*;

/**
 * 对于单向列表有两个操作我无法实现O（1）的运行时间，为什么？
 * @param <T> 泛型参数
 */
public class LinkedImplementOfSingle<T> extends AbstractQueue<T> {

    /**
     * 这是队列中等待时间最长的节点
     */
    private Node head;
    private Node tail;
    private int size;
    private int modCount;

    class Node {
        private T data;
        private Node next;
        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    public LinkedImplementOfSingle() {
        size = 0;
        modCount = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    /**
     * 内部方法，移除头部
     * @return 移除元素
     */
    private T removeFirst() {
        Node h = head;
        Node next = h.next;
        T data = h.data;
        h.data = null;
        h.next = null;
        head = next;
        if (next == null)
            //如果只存在一个节点
            tail = null;
        size--;
        modCount++;
        return data;
    }

    public int size() {
        return size;
    }

    public   boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 将指定元素插入到队列中，如果当前没有空间则抛出{@code IllegalStateException }异常
     * @param t 插入元素
     * @return 插入成为返回 {@code true} 否则为{@code false}
     */
    public boolean add(T t) {
        Node last = tail;
        Node aNode = new Node(t,null);
        tail = aNode;
        if (last == null)
            //插入第一个元素
            head = aNode;
        else {
            //否则……
            last.next = aNode;
        }
        size++;
        modCount++;
        return true;
    }

    /**
     * 将指定元素插入到队列中
     * @param t 插入元素
     * @return 插入成为返回 {@code true} 否则为{@code false}
     */
    public boolean offer(T t) {
        return add(t);
    }

    /**
     * 获取并移除此队列的头部。
     * @return 移除元素
     */
    public T remove() {
        if (isEmpty())
            throw new NoSuchElementException("暂无更多元素");
        return removeFirst();
    }


    /**
     * 获取并移除此队列的头部。
     * @return 移除元素
     */
    public T poll() {
        if (isEmpty())
            return null;
        return removeFirst();
    }

    /**
     * 获取但不移除此队列的头部
     * @return 头部元素
     */
    public T element() {
        if (isEmpty())
            throw new NoSuchElementException("暂无更多元素");
        return head.data;
    }

    /**
     *获取此队列的头部元素，但不移除它
     * @return 队列为空是返回 {@code null} 否则返回头部元素
     */
    public T peek() {
        if (isEmpty())
            return null;
        return head.data;
    }


    class ListIterator implements Iterator<T> {

        private Node node = head;
        private int exceptedCount = modCount;
        boolean toRemoved = false;
        int count = 0;
        int removeCount = 0;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            checkConcurrent();
            if (!hasNext()) {
                throw new NoSuchElementException("暂无跟多元素");
            }
            T data = node.data;
            node = node.next;
            toRemoved = true;
            count++;
            return data;
        }

        private void checkConcurrent() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
        }

        @Override
        public void remove() {
            checkConcurrent();
            if (!toRemoved)
                throw new IllegalStateException("非法操作");
            removeFirst();
            removeCount++;
            exceptedCount++;
            toRemoved  = removeCount != count;
        }
    }
}
