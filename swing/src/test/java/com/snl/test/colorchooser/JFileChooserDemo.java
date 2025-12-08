package com.snl.test.colorchooser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class JFileChooserDemo extends JPanel implements ChangeListener {

    private JColorChooser fileChooser;
    private JLabel banner;

    public JFileChooserDemo() {
        super(new BorderLayout());

        //创建横幅
        banner = new JLabel("你好世界！",JLabel.CENTER);
        banner.setForeground(Color.YELLOW);
        banner.setBackground(Color.BLUE);
        //下一步必须
        banner.setOpaque(true);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setPreferredSize(new Dimension(100, 65));

        //创建颜色选择框
        fileChooser = new JColorChooser(banner.getForeground());
        fileChooser.getSelectionModel().addChangeListener(this);
        fileChooser.setBorder(BorderFactory.createTitledBorder("颜色选择器"));

        add(banner,BorderLayout.CENTER);
        add(fileChooser,BorderLayout.PAGE_END);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Color color = fileChooser.getColor();
        System.out.println("color = " + color);
        banner.setForeground(color);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new JFileChooserDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

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
