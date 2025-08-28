package com.apress.bgn.char006.frame.grafics;

import java.awt.*;

public class DrawTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new DrawFrame();
            frame.setTitle("这是一个世界！");
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
