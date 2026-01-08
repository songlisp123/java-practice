package com.snl.data.homework.charptor03;

import java.util.NoSuchElementException;

/**
 * 我们给出的删除策略的另一种替代方法是使用惰性删除。
 * 要删除一个元素，我们只需将其标记为已删除（使用一个额外的位字段）。
 * 列表中已删除和非已删除元素的数量作为数据结构的一部分进行维护。
 * 如果已删除元素的数量与非已删除元素的数量相同，我们将遍历整个列表，
 * 对所有标记的节点执行标准删除算法。
 */
public class Problem20<T> {
    /**
     * 当前链表的数据大小
     */
    private int size;

    /**
     * 记录当前数据结构被修改的次数,默认为0,迭代器会以此属性为基础构造
     */
    private int modCount = 0;

    /**
     * 延迟计数
     */
    private int deletedCount;

    /**
     * 未删除标记数
     */
    private int notDeletedCount;

    private Node head;
    private Node tail;

    private class Node {
        private T data;
        private Node prev;
        private Node next;
        private boolean isDeleted;

        public Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public Problem20() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addFirst(T t) {
        Node h = head;
        if (h == null) {
            head = new Node(t,null,null);
            tail = head;
        }
        else {
            Node p  = new Node(t,null,head);
            h.prev = p;
            head = p;
        }
        size++;
        notDeletedCount = size;
        modCount++;
    }

    public void addLast(T t) {
        Node o = tail;
        if (o==null) {
            tail = new Node(t,null,null);
            head = tail;
        }else {
            Node p = new Node(t,tail,null);
            o.next = p;
            tail = p;
        }

        size++;
        notDeletedCount = size;
        modCount++;
    }

    public T getHead() {
        return head.data;
    }

    public T getTail() {
        return tail.data;
    }

    public T removeLast() {
        Node o = tail;
        if (o == null) {
            throw new NoSuchElementException("暂无更多元素");
        }
        T data = tail.data;
        tail  = o.prev;
        o.data = null;
        o.prev.next = null;
        size--;
        notDeletedCount = size;
        modCount++;
        return data;
    }

    public T removeFirst() {
        Node h = head;
        if (h == null) {
            throw new NoSuchElementException("暂无更多元素");
        }
        T data = h.data;
        h.next.prev = null;
        h.data = null;
        head = h.next;
        size--;
        notDeletedCount = size;
        modCount++;
        return data;
    }

    public void show() {
        Node p = head;
        for (int i=0;i<size;i++) {
            if (p.next == null) {
                System.out.print(p.data);
                System.out.println();
                return;
            }
            else
                System.out.print(p.data+"-->");

            p = p.next;
        }
    }

    public void clear() {
        size = 0;
        modCount++;
        head = null;
        tail = null;
    }

    /**
     * 删除逻辑
     * @param t 参数
     * @return 布尔值
     * @implNote 该删除逻辑M次遍历，时间是O（M）（M ≤ N）
     */
    public boolean remove(T t) {
        Node p = head;
        while (p != null) {
            if (!p.isDeleted && p.data.equals(t)) {
                p.isDeleted = true;
                notDeletedCount--;
                deletedCount++;
                if (deletedCount >= notDeletedCount) {
                    clearUp();
                }
                return true;
            }
            p = p.next;
        }
        return false;
    }

    /**
     * 核心删除逻辑
     */
    private void clearUp() {
        Node s = new Node(null,null,head);
        Node prev = s;
        Node curr = head;
        while (curr != null) {
            //跳过删除的部分
            if (curr.isDeleted) {
                prev.next = curr.next;
                curr.next.prev = prev;
                deletedCount--;
            }else {
                prev = curr;
            }
            curr = curr.next;
        }
        head = s.next;
    }
}

/**
 * 延迟操作的有点：
 * 1、删除操作变为O（1）
 * ✅ 优点 2：避免频繁指针修改（对复杂结构友好）
 * ✅ 优点 3：删除 + 插入高度不对称时更高效（删除多，插入少，偶尔遍历才行）
 * ✅ 优点 4：摊还复杂度低（Amortized O(1)）
 * 缺点：
 * ❌ 缺点 1：遍历成本变高
 * ❌ 缺点 2：空间浪费（假死节点
 * ❌ 缺点 3：代码复杂度明显上升
 * ❌ 缺点 4：最坏情况仍是 O(N)
 */
