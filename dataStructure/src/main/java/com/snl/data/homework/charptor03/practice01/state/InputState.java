package com.snl.data.homework.charptor03.practice01.state;

public class InputState {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public boolean change;

    public Direction direction = Direction.EAST;

    //电平
    public boolean attackHeld;

    // 边沿状态（只一帧）
    public boolean attackPressed;
    public static char c;
}
