package com.apress.bgn.char006.frame;

import javax.swing.*;
import java.awt.*;

public class frame {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new SimpleFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("这是一个尝试！");
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}


