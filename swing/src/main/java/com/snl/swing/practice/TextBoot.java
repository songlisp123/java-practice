package com.snl.swing.practice;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class TextBoot  {


    public static void main(String[] args) {
        JFrame frame = new JFrame("文本编辑器");
        ClosedWindowDemo closedWindowDemo = new ClosedWindowDemo(frame);
        CustomMenuBar customMenuBar = new CustomMenuBar();
        MainPanel mainPanel = new MainPanel();
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setJMenuBar(customMenuBar);
        frame.setLocation(200,50);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(closedWindowDemo);
    }
}
