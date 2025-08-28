package com.apress.bgn.char006.frame.perfence;

import java.awt.*;

public class ImageTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new ImageViewerFrame();
            frame.setTitle("设置主体");
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
