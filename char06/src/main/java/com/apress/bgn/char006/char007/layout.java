package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class layout extends SimpleFrame {
    private JPanel panel;
    private GridBagLayout gridBagLayout;
    public layout() {
        super();
        gridBagLayout = new GridBagLayout();
        panel = new JPanel();
        panel.setLayout(gridBagLayout);
        //设置约束
        var constarints = new GridBagConstraints();
        constarints.weightx = 100.00;
        constarints.weighty = 100.00;
        constarints.gridx = 0;
        constarints.gridy = 2;
        constarints.gridwidth = 2;
        constarints.gridheight = 1;
        var button = new JButton("1");
        var button2 = new JButton("2");
        var button3 = new JButton("3");
        var button4 = new JButton("4");
        var button5 = new JButton("5");
        panel.add(button,constarints);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        button.addActionListener(event->{
            panel.setBackground(Color.CYAN);
        });
        button2.addActionListener(event->{
            panel.setBackground(panel.getForeground());
        });
        super.add(panel);
    }
}

class test06 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new layout();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
