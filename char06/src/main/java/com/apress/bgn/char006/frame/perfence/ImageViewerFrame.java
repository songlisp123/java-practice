package com.apress.bgn.char006.frame.perfence;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

public class ImageViewerFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;
    private String image;

    public ImageViewerFrame() {
        Preferences root = Preferences.userRoot();
//        System.out.println(root.toString());
        Preferences node = root.node("//com//apress//bgn//char006");
        int left = node.getInt("left",0);
        int top = node.getInt("top",0);
        int width = node.getInt("width",DEFAULT_HEIGHT);
        int height = node.getInt("height",DEFAULT_HEIGHT);

        setBounds(left,top,width,height);
        image = node.get("image",null);
        var label = new JLabel();
        if (image != null) label.setIcon(new ImageIcon(image));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                node.putInt("left",getX());
                node.putInt("top",getY());
                node.putInt("width",getWidth());
                node.putInt("height",getHeight());
                if (image != null)  node.put("image",image);
            }
        });

        add(label);
        var chooser  = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        var menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        var openItem = new JMenuItem("Open");
        menuBar.add(openItem);
        openItem.addActionListener(event->{
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                image = chooser.getSelectedFile().getPath();
                label.setIcon(new ImageIcon(image));
            }
        });

        var exitItem = new JMenuItem("Exit");
        menuBar.add(exitItem);
        exitItem.addActionListener(event->{
            System.exit(0);
        });
    }
}
