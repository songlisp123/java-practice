package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.Vector2D;

public interface Translatable {

    /**
     * 平移
     * @param translated 平移距离
     */
    void translate(Vector2D translated);

    /**
     * 平移
     * @param x x坐标
     * @param y y坐标
     */
    void translate(double x,double y);
}
