package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class TextFileds {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new SimpleFrame();
            var test = new JTextField("默认输出……？",20);
            var label = new JLabel("User name: ", SwingConstants.LEFT);
            label = new JLabel("<html><a href='nan'>nansho</a><b>Required</b> " +
                    "<strong>entry</strong>" +
                    "<b>:</b></html>");
            var testArea = new JTextArea(4,60);
            var scrollPanle = new JScrollPane(testArea);
//            testArea.setLineWrap(true);
            test.setBackground(Color.darkGray);
            frame.setLayout(new GridLayout(0,1));
            frame.add(test);
            frame.add(label);
            frame.add(new JButton("弹出"));
//            frame.add(testArea);
            frame.add(scrollPanle);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setTitle("测试！");
            frame.setVisible(true);
        });
    }
}
