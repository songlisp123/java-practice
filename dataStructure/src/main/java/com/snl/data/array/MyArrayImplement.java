package com.snl.data.array;

public interface MyArrayImplement<T>  {

    /**
     * 这个程序要求插入数据到额外的空间,默认为最后一个元素
     * @param t 添加的元素
     */
    void add(T t);

    /**
     * 这个方法要求移除最后一个元素
     * @return 移除的元素
     */
    T remove();

    /**
     * 这个方法要求移除特定索引的元素，确保索引保证在区间内
     * @param index 特定索引
     * @return 移除的元素
     * @throws IndexOutOfBoundsException 如果索引超出边界
     */
    T remove(int index) throws IndexOutOfBoundsException;

    /**
     * 查询特定的元素是否在数据结构中
     * @param t 要查询的元素
     * @return 如果找到则为true，否则为false
     */
    boolean search(T t);

    /**
     * 打印自身所包含的元素
     */
    void show();

    /**
     * 在指定索引处插入值
     * @param index 索引
     * @param t 插入的新值
     */
    void add(int index,T t);

    /**
     * 此函数要求返回元素数量
     * @return 返回元素数量
     */
    long length();

    /**
     * 此函数将会修改指定索引的指
     * @param index 索引
     * @param t 修改后的值
     * @return 修改前的值
     */
    T modify(int index,T t);

    T get(int index);
}
