package com.snl.swing.game2026.dataStructure;

import com.snl.swing.game.math.Vector2D;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

//模拟数组实现
public class Array<T> implements Iterable<T>, Serializable,Cloneable {

    private T[] items;
    //大小
    private int size;
    //是否有序？？
    private boolean ordered;

    private static final int INITIAL_SIZE = 16;

    public Array() {
        //无参构造器，创建一个有序的列表
        this(true, INITIAL_SIZE);
    }

    public Array(int initialSize) {
        this(true, initialSize);
    }

    public Array(boolean ordered, int initialSize) {
        this(ordered, initialSize, ArraySupplier.instance());
    }

    public Array(boolean ordered, int initialSize, ArraySupplier<T[]> arraySupplier) {
        this.ordered = ordered;
        //使用size参数创建底层数据
        items = arraySupplier.get(initialSize);
    }

    public Array(Array<? extends T> otherArray) {
        this.items = Arrays.copyOf(otherArray.items, otherArray.size);
        this.ordered = otherArray.ordered;
        this.size = otherArray.size;
    }


    //关于数组的一些操作：添加、删除、排序和打乱
    //添加操作可以有以下操作完成：
    /*
    * 判断是否到达末尾，如果是扩容否则插入
    * */
    public void add(T elemnet) {
        T[] items = this.items;
        if (size == items.length) {
            //到达数组末尾，扩容后：
            /*
             * 如果数组元素 * 扩容系数少于8个 --》 扩容至八个
             * */
            items = resize(Math.max(8, (int) (size * 1.75)));
        }
        items[size++] = elemnet;
    }

    public void add(int index,T element) {
        checkIndexBounds(index);
        T[] items = this.items;
        if (size == items.length)
            items = resize(Math.max(8,(int) (size * 1.75)));
        if (ordered) {
            System.arraycopy(items,index,items,index + 1,size - index);
        }
        else
            items[size] = items[index];
        size++;
        items[index] = element;
    }

    private void checkIndexBounds(int index) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("参数数组超越边界");
    }

    private T[] resize(int newSize) {
         this.items = Arrays.copyOf(this.items, newSize);
         return this.items;
    }

    public void addAll(Array<? extends T> array) {
        this.addAll(array,0,array.size);
    }

    public void addAll(Array<? extends T> array,int start,int count) {
        int s = start + count;
        if (s > array.size)
            throw new IndexOutOfBoundsException("索引超出异常");
        this.addAll(array.items,start,count);
    }

    public void addAll(T[] array) {
        this.addAll(array,0,array.length);
    }

    private void addAll(T[] array, int start, int count) {
        T[] items = this.items;
        int needResize = size + count;
        if (needResize >= items.length)
            items = resize(Math.max(Math.max(8,needResize),(int) (size * 1.75)));
        System.arraycopy(array,start,items,size,count);
        size += count;
    }

    public void clear() {
        T[] items = this.items;
        for (int i = 0;i<size;i++) {
            items[i] = null;
        }
        size = 0;
    }

    public boolean contains(T value,boolean identity) {
        checkNullAugmentException(value);
        T[] items = this.items;
        int i = size - 1;
        if (identity) {
            while (i >= 0)
                if (items[i--] == value) return true;
        }else {
            while (i >= 0)
                if (items[i--].equals(value)) return true;
        }
        return false;
    }

    //是否包含指定的数组的所有部分
    public boolean containsAll(Array<? extends T> array,boolean identity) {
        T[] items = array.items;
        for (int i = 0;i<array.size;i++) {
            //TODO
            if (!contains(items[i],identity )) return false;
        }
        return true;
    }


    public boolean containsAny(Array<? extends T> array,boolean identity) {
        T[] items = array.items;
        for (int i = 0;i<array.size;i++) {
            if (contains(items[i],identity )) return true;
        }
        return false;
    }

    public T get(int index) {
        checkIndexBounds(index);
        return items[index];
    }

    public void  set(int index,T value) {
        checkIndexBounds(index);
        items[index] = value;
    }

    public void swap(int first,int second) {
        checkIndexBounds(first);
        checkIndexBounds(second);
        T[] items = this.items;
        T temp = items[first];
        items[first] = items[second];
        items[second] = temp;
    }

    public int size() {
        return size;
    }

    public int length() {
        return items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int indexOf(T value,boolean identity) {
        checkNullAugmentException(value);
        T[] items = this.items;
        int result = -1;
        if (identity) {
            for (int i = 0;i<size;i++)
                if (items[i] == value) {
                    result = i;
                    break;
                }
        }else {
            for (int i = 0;i<size;i++)
                if (items[i].equals(value)) {
                    result = i;
                    break;
                }
        }
        return result;
    }


    public int lastIndexOf(T value,boolean identity,boolean reversing) {
        checkNullAugmentException(value);
        T[] items = this.items;
        int result = -1;
        if (identity) {
            if (reversing) {
                //如果反向
                for (int i = size - 1;i>=0;i--) {
                    if (items[i] == value) {
                        result = i;
                        break;
                    }
                }
            }else {
                //否则正向
                for (int i = 0;i<size;i++) {
                    if (items[i] == value) {
                        result = i;
                        break;
                    }
                }
            }
        }
        else {
            if (reversing) {
                //如果反向
                for (int i = size - 1;i>=0;i--) {
                    if (items[i].equals(value)) {
                        result = i;
                        break;
                    }
                }
            }else {
                //否则正向
                for (int i = 0;i<size;i++) {
                    if (items[i].equals(value)) {
                        result = i;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private void checkNullAugmentException(T t) {
        if (t == null)
            throw new IllegalArgumentException("参数为null");
    }

    public boolean remove(T t,boolean identity) {
        checkNullAugmentException(t);
        T[] items = this.items;
        if (identity) {
            for (int i = 0; i < size; i++) {
                if (items[i] == t) {
                    remove(i);
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (items[i].equals(t)) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public T remove(int index) {
        checkIndexBounds(index);
        T[] items = this.items;
        T removed = items[index];
        size--;
        //TODO
        if (ordered)
            System.arraycopy(items,index + 1,items,index,size - index);
        else
            items[index] = items[size];
        items[size] = null;
        return  removed;
    }

    public int replaceAll(T removed,T newValue,boolean identity) {
        checkNullAugmentException(removed);
        checkNullAugmentException(newValue);
        T[] items = this.items;
        int replaceCount = 0;
        if (identity) {
            for (int i = 0;i<size();i++) {
                if (items[i] == removed) {
                    items[i] = newValue;
                    replaceCount++;
                }
            }
        }else {
            for (int i = 0;i<size;i++) {
                if (items[i].equals(removed)) {
                    items[i] = newValue;
                    replaceCount++;
                }
            }
        }
        return replaceCount;
    }

    //根据文档这个操作在【fromindex 到 endindex】是闭区间
    protected void removeRange(int fromIndex,int endIndex) {
        checkIndexBounds(fromIndex);
        checkIndexBounds(endIndex);
        if (fromIndex > endIndex)
            throw new IllegalArgumentException(fromIndex +"参数必须比" + endIndex + "参数要小！");
        int n = size;
        int removeCount = endIndex - fromIndex + 1;
        //最后一个元素的索引
        int lastIndex = n - removeCount;
        T[] items = this.items;
        if (ordered) {
            System.arraycopy(items,fromIndex + removeCount,items,fromIndex,n - (fromIndex + removeCount));
        }else {
            int i = Math.max(lastIndex,endIndex + 1);
            System.arraycopy(items,i,items,fromIndex,n - i);
        }
        for (int  i = lastIndex;i < n ;i++) {
            items[i] = null;
        }
        size = n - removeCount;
    }

    public boolean removeAll(Array<? extends T> array,boolean identity) {
        T[] items = this.items;
        int size = size();
        int startSize = size;
        if (identity) {
            for (int i = 0;i<array.size;i++ ) {
                //TODO
                T item = array.get(i);
                for (int ii = 0;ii<size;i++) {
                    if (item == items[ii]) {
                        remove(ii);
                        size--;
                        break;
                    }
                }
            }
        }else {
            for (int i = 0;i<array.size;i++ ) {
                //TODO
                T item = array.get(i);
                for (int ii = 0;ii<size;i++) {
                    if (item.equals(items[ii])) {
                        remove(ii);
                        size--;
                        break;
                    }
                }
            }
        }
        return size != startSize;
    }

    public boolean removeIf(Predicate<? super T> predicate) {
        T[] items = this.items;
        int removeCount = 0;
        for (int i = 0;i<size;i++) {
            if (predicate.test(items[i])) {
                remove(i);
                removeCount++;
            }
        }
        return removeCount > 0;
    }

    //开闭区间
    public Array<T> subArray(int from,int end) {
        checkIndexBounds(from);
        checkIndexBounds(end);
        if (from > end)
            throw new IllegalArgumentException(from +"参数必须比" + end + "参数要小！");
        Array<T> array = new Array<>();
        if (from == end)
            //无操作
            return array;
        T[] items = this.items;
        int size = size();
        int count = end - from;
        array.items = Arrays.copyOfRange(items,from,end);
        array.size = size - count;
        array.ordered = this.ordered;
        return array;
    }

    public void trimSize() {
        int size = size();
        int length = length();
        if (length == size)
            return;
        T[] items = this.items;
        this.items = Arrays.copyOf(items,size);
    }

    public void truncate(int newSize) {
        if (newSize > size()) {
            //如果大于现在数组包含的元素
            return;
        }
        for (int i = newSize;i<size;i++) {
            items[i] = null;
        }
        size = newSize;
    }

    public void reverse() {
        T[] items = this.items;
        int lastIndex = size() - 1,n  = size() / 2;
        for (int i = 0;i<n;i++) {
            int ii = lastIndex - i;
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public void  shuffle() {
        T[] items = this.items;
        for (int i = size() - 1;i>=0;i--) {
            int ii = (int) (Math.random() * i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(this,false);
    }

    //经典
    public int hashCode () {
        if (!ordered) return super.hashCode();
        Object[] items = this.items;
        int h = 1;
        for (int i = 0, n = size; i < n; i++) {
            h *= 31;
            Object item = items[i];
            if (item != null) h += item.hashCode();
        }
        return h;
    }

    public boolean equals (Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof Array)) return false;
        Array array = (Array)object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        Object[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++) {
            Object o1 = items1[i], o2 = items2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) return false;
        }
        return true;
    }

    //使用 == 号而不是equals方法
    public boolean equalsIdentity (Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof Array)) return false;
        Array array = (Array)object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        Object[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++)
            if (items1[i] != items2[i]) return false;
        return true;
    }

    public String toString () {
        if (size == 0) return "[]";
        T[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    public String toString (String separator) {
        if (size == 0) return "";
        T[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
    

    static public class ArrayIterator<T> implements Iterator<T> , Iterable<T> {

        //引用final底层数据变量
        private final Array<T> copyArray;
        private final boolean allowRemove;
        int index;
        boolean valid = true;

        public ArrayIterator(Array<T> copyArray) {
            this(copyArray,true);
        }

        public ArrayIterator(Array<T> copyArray, boolean allowRemove) {
            this.copyArray = copyArray;
            this.allowRemove = allowRemove;
        }

        @Override
        public Iterator<T> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            if (!valid) {
                throw new RuntimeException("不能再运行期间修改数组");
            }
            return index < copyArray.size;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException("暂未找到该元素");
            if (!valid) {
                throw new RuntimeException("不能再运行期间修改数组");
            }
            return copyArray.items[index++];
        }

        @Override
        public void remove() {
            if (!allowRemove)
                throw new RuntimeException("不支持删除数组");
            index--;
            copyArray.remove(index);
        }
    }

    //TODO
    public static class ArrayIterable<T> implements Iterable<T> {

        @Override
        public Iterator<T> iterator() {
            return null;
        }
    }

    public T[] toArray() {
        return Arrays.copyOf(items,size);
    }
}
