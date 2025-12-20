package com.snl.data.linkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 普通的双向链表实现
 * @param <T> 泛型参数
 */
public class MyLinedListImplement<T> implements List<T> {

    /**
     * 当前链表的数据大小
     */
    private int size;

    /**
     * 记录当前数据结构被修改的次数,默认为0,迭代器会以此属性为基础构造
     */
    private int modCount = 0;

    /**
     * 节点头部,数据为null
     */
    private Node beginMarker;

    /**
     * 尾部节点,数据为null
     */
    private Node endMarker;

    public MyLinedListImplement() {
        clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(T t) {
        add(size(),t);
    }

    @Override
    public void add(int index, T t) {
        addBefore(getNode(index,0,size()),t);
    }

    @Override
    public T remove(int index) {
        return remove(getNode(index));
    }

    public T remove(Node p) {
        p.prev.next = p.next;
        p.next.prev = p.prev;
        this.size--;
        modCount++;
        return p.data;
    }

    @Override
    public boolean contains(T t) {
        var iterator = iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (Objects.equals(next,t)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public T get(int index) {
        Node node = getNode(index);
        return node.data;
    }

    @Override
    public void set(int index, T t) {
        Node node = getNode(index);
        node.data = t;
    }

    @Override
    public void clear() {
        beginMarker = new Node(null,null,null);
        endMarker = new Node(null,beginMarker,null);
        beginMarker.next = endMarker;
        size = 0;
        modCount++;
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private void addBefore(Node p,T t) {
        Node newNode = new Node(t,p.prev,p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        this.size++;
        modCount++;
    }

    /**
     * 为了保证索引不越界，必须确保index位于【0~size-1】中。
     * @param index 当前节点的索引
     * @return  {@code node}当找到改节点的时候，否则返回{@code null}
     */
    private Node getNode(int index) {
        return getNode(index,0,size() - 1);
    }

    private Node getNode(int index,int lower,int higher) {
        Node p;
        if (index < lower|| index > higher) {
            throw new IndexOutOfBoundsException("索引越界");
        }
        if (index < size() /2 ){
            //左半部分
            p = beginMarker.next;
            for (int i =0;i<index;i++)
                p = p.next;
        }else {
            //右半部分
            p = endMarker;
            for (int i=size();i>index;i--)
                p = p.prev;
        }
        return p;
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

    class LinkedListIterator implements Iterator<T> {

        private int exceptedCount = modCount;
        private Node current = beginMarker.next;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return current != endMarker;
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
            MyLinedListImplement.this.remove(current.prev);
            exceptedCount++;
            okToRemove = false;
        }
    }

    /**
     * 重新排序索引
     * @param index0 1
     * @param index1 2
     */
    public void swap(int index0,int index1) {
        Node node = getNode(index0);
        Node node1 = getNode(index1);

        node1.prev = node.prev;
        node.prev.next = node1;
        node.next = node1.next;
        node1.next.prev = node;

        node1.next = node;
        node.prev = node1;

        modCount++;

    }
}
