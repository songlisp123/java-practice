package com.snl.data.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 双向链表对于每个操作都是O（1）的
 * @param <T> 泛型参数
 */
public class LinkedListImplementOfDouble<T> {

    private int size;
    private Node head;
    private Node tail;

    public LinkedListImplementOfDouble() {
        head = new Node(null,null,null);
        tail = new Node(null,head,null);
        head.next = tail;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void push(T t) {
        Node p = new Node(t,head,head.next);
        head.next.prev = p;
        head.next = p;
        size++;
    }

    public T pop() {
        if (size == 0) {
            throw new NoSuchElementException("暂无元素");
        }
        T data = tail.prev.data;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        size--;
        return data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<T> iterator() {
        return new ListIterator();
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
