package com.snl.data.linkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 使用单向链表实现
 * @param <T>
 */
public class SingleLinkedListImplement<T> implements List<T> {

    private int size;
    private Node head;
    private Node tail;
    private int modCount = 0;

    public SingleLinkedListImplement(int size, Node head, Node tail) {
        this.size = size;
        this.head = head;
        this.tail = tail;
    }

    /**
     * 初始化链表
     */
    public SingleLinkedListImplement() {
        clear();
    }

    class Node {
        private T data;
        private Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * 对于size操作，应该在常数时间完成
     * @return 当前链表中的实际项目数量
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 对于add操作，默认实现为在链表的末节节点插入，因此操作时间为O(N)
     * 需要遍历每个节点
     * @param t 类型T的参数
     */
    @Override
    public void add(T t) {
        add(size(),t);
    }

    @Override
    public void add(int index, T t) {
        addAfter(getNode(index),t);
    }

    private void addAfter(Node node, T t) {
        Node p = new Node(t,node.next);
        node.next = p;
        size++;
        modCount++;
    }

    @Override
    public T remove(int index) {
        Node node = getNode(index);
        Node prev = getNode(index - 1);
        prev.next = node.next;
        size--;
        modCount++;
        return node.data;
    }

    @Override
    public boolean contains(T t) {
        var i = iterator();
        while (i.hasNext()) {
            T next = i.next();
            if (Objects.equals(t,next)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 这个和下面这个都是O(N) 运行时间
     * @param index 指向的索引
     * @return
     */
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

    /**
     * 必须确保high参数小于size
     * @param index 索引
     * @return node参数
     */
    private Node getNode(int index) {
        return getNode(index,0,size());
    }

    private Node getNode(int index,int lower,int higher) {
        if (index < lower|| index > higher) {
            throw new IndexOutOfBoundsException("索引越界");
        }
        Node p = head;
        for (int i = 0;i<index;i++)
            p = p.next;
        return p;
    }

    @Override
    public void clear() {
        head = new Node(null,null);
        tail = new Node(null,null);
        head.next = tail;
        this.size = 0;
        modCount++;
    }

    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private int indexOf(Node node) {
        Node p = head;
        for (int i = 0;i<size;i++)
        {
            p = p.next;
            if (Objects.equals(p,node)) return i;
        }
        throw new NoSuchElementException("暂未找到节点");
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

        private int exceptedCount = modCount;
        private Node currentNode = head.next;
        private boolean okToRemove = false;


        @Override
        public boolean hasNext() {
            return currentNode != tail;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("暂无更多项目");
            }
            T data = currentNode.data;
            currentNode  = currentNode.next;
            okToRemove = true;
            return data;
        }

        @Override
        public void remove() {
            if (modCount != exceptedCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            //这一句实现真的难受这是一个O(n)操作
            SingleLinkedListImplement.this.remove(indexOf(currentNode));
            exceptedCount++;
            okToRemove = false;
        }
    }

    /**
     * 这是一个简单的版本，用来交换相邻的两个元素
     * 这是一个极端的情况，需要index0+1 = index1
     * 如果你输入错了索引，则该将重新排列索引项并除去闭区间内的所有项目，除去项等于index1-index0-1
     * @param index0 左闭区间
     * @param index1 右闭区间
     */
    public void swap(int index0,int index1) {
        Node prev = getNode(index0);
        Node previous = getNode(index0 - 1);
        Node node = getNode(index1);

        prev.next = node.next;
        node.next = prev;
        previous.next = node;
        modCount++;
    }

    public void removeAll(Iterable<? extends T> items) {
        //首先需要判断是否为null
        if (Objects.isNull(items)) {
            return;
        }
        for (T t : items) {

        }
    }
}
