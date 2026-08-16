package com.snl.swing.game2026;


import java.awt.*;

public interface Part   {

    double getStart();
    double getEnd();

    void startAnimator();

    void flush();

    void render(Graphics2D g2);
}
