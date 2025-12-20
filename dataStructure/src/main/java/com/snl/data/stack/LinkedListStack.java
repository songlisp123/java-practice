package com.snl.data.stack;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedListStack<T> {

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

    protected int size;
    protected Node head;
    protected Node tail;
    protected int modCount = 0;

    public LinkedListStack() {
        head = new Node(null,null,null);
        tail = new Node(null,head,null);
        head.next = tail;
        size = 0;
        modCount ++;
    }

    public int size() {
        return size;
    }

    public void push(T t) {
        Node p = new Node(t,tail.prev,tail);
        tail.prev.next = p;
        tail.prev = p;
        size++;
        modCount++;
    }

    public T pop() {
        Node prev = tail.prev;
        if (Objects.equals(prev,head)) {
            throw new NoSuchElementException("没有元素要弹出");
        }
        return remove(tail.prev);
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

    private Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private T remove(Node p) {
        p.prev.next = p.next;
        p.next.prev = p.prev;
        this.size--;
        modCount++;
        return p.data;
    }

    class LinkedListIterator implements Iterator<T> {

        private int exceptedCount = modCount;
        private Node current = head.next;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public T next() {
            if (modCount != exceptedCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("暂无改元素异常");
            }
            var item = current.data;
            current = current.next;
            okToRemove = true;
            return item;
        }

        @Override
        public void remove() {
            if (modCount != exceptedCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            LinkedListStack.this.remove(current.prev);
            exceptedCount++;
            okToRemove = false;
        }
    }
}
