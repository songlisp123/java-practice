package com.snl.test.grafic.mouseEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseListenerDemo implements MouseListener {

    private final int squareX = 50;
    private final int squareY = 50;
    private final int squareW = 20;
    private final int squareH = 20;

    public MouseListenerDemo() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("按键点击……");
        draw(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("鼠标按压……");
        draw(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("鼠标是放……");
        draw(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("鼠标进入区域……");
        draw(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("鼠标退出区域……");
        draw(e);
    }

    private void draw(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            System.out.println("开始……");
        } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
            System.out.println("退出……");
        }
    }
}
