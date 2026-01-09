package com.snl.data.homework.charptor03.practice01.entity;

import java.awt.*;
import java.util.Collection;

public interface Group<T extends Sprite> {

    /**
     * 渲染组中元素
     * @param g 渲染上下文
     */
    void render(Graphics g);

    /**
     * 更新组中元素
     */
    void update(double detta);

    /**
     * 清空组
     */
    void clear();

    /**
     * 向组中添加元素
     * @param t 希望添加的元素
     */
    void add(T t);

    /**
     * 返回组中的精灵数量
     * @return 武器数量
     */
    int size();

    /**
     * 重置组中元素状态
     */
    void reset();

    /**
     * 组中是否存在元素
     * @return
     */
    boolean isEmpty();


    void addAll(Group<? super T>  otherGroup);

    /**
     * 获取数据模型
     */
    Collection<? super T> getData();

}
