package com.snl.data.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 对于单向列表有两个操作我无法实现O（1）的运行时间，为什么？
 * @param <T> 泛型参数
 */
public class LinkedImplementOfSingle<T> {

    /**
     * 这是队列中等待时间最长的节点
     */
    private Node head;
    private Node tail;
    private int size;

    class Node {
        private T data;
        private Node next;
        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    public LinkedImplementOfSingle() {
        head = new Node(null,null);
        tail = new Node(null,null);
        head.next = tail;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void push(T t) {
        head.next = new Node(t,head.next);
        size++;
    }

    public T pop() {
        if (size == 0){
            throw new NoSuchElementException("暂未存在该元素");
        }
        /**
         * 以下两个方法的运行时间是O（N）的，
         * 为什么书上说对于任何操作都可能在O（1）时间运行？
         * 链表和数组实现都为每个操作提供快速的 O(1) 运行时间
         */
        Node longest = getNode();
        Node larggerNode = getLarggerNode();
        larggerNode.next = tail;
        size--;
        return longest.data;
    }


    public boolean isEmpty() {
        return size == 0;
    }
    //单链接并不搞笑，为什么呢？因为单链接并不能保证最后一个元素他需要遍历所有的节点

    public Node getNode() {
        Node p = head;
        for (int i=0;i<size;i++) p = p.next;
        return p;
    }

    public Node getLarggerNode() {
        Node p = head;
        for (int i = 0;i<size-1;i++) p = p.next;
        return p;
    }

    public Iterator<T> iterator() {
        return new ListIterator();
    }

    public boolean contains(T t) {
        var i = this.iterator();
        while (i.hasNext()) {
            T next = i.next();
            if (Objects.equals(next,t)) return true;
        }
        return false;
    }

    public void show() {
        var i = iterator();
        System.out.print("head-->");
        while (i.hasNext()) {
            T next = i.next();
            System.out.print(next + "-->");
        }
        System.out.println("tail");
    }

    class ListIterator implements Iterator<T> {

        private Node node = head.next;

        @Override
        public boolean hasNext() {
            return node != tail;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("暂无跟多元素");
            }
            T data = node.data;
            node = node.next;
            return data;
        }
    }
}
