package com.snl.data.stack;

import java.util.NoSuchElementException;

public class StackImplementInDoubleLinked<T extends Comparable<? super T>> {

    class Node {
        T data;
        Node prev;
        Node next;

        public Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private int size;
    private Node top;

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 将元素压入栈中
     * @param t 参数
     * @return item参数
     */
    public T push(T t) {
        Node p = top;
        Node aNode = new Node(t,null,null);
        top = aNode;
        if (p == null)
            //刚刚插入节点,什么也不操作
            ;
        else
        {
            //插入到p节点之后
            p.next = aNode;
            aNode.prev = p;
        }
        size++;
        return t;
    }

    /**
     * 移除并返回栈顶的元素
     * @return 栈顶元素
     */
    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException("站务更多元素");
        Node p = top;
        Node prev = p.prev;
        T data = p.data;
        p.data = null;
        p.prev = null;
        top = prev;
        if (prev == null)
            //只存在一个节点
            ;
        else {
            //存在多个节点
            prev.next = null;
        }
        size--;
        return data;
    }

    /**
     * 查看此栈顶部的对象，但不将其从栈中移除
     * @return 栈顶元素
     */
    public T peek()  {
        if (isEmpty())
            throw new NoSuchElementException("暂无更多元素");
        Node p = top;
        return p.data;
    }


    /**
     * 返回对象在此栈中的 1-based 位置。如果对象 o 作为此栈中的一个项出现，
     * 此方法返回从栈顶到该出现项的距离；栈顶的项被认为是距离 1 。使用 equals 方法将 o 与此栈中的项进行比较
     * @param t 要搜索的元素
     * @return 该元素距离栈顶的距离, {@code -1} 表示该元素不在栈中
     */
    public int search(T t) {
        int distance = 0;
        Node p;
        for (p = top; p != null; p = p.prev) {
            if (t.equals(p.data))
                return distance;
            distance++;
        }
        return  -1;
    }
}
