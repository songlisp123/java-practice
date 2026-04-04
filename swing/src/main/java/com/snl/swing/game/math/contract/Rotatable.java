package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.Vector2D;

public interface Rotatable {

    /**
     * 旋转
     * @param rotateTheta 旋转角度
     */
    void rotate(double rotateTheta);

    /**
     * 以{@code rotateCenter} 为中心 旋转
     * @param rot 旋转角度
     * @param rotateCenter 旋转中心
     * @since 2026年4月4日12:41:10
     */
    void rotate(double rot, Vector2D rotateCenter);

    /**
     * 以坐标为中心旋转
     * @param rot 旋转角度
     * @param x x坐标
     * @param y y坐标
     * @since 2026年4月4日12:42:10
     */
    void rotate(double rot,double x,double y);
}
