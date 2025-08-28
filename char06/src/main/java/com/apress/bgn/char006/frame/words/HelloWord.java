package com.apress.bgn.char006.frame.words;

import javax.swing.*;
import java.awt.*;

public class HelloWord {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new Frame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("你好世界！");
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
