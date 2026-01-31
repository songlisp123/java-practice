package com.snl.test.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CheckInputEvent implements KeyListener {

    boolean[] keys;
    int[] polled;

    public CheckInputEvent() {
        keys = new boolean[256];
        polled = new int[256];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //空实现
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        //获取事件虚拟键码
        int keyCode = e.getKeyCode();
        if (keyCode < 0 || keyCode > keys.length)
        {
            //如果存在
            throw new ArrayIndexOutOfBoundsException("数组越界");
        }

        keys[keyCode] = true;
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 || keyCode < keys.length)
        {
            keys[keyCode] = false;
        }
    }

    public synchronized boolean keyDown(int keyCode) {
        return polled[keyCode] > 0;
    }

    public boolean keyDownOnce(int keyCode) {
        return polled[keyCode] == 1;
    }

    public synchronized void poll() {
        for (int i=0;i<keys.length;i++) {
            if (keys[i]) {
                polled[i] ++;
            }else {
                polled[i] = 0;
            }
        }
    }
}
