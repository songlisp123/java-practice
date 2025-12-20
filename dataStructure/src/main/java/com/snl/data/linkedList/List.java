package com.snl.data.linkedList;

public  interface List<T> {

    /**
     * 该方法将会返回集合中元素的数量
     */
    int size();

    /**
     * 此方法将会在末端添加元素
     * @param t 类型T的参数
     */
    void add(T t);

    /**
     * 在索引{@code index}处添加元素
     * @param index 添加元素的索引位置
     * @param t 要添加的数据
     */
    void add(int index,T t);

    /**
     * 移除指定索引出的元素
     * @param index 索引位置
     * @return 移除的项目
     */
    T remove(int index);

    /**
     * 判断是否存在该元素
     * @param t 判断该元素
     * @return {@code true} 如果元素存在;{@code false} 元素不存在
     */
    boolean contains(T t);

    /**
     * 判断是否是空集
     * @return {@code true} 如果结果是空集;否则则为{@code false}
     */
    boolean isEmpty();

    /**
     * 获取值
     * @param index 指向的索引
     * @return 该元素
     */
    T get(int index);

    /**
     * 设置索引处的值
     * @param index 索引
     * @param t 提供的新值
     */
    void set(int index,T t);

    /**
     * 清空数据
     */
    void clear();
}
