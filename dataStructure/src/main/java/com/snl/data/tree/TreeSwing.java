package com.snl.data.tree;

import javax.swing.*;
import java.awt.*;

public class TreeSwing extends JFrame  {

    private JPanel drawPanel = new TreePanel();

    public TreeSwing() {
        super("二叉树测试学习版");
        add(drawPanel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(TreeSwing::new);
    }
}
