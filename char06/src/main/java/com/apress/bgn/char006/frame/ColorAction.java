package com.apress.bgn.char006.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorAction implements ActionListener {
     static JPanel buttonPanel = new JPanel();
    private Color backgroudColor;

    public ColorAction(Color c) {
        buttonPanel.setSize(150,150);
        backgroudColor = c;
    }

    public void actionPerformed(ActionEvent event) {
        this.buttonPanel.setBackground(backgroudColor);
    }
}
