package com.snl.data.homework.charptor03.practice01.entity;

import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public interface AbstractSprite {

    void update(double delta, InputState state);

    void paint(Graphics g);

    void reset();

    //以下程序获取精灵的边界线
    double getLeft();
    double getRight();
    double getTop();
    double getBottom();

}
