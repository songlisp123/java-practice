package com.snl.data.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleLinkedListStack<T>  {

    private int size;
    private Node head;

    private Node current;

    class Node {
        protected Node next;
        protected T data;

        public Node(Node next, T data) {
            this.next = next;
            this.data = data;
        }
    }

    public SingleLinkedListStack() {
        head = new Node(null,null);
        current = head;
        size = 0;
    }

    public void push(T t) {
        Node p = new Node(null,t);
        p.next = current;
        current = p;
        size++;
    }

    public T pop() {
        if (size == 0){
            throw new NoSuchElementException("暂未有元素");
        }
        T data = current.data;
        current = current.next;
        size--;
        return data;
    }

    public int length() {
        return size;
    }

    public Iterator<T> iterator() {
        return new IteratorImplement();
    }

    public void show() {
        var i = iterator();
        System.out.println("⬆️");
        while (i.hasNext()) {
            T next = i.next();
            System.out.println(next);
        }
    }

    class IteratorImplement implements Iterator<T> {

        private Node currentNode = current;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return currentNode != head;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("暂未有该元素");
            }
            var item = currentNode.data;
            currentNode = currentNode.next;
            okToRemove = true;
            return item;
         }

        @Override
        public void remove() {
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            okToRemove = false;
        }
    }
}
