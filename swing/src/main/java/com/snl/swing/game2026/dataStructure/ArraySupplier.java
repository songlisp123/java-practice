package com.snl.swing.game2026.dataStructure;

//据libgdx文档描述，这是一个数组提供类，可以创建新的数组。
@FunctionalInterface
public interface ArraySupplier<T> {

    ArraySupplier<?> INSTANCE = Object[]::new;

    static <T> ArraySupplier<T[]> instance() {
        return (ArraySupplier<T[]>) INSTANCE;
    }

    /**
     * 这是一个函数接口
     * @param size 给定的数组大小
     * @return 新的数组？？为什么是类型参数T？
     */
    T get(int size);
}
