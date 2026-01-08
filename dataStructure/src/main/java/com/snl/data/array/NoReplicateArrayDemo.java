package com.snl.data.array;

import java.util.Objects;

public class NoReplicateArrayDemo<T>  implements MyArrayImplement<T>{

    protected T[] data;
    /**
     * 这是下一个可插入的元素
     */
    protected int currentIndex;

    public NoReplicateArrayDemo(int number) {
        data = (T[]) new Object[number];
        currentIndex = 0;
    }

    @Override
    public void add(T t) {
        //处理的逻辑有点多
        boolean isHave = search(t);
        if (isHave) {
            throw new RuntimeException("已经存在该元素，不能重复添加元素");
        }
        data[currentIndex] = t;
        currentIndex++;
    }

    @Override
    public T remove() {
        int lastIndex = currentIndex - 1;
        T d = data[lastIndex];
        data[lastIndex] = null;
        currentIndex--;
        return d;
    }

    @Override
    public T remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= currentIndex) {
            throw new IndexOutOfBoundsException("超出索引边界！");
        }
        T t = data[index];
        int lastIndex = currentIndex - 1;
        for (int i = index;i<lastIndex;i++) {
            data[i] = data[i + 1];
        }
        data[lastIndex] = null;
        currentIndex--;
        return t;
    }

    @Override
    public boolean search(T t) {
        for (int i=0;i<currentIndex-1;i++) {
            T d = (T) data[i];
            if (Objects.equals(d,t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(int index, T t) {
        if (index <0 || index > currentIndex) {
            throw new IndexOutOfBoundsException("超出索引边界");
        }
        //处理的逻辑有点多
        boolean isHave = search(t);
        if (isHave) {
            throw new RuntimeException("已经存在该元素，不能重复添加元素");
        }
        T d = null;
        T f;
        for (int i = index;i<currentIndex;i++) {
            if ( d== null) {
                d = data[i +1];
                data[i+1] = data[i];
                continue;
            }
            f = data[i +1];
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
        T old = data[index];
        data[index] = t;
        return old;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > length()) {
            throw new IndexOutOfBoundsException("索引超出边界");
        }
        return data[index];
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
}

//第一阶段：已完成
