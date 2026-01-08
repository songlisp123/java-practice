package com.snl.data.queue;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class LinkedQueueImplement<T extends Comparable<? super T>> extends AbstractQueue<T> {

    private int size;
    private int modCount;
    private Node head;
    private Node tail;

    class Node {
        private T data;
        private Node prev;
        private Node next;

        public Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * 空构造器
     */
    public LinkedQueueImplement() {
        size = 0;
        modCount = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * 内部方法移除头部节点
     * @return 移除头部节点
     */
    private T removeFirst() {
        Node h = head;
        Node next = head.next;
        T data = h.data;
        h.data = null;
        h.next = null;
        head = next;
        if (next == null)
        {
            //此时只有一个节点
            tail = null;
        }else {
            //或者……
            next.prev = null;
        }
        size--;
        modCount++;
        return data;
    }

    /**
     * 内部方法判断队列是否为空
     */
    private void  checkIsEmpty() {
        if (isEmpty())
            throw new NoSuchElementException("暂无更多节点");
    }


    /**
     * 将指定元素插入到队列
     * @param t 参数元素
     * @return 插入成功 {@code true} ; 插入失败返回 {@code false}
     */
    public boolean add(T t) {
        if (t == null)
            throw new NullPointerException("空指针异常");
        Node last = tail;
        Node aNode = new Node(t,tail,null);
        tail = aNode;
        if (last == null)
        {
            //第一次插入，分配节点
            head = aNode;
        }else {
            //否则……
            last.next = aNode;
        }
        size++;
        modCount++;
        return true;
    }

    /**
     * 判断队列是否为空
     * @return 队列为空 {@code true} ; {@code false} 队列不为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 插入实现，在链表中相当于add方法
     * @param t 参数
     * @return 插入成功 {@code true} ; 插入失败返回 {@code false}
     */
    public boolean offer(T t) {
        return add(t);
    }

    /**
     * 取出并移出此队列的头部，如果队列为空，返回null;
     * @return 队列头部元素
     */
    public T poll() {
        if (isEmpty())
            return null;
        return removeFirst();
    }

    /**
     * 此方法与poll方法相同，只不过抛出异常
     * @return 队列头部元素
     */
    public T remove() {
        checkIsEmpty();
        return removeFirst();
    }

    /**
     * 获取但并不移除队列的头部。此方法将会在队列为空的时候抛出异常
     * @return 头部节点元素值
     */
    public T element() {
        checkIsEmpty();
        return head.data;
    }

    /**
     * 获取但不移除头部节点，如果队列为空的时候则为null
     * @return 头部节点数据
     */
    public T peek() {
        if (isEmpty())
            return null;
        return head.data;
    }

    /**
     * 清空队列
     */
    public void clear() {
        if (isEmpty())
            return;
        Node h , next ;
        for (h = head;h != null;) {
            next = h.next;
            h.data = null;
            h.prev = null;
            h.next = null;
            h = next;
        }
        head = tail = null;
        size = 0;
        modCount++;
    }

    /**
     * 不需要移除节点操作
     */
    class ListIterator implements Iterator<T> {

        private Node node = head;
        int exceptedCount = modCount;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("暂无跟多元素");
            }
            if (exceptedCount != modCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            T data = node.data;
            node = node.next;
            return data;
        }
    }

}
