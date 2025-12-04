package com.snl.test;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ColorChooserTestDemo extends JPanel implements ChangeListener {

    protected JColorChooser colorChooser;
    protected JLabel banner;

    public ColorChooserTestDemo() {
        super(new BorderLayout());
        //设置窗口中的横幅
        banner = new JLabel("你好，世界",JLabel.CENTER);
        banner.setForeground(Color.YELLOW);
        banner.setBackground(Color.BLUE);
        banner.setOpaque(true);
        banner.setFont(new Font("楷体",Font.BOLD,15));
        banner.setPreferredSize(new Dimension(100,65));

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(banner,BorderLayout.CENTER);
        jPanel.setBorder(BorderFactory.createTitledBorder("被金色"));

        //设置文本颜色选择器
        colorChooser = new JColorChooser(banner.getForeground());
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("颜色选择器"));

        add(jPanel,BorderLayout.CENTER);
        add(colorChooser,BorderLayout.PAGE_END);

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Color color = colorChooser.getColor();
        banner.setForeground(color);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ColorChooserTestDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.add(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
