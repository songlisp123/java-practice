package com.snl.data.demo.test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class DrawSomething extends JPanel {

    private GeneralPath shape;

    public DrawSomething() {
        setBackground(Color.black);
        shape = new GeneralPath();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.cyan);
        g2.fill(shape);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("绘制");
        frame.setLocationRelativeTo(null);
        var d = new DrawSomething();
        frame.getContentPane().add(d);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawSomething::createUi);
    }
}
