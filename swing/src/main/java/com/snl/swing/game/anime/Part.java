package com.snl.swing.game.anime;

import java.awt.*;

/**
 * 场景，画面
 */
public interface Part {
    int getEnd();
    int getStart();
    void render(int w, int h, Graphics2D g2);
    void step(int w,int h);
    void reset(int w,int h);
}
