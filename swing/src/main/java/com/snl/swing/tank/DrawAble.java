package com.snl.swing.tank;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import java.awt.*;

public interface DrawAble {

    void update(double delta,Component parentComponent);
   <T extends DiKaErPlus> void draw(Graphics2D g2, T gameFrame);
}
