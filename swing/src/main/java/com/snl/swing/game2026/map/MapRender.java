package com.snl.swing.game2026.map;

import java.awt.*;

public interface MapRender {

    //渲染全部精灵
    void render();

    //渲染指定层级的精灵
    void  render(int[] layers);

    default void render(Graphics2D g2) {};
}
