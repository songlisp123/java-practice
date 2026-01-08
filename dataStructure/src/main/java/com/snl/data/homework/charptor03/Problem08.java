package com.snl.data.homework.charptor03;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Problem08<T> {

    private int modCount = 0;
    private int size;

    private Node head;
    private Node TOP;

    class Node {
        private  T data;
        private Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    public Problem08() {
        clear();
    }

    /**
     * 添加到链表末端
     * @param t 该节点的数据
     */
    public void add(T t) {
        if (t == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (contains(t)) {
            throw new IllegalArgumentException("已经存在该元素");
        }
        Node o = TOP;
        Node p = new Node(t,null);
        if ( o == head) {
            head.next = p;
        }else {
            o.next = p;
        }
        TOP = p;
        size++;
        modCount++;
    }

    /**
     * 插入到指定索引
     * @param index 索引
     * @param t 参数类型
     */
//    public void add(int index,T t) {
//        if (index < 0 || index > size()) {
//            throw new IndexOutOfBoundsException("索引超出边界");
//        }
//        Node prev = getNode(index);
//        if (prev == TOP)
//            add(t);
//        else
//            addAfter(prev,t);
//    }

    private void addAfter(Node node, T t) {
        Node p = new Node(t,node.next);
        node.next = p;
        size++;
        modCount++;
    }

    /**
     * 移除给定的项
     * @param isLast 布尔标志，{@code true}表示从尾端开始移除，{@code false} 表示从头开始移除
     * @return 移除的项目
     */
    public T remove(boolean isLast) {
        if (isLast)
            return removeLast();
        return removeFirst();

    }

    /**
     * 移除包含数据为t的节点,该移除使用{@code Objects.equals(x,y)}计算两者是否相等
     * 该实现使用迭代器实现，目前来说时间是{@code O(N²)}
     * 现在是简单的O（N）
     * 如果换一种版本使用可比较接口如何？
     * @param t 匹配的数据项
     */
    public void remove(T t) {
        var i = iterator();
        while (i.hasNext()) {
            T next = i.next();
//            if (Objects.equals(t,next)) {
//                i.remove();
//                break;
//            }
            /*
            这是一个模拟的使用可比较的接口，不过实体类T应该实现compare接口
             */
            if (((Comparable<T>)t).compareTo(next) == 0)
                i.remove();
        }
    }

    private T removeFirst() {
        Node p = head.next;
        return unLinkFirst(p);
    }

    private T unLinkFirst(Node p) {
        if (p == null) {
            throw new NoSuchElementException("暂无元素");
        }
        Node t = p.next;
        T data = p.data;
        p.data = null;
        p.next = null;
        head.next = t;
        size--;
        modCount++;
        return data;
    }

    private T removeLast() {
        Node p = TOP;
        if (p == null) {
            throw new NoSuchElementException("暂无元素要更改");
        }
        return unLinkLast(p);
    }

    //TODO 第10题
    /**
     * 移除给定集合中匹配的所以项，使用{@code x.compareTo(y)}方法比较对象
     * @param items 给定要移除的项目项
     * @apiNote 也许会有更简单的算法操作，该算法实现是低效的
     * @implNote 该实现具有<code><b>O(N²)</b><code/>运行时间
     */
    public void removeAll(Iterable<? extends T> items) {
        var i = iterator();
        Iterator<? extends T> iterator;
        while (i.hasNext()) {
            iterator = items.iterator();
            T next = i.next();
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (item.equals(next)) {
                    i.remove();
                    /*
                    不建议这个操作，对于链表来说，这是一个o1操作
                    但是对于列表来说，这是一个on操作
                    iterator.remove();
                     */
                    break;
                }
            }
        }
    }

    private T unLinkLast(Node p) {
        if (head == p) {
            throw new NoSuchElementException("暂无元素要移除");
        }
        Node pre = getNode(size - 1);
        T data = p.data;
        p.data = null;
        pre.next = null;
        TOP = pre;
        size--;
        modCount++;
        return data;
    }

    private Node getNode(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("索引超出边界");
        }
        Node p =head.next;
        for (int i = 1;i<index;i++)
            p = p.next;
        System.out.println("p = " + p.data);
        return p;
    }

    public void  clear() {
        head = new Node(null,null);
        TOP = head;
        size = 0;
        modCount ++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void  show() {
        var  i = iterator();
        while (i.hasNext()) {
            T next = i.next();
//            System.out.println("next = " + next);

            if (next == null) System.out.print("");
            else {
                if (i.hasNext())
                    System.out.print(next+"-->");
                else
                    System.out.print(next+"");
            }
        }
        System.out.println();
    }

    public boolean contains(T t) {
        var i = iterator();
        while (i.hasNext()) {
            T next = i.next();
            if (Objects.equals(next,t)) return true;
        }
        return false;
    }

    public Iterator<T> iterator() {
        return new LinkIterator();
    }

    /**
     * 获取最后一个节点的数据
     * @return 该节点的数据
     */
    public T getLast() {
        if (TOP == head) {
            throw new NoSuchElementException("暂无跟多元素");
        }
        return TOP.data;
    }

    /**
     * 获取第一个节点的数据
     * @return 第一个节点的数据,如果为空链表，则为null
     */
    public T getFirst() {
        return head.next.data;
    }

    /**
     * 节点会乱难受😣
     */
//    public void sort() {
//        Node p = head.next;
//        Node skip = p;
//        Node prev = skip;
//        for (int i=0;i<size();i++) {
//            prev = skip;
//            skip = p;
//            System.out.println("p.data = " + p.data);
//            p = p.next;
//
//            if (judge(skip,p)) swap(prev,skip,p);
//        }
//    }
//
//    private boolean judge(Node skip, Node p) {
//        return ((Comparable<T>)skip.data).compareTo(p.data) > 0;
//    }

    public void swap(int index0,int index1) {


    }

    public void swap(Node skip,Node p) {
        Node next = p.next;
        Node o = TOP;
        head.next = p;
        skip.next = next;
        p.next = skip;
        modCount++;
    }

    public void swap(Node prev,Node skip,Node p) {
        Node next = p.next;
        Node o = TOP;
        prev.next = p;
        skip.next = next;
        p.next = skip;
        if (p == o) {
            TOP = skip;
        }
        modCount++;
    }

    class LinkIterator implements Iterator<T> {

        private int excepted = modCount;
        private Node currentNode = head.next;
        private Node skipNode = head;
        private Node previous = head;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (excepted != modCount) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("暂无更多元素");
            }
            T data = currentNode.data;
            previous = skipNode;
            skipNode = currentNode;
            currentNode = currentNode.next;
            okToRemove = true;
            return data;
        }

        @Override
        public void remove() {
            if (modCount != excepted) {
                throw new ConcurrentModificationException("并发修改异常");
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            Problem08.this.removeAfter(previous);
            excepted++;
            okToRemove = false;
        }
    }

    private void removeAfter(Node node) {
        Node n = node.next;
        node.next = n.next;
        if (n == TOP) TOP = node;
        size--;
        modCount++;
    }

}
