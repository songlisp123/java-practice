package com.snl.data.linkedList;

import java.util.*;

public class MoreStrongImplement<T extends Comparable<? super T>> {

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

    private Node first;
    private Node last;
    private int size = 0;
    private int modCount = 0;

    private final int MAX = Integer.MAX_VALUE;

    public MoreStrongImplement() {
        clear();
    }

    public MoreStrongImplement(Collection<? extends T> c) {
        clear();
        addAll(c);
    }

    private void linkFirst(T t) {
        Node f = first;
        Node p = new Node(t,null,first);
        first = p;
        if (f == null)
            last = p;
        else
            f.prev = p;
        size++;
        modCount++;
    }

    private T unLikeFirst() {
        Node f = first;
        if (f == null) return null;
        T data = f.data;
        Node next = first.next;
        f.data = null;
        f.next = null;
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return data;
    }

    private T unLinkLast() {
        Node l = last;
        if (l == null) return null;
        Node prev = l.prev;
        T data = l.data;
        l.data = null;
        l.prev = null;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return data;
    }

    private T unLikeNode(Node p) {
        assert p != null;
        Node prev = p.prev;
        Node next = p.next;
        T data = p.data;
        p.data = null;
        p.prev = null;
        p.next = null;
        if (prev == null) {
            //移除的是头部节点
            first = next;
            next.prev = null;
        } else if (next == null) {
            //移除的尾节点
            last = prev;
            prev.next = null;
        }else {
            //中间节点
            prev.next = next;
            next.prev = prev;
        }
        size--;
        modCount++;
        return data;

    }

    private void linkLast(T t) {
        Node l = last;
        Node p = new Node(t,last,null);
        last = p;
        if (l == null)
            first = p;
        else
            last.next = p;
        size++;
        modCount++;
    }

    private void linkBefore(T t,Node p) {
        //在p节点之前链接元素
        Node prev = p.prev;
        Node aNode = new Node(t,prev,p);
        //判断情况，如果p节点是一个头部节点
        p.prev = aNode;
        if (prev == null)
            //头部节点
            first = aNode;
        else
            prev.next = aNode;
        size++;
        modCount++;
    }

    private void linkAfter(T t,Node p) {
        Node next = p.next;
        Node aNode = new Node(t,p,next);
        p.next = aNode;
        if (next == null)
            //尾节点
            last = aNode;
        else
            next.prev = aNode;
        size++;
        modCount++;
    }

    public void addFirst(T t) {
        linkFirst(t);
    }

    public void addLast(T t) {linkLast(t);}

    public T getFirst() {
        if (isEmpty())
            throw  new NoSuchElementException("暂无更多元素");
        Node f = first;
        return f.data;
    }

    public T getLast() {
        if (isEmpty())
            throw  new NoSuchElementException("暂无更多元素");
        Node f = last;
        return f.data;
    }

    public boolean add(T t) {
        linkLast(t);
        return true;
    }

    public void add(int index , T t) {
        Node p = getNode(index);
        linkBefore(t,p);
    }

    public T get(int index) {
        Node node = getNode(index);
        return node.data;
    }

    public  int indexOf(T t) {
        //TODO ✅
        int index = 0;
        Node f;
        for (f = first;f!=null;f = f.next) {
            if (Objects.equals(t,f.data))
                return index;
            index++;
        }
        return -1;
    }

    /**
     * 获取并删除列表的头部节点
     * @return 删除的数据
     */
    public T remove() {
        return unLikeFirst();
    }

    /**
     * 如果存在，则删除容器内第一个出现的元素
     * @param t 要删除的元素
     * @return 布尔值，{@code true} 表示删除成功，否则返回{@code false}
     */
    public boolean remove(T t) {
        //TODO ✅
        Node f;
        for (f = first;f !=null;f = f.next) {
            if (Objects.equals(f.data,t))
            {
                unLikeNode(f);
                return true;
            }
        }
        return false;
    }

    /**
     * 移除并返回改容器的第一个元素
     * @return 第一个头部元素
     */
    public T removeFirst() {
        return unLikeFirst();
    }

    /**
     * 获取并删除最后一个元素
     * @return 最后一个元素
     */
    public T removeLast() {
        return unLinkLast();
    }

    /**
     * 内部方法，用来移除节点p
     * @param p 要移除的节点
     */
    public void remove(Node p) {
        unLikeNode(p);
    }

    /**
     * 将此容器内指定位置为{@code index} 的元素替换成新元素
     * @param index 指定索引
     * @param t 新元素值
     * @return 被替换的元素
     */
    public T set(int index , T t) {
        Node node = getNode(index);
        T data = node.data;
        node.data = t;
        return data;
    }

    private Node getNode(int index) {
        return getNode(index,0,size-1);
    }

    private Node getNode(int index, int lower, int higher) {
        if (index < lower || index > higher)
            throw new IndexOutOfBoundsException("超出索引边界");
        Node f;
        int middle = higher / 2;
        if (index < middle) {
            f = first;
            for (int i = 0;i<index;i++)
                f = f.next;
        }
        else {
            f = last;
            for (int i = size - 1;i>index;i--)
                f = f.prev;
        }
        return f;
    }

    int size() {
        return Math.min(size, MAX);
    }

    public boolean contains(T t) {
        return (indexOf(t) >= 0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        Node x ;
        for (x = first;x != null;)
        {
            Node next = x.next;
            x.data = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount ++;
    }

    /**
     * 从指定位置开始，将集合中的元素插入到列表中
     * @param index 指定索引
     * @param c 集合c
     * @return {@code true} 插入成功， {@code false} 插入失败
     */
    public boolean addAll(int index, Collection<? extends T> c) {
        //TODO 这个实现有点困难 ✅
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("索引超出边界");
        //判断集合的长度
        if (c == null || c.isEmpty())
            return false;
        int number = c.size();
        //接下来判断索引边界的问题
        //第一种情况是：索引恰好停在末尾
        Node previous , successor;
        if (index == size) {
            //如果插入位置是末端,后面的节点weinull，前面的节点为尾端
            successor = null;
            previous = last;
        }else {
            //如果插入的顺序在中间
            successor = getNode(index);
            previous = successor.prev;
        }
        //第三部：插入节点
        for (T t : c){
            Node p = new Node(t,previous,null);
            if (previous == null)
                //插入节点是头部
                first = p;
            else
                //插入节点是中间
                previous.next = p;
            previous = p;
        }

        if (successor == null)
            //插入的节点是尾部
            last = previous;
        else {
            previous.next = successor;
            successor.prev = previous;
        }

        size += number;
        modCount++;
        return true;
    }

    /**
     * 将指定集合中的所有元素追加到此列表的末尾，按照指定集合的迭代器返回他们的顺序
     * @param c 要插入的集合
     * @return {@code true} 插入成功， {@code false} 插入失败
     */
    public boolean addAll(Collection<? extends T> c) {
        //TODO 待实现 ✅
        return addAll(size(),c);

    }

    public boolean containsAll(Collection<? extends T> c) {
        //TODO 实现 ❌
        return false;
    }

    public int lastIndexOf(T t) {
        //TODO 实现 ✅
        Node l;
        int index = size - 1;
        for (l = last;l!=null;l = l.prev){
            if (Objects.equals(l.data,t))
                return index;
            index--;
        }
        return -1;
    }

    public ListIterator<T> listIterator() {
        return new ListIteratorImplement();
    }

    class ListIteratorImplement implements ListIterator<T> {

        Node currentNode = first;
        Node skipped = null;
        boolean toRemoved = false;
        int exceptedCount = modCount;
        int index0; //当前节点的索引,也是下一次调用next将要返回的索引
        int index1; //上一个节点的索引

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
            if (!hasNext())
                throw new NoSuchElementException("暂无元素");
            skipped = currentNode;
            T data = skipped.data;
            currentNode = currentNode.next;
            toRemoved = true;
            index1 = index0;
            index0++;
            return data;
        }

        @Override
        public boolean hasPrevious() {
            return  currentNode.prev != null;
        }

        @Override
        public T previous() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
            if (!hasPrevious())
                throw new NoSuchElementException("暂无元素");
            skipped = currentNode;
            T data = skipped.data;
            currentNode = currentNode.prev;
            //到达头部节点
            toRemoved = currentNode.prev != null;
            index1 = index0;
            index0--;
            return data;
        }

        @Override
        public int nextIndex() {
            if (currentNode == last)
                return size;
            return index0;
        }

        @Override
        public int previousIndex() {
            if (currentNode == first)
                return -1;
            return index0;
        }

        @Override
        public void remove() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
            if (!toRemoved)
                throw new IllegalStateException("非法状态异常");
            MoreStrongImplement.this.remove(currentNode.prev);
            toRemoved = false;
            exceptedCount++;
        }

        @Override
        public void set(T t) {
            currentNode.data = t;
            exceptedCount++;
        }

        @Override
        public void add(T t) {
            linkAfter(t,currentNode);
            exceptedCount++;
        }
    }

    class LinkIterator implements Iterator<T> {

        Node currentNode = first;
        int exceptedCount = modCount;
        boolean toRemove = false;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
            if (!hasNext())
                throw new NoSuchElementException("暂无更多元素");
            T data = currentNode.data;
            currentNode = currentNode.next;
            toRemove = true;
            return data;
        }

        @Override
        public void remove() {
            if (exceptedCount != modCount)
                throw new ConcurrentModificationException("并发修改异常");
            if (!toRemove)
                throw new IllegalStateException("状态异常");
            MoreStrongImplement.this.remove(currentNode.prev);
            toRemove = false;
            exceptedCount++;
        }
    }
}
