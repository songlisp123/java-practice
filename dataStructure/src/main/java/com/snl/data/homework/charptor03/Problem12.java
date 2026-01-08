package com.snl.data.homework.charptor03;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 这是一个有序链表的可能实现方式,关键的问题是：保持单链表有序。
 * 先从一个简单的例子排序：学生成绩
 * @param <T>
 */
public class Problem12<T extends Comparable<? super T>> {
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

    public Problem12() {
        clear();
    }

    /**
     * 添加到链表末端,这个实现有一点难受？因为我要比较学生的分数
     * @param t 该节点的数据
     */
    public void add(T t) {
        if (t == null) {
            throw new IllegalArgumentException("非法参数");
        }
        if (contains(t)) {
            throw new IllegalArgumentException("已经存在该元素");
        }
        /*
        该元素比前一个大还是小？
         */
        Node o = TOP;
        Node p = new Node(t,null);
        if ( o == head) {
            head.next = p;
        }else {

            //这是插入到末端版本
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
    public void add(int index,T t) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("索引超出边界");
        }
        Node prev = getNode(index);
        if (prev == TOP)
            add(t);
        else
            addAfter(prev,t);
    }

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
     * 采用comparable实现比较排序
     * @param t 匹配的数据项
     */
    public void remove(T t) {
        var i = iterator();
        while (i.hasNext()) {
            T next = i.next();
            if (t.compareTo(next) == 0)
                i.remove();
        }
    }

    /**
     * 移除当前节点，该方法只有在迭代器中实现
     * @param prev 即将移除的节点
     */
    private void removeAfter(Node prev) {
        Node target = prev.next;
        prev.next = target.next;
        if (target == TOP) TOP = prev;
        size--;
        modCount++;
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
        Node p =head;
        for (int i = 0;i<index;i++)
            p = p.next;
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
            Problem12.this.removeAfter(previous);
            excepted++;
            okToRemove = false;
        }
    }
}
