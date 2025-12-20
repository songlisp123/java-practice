package com.snl.swing.practice;

import audio.util.WindowHandler;
import com.snl.swing.practice.menu.CustomMenuBar;
import com.snl.swing.practice.window.ClosedWindowDemo;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TextBoot  {


    private static final Logger logger = Logger.getLogger("TextEditor");
    private static final WindowHandler handler = new WindowHandler();

    static {
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("文本编辑器");
        ClosedWindowDemo closedWindowDemo = new ClosedWindowDemo(frame);
        MainPanel mainPanel = new MainPanel();
        CustomMenuBar customMenuBar = new CustomMenuBar();
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setJMenuBar(customMenuBar);
        frame.setLocation(200,50);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(closedWindowDemo);
    }
}
