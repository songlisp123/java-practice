package com.apress.bgn.char006.frame.picture;

import javax.swing.*;
import java.awt.*;

public class frame extends JFrame {
    public frame() {
        add(new Picture("//home//snl//"));
        pack();
    }
}

class test {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new frame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("你好世界！");
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
