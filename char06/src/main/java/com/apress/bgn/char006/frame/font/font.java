package com.apress.bgn.char006.frame.font;

import java.awt.*;

public class font {
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->{
            var frame = new FontFrame();
            frame.setTitle("测试");
            frame.setResizable(true);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
