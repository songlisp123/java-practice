package com.snl.data.array;

import java.util.Objects;

public class HasReplicateArrayDemo<T> implements MyArrayImplement<T> {

    /**
     * 数据模型
     */
    private Object[] data;
    /**
     * 总是指向最后元素的下一个索引
     */
    private int currentIndex;

    /**
     * 这是数组的容量
     */
    private int maxCount;

    public HasReplicateArrayDemo(int number) {
        data = new Object[number];
        this.currentIndex = 0;
        this.maxCount = number;
    }

    @Override
    public void add(T t) {
        if (currentIndex > maxCount) {
            //要么扩容？要么抛出异常
            throw new ArrayIndexOutOfBoundsException("数组容量超出范围！");
        }
        data[currentIndex] = t;
        currentIndex++;
    }

    @Override
    public T remove() {
        if (currentIndex < 1) {
            throw new RuntimeException("暂无更多数据要删除");
        }
        T removeItem = (T) data[currentIndex-1];
        data[currentIndex - 1] = null;
        currentIndex--;
        return removeItem;
    }

    @Override
    public T remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= currentIndex) {
            throw new IndexOutOfBoundsException("超出索引边界！");
        }
        T t = (T) data[index];
        for (int i = index;i<currentIndex - 1;i++) {
            data[i] = data[i + 1];
        }
        data[currentIndex - 1] = null;
        currentIndex--;
        return t;
    }

    @Override
    public boolean search(T t) {
        //复杂逻辑
        int j = -1;
        for (int i=0;i<currentIndex;i++) {
            T result = (T) data[i];
            if (Objects.equals(t,result)) {
                j = i;
                break;
            }
        }
        return j != -1;
    }

    @Override
    public void show() {
        if(currentIndex == 0) System.out.println("[]");
        else {
            System.out.print("[");
            for (int i=0;i<currentIndex;i++) {
                if (i == currentIndex - 1)
                    System.out.print(data[i]);
                else {
                    System.out.print(data[i]+",");
                }
            }
            System.out.println("]");
        }
    }

    @Override
    public void add(int index, T t) {
        if (index <0 || index > currentIndex) {
            throw new IndexOutOfBoundsException("超出索引边界");
        }
        T d = null;
        T f;
        for (int i = index;i<currentIndex;i++) {
            if ( d== null) {
                d = (T) data[i +1];
                data[i+1] = data[i];
                continue;
            }
            f = (T) data[i +1];
            data[i + 1] = d;
            d = f;
        }
        data[index] = t;
        currentIndex++;
    }

    @Override
    public long length() {
        return currentIndex;
    }

    @Override
    public T modify(int index, T t) {
        if (index <0 || index > currentIndex) {
            throw new IndexOutOfBoundsException("超出索引边界");
        }
        T old = (T) data[index];
        data[index] = t;
        return old;
    }
}
