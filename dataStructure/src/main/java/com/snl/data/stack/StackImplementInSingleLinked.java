package com.snl.data.stack;

import java.util.NoSuchElementException;

public class StackImplementInSingleLinked<T extends Comparable<? super T>> {

    private int size;
    private Node top;
    private Node previous;

    class Node {
        T data;
        Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    public  int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void push(T t) {
        Node p = top;
        Node aNode = new Node(t,null);
        top = aNode;
        if (p == null)
        {
            //当前第一次插入节点
            previous = null;
        }else {
            p.next = aNode;
            previous = p;
        }
        size++;
        return;
    }


    public T pop() {
        if (isEmpty())
            throw  new NoSuchElementException("暂无更多元素");
        Node p = top;
        Node prev = previous;
        T data = p.data;
        p.data = null;
        p.next = null;
        if (prev == null){
            //当前节点是第一个
            top = null;
        }else {
            //否则……
            top = prev;
            previous = p;
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

}
