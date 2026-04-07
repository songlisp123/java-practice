package com.snl.swing.practice01.text;

public interface Text {

    /**
     * 该方法返回文章的行数
     * @return 文章的行数
     */
    int getCount();

    /**
     * 获取文本
     * @param index 索引位置
     * @return 索引位置的文本
     */
    String getString(int index);
}
