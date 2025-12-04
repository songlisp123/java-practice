package com.snl.swing.practice;

import javax.swing.*;
import java.awt.*;

public class EditTextPanel extends JPanel {

    protected JTextPane pane;

    public EditTextPanel() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        pane = new JTextPane();
        //TODO 这是一个文档话的文本
        alignAttributes();
    }

    private void alignAttributes() {
        pane.setForeground(Color.WHITE);
        pane.setBackground(Color.black);
        pane.setEditable(true);
        add(pane,BorderLayout.CENTER);
    }

    public JTextPane getPane() {
        return pane;
    }
}
