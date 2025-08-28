package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class File extends SimpleFrame{
    private JFileChooser chooser;
    public File() {
        super();
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        add(chooser);
    }
}

class test08 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new File();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}