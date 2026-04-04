package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.SegMent;
import com.snl.swing.game.math.Vector2D;

public interface Convex {

    /**
     * 获取投影法向轴
     * @return 轴集合
     */
    Vector2D[] getAxes();

    /**
     * 获取 凸变形的边
     * @return 凸变形的边
     */
    SegMent[] getEdge();

}
