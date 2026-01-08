package com.snl.data.homework.charptor03;

import java.util.NoSuchElementException;

/**
 * 该类与维护头部和尾部节点的不同点是：
 * 我们必须维护头部节点和尾部节点动态性的
 * 这两个节点作为哨兵监控当前首节点和尾节点
 * @param <T>
 */
public class Problem19<T> {
    /**
     * 当前链表的数据大小
     */
    private int size;

    /**
     * 记录当前数据结构被修改的次数,默认为0,迭代器会以此属性为基础构造
     */
    private int modCount = 0;

    private Node head;
    private Node tail;

    private class Node {
        private T data;
        private Node prev;
        private Node next;

        public Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public Problem19() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addFirst(T t) {
        Node h = head;
        if (h == null) {
            head = new Node(t,null,null);
            tail = head;
        }
        else {
            Node p  = new Node(t,null,head);
            h.prev = p;
            head = p;
        }
        size++;
        modCount++;
    }

    public void addLast(T t) {
        Node o = tail;
        if (o==null) {
            tail = new Node(t,null,null);
            head = tail;
        }else {
            Node p = new Node(t,tail,null);
            o.next = p;
            tail = p;
        }

        size++;
        modCount++;
    }

    public T getHead() {
        return head.data;
    }

    public T getTail() {
        return tail.data;
    }

    public T removeLast() {
        Node o = tail;
        if (o == null) {
            throw new NoSuchElementException("暂无更多元素");
        }
        T data = tail.data;
        tail  = o.prev;
        o.data = null;
        o.prev.next = null;
        size--;
        modCount++;
        return data;
    }

    public T removeFirst() {
        Node h = head;
        if (h == null) {
            throw new NoSuchElementException("暂无更多元素");
        }
        T data = h.data;
        h.next.prev = null;
        h.data = null;
        head = h.next;
        size--;
        modCount++;
        return data;
    }

    public void show() {
        Node p = head;
        for (int i=0;i<size;i++) {
            if (p.next == null)
                System.out.print(p.data);
            else
                System.out.print(p.data+"-->");

            p = p.next;
        }
        System.out.println();
    }

    public void clear() {
        size = 0;
        modCount++;
        head = null;
        tail = null;
    }
}
