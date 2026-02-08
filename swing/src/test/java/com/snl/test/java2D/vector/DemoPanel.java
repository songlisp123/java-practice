package com.snl.test.java2D.vector;

import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class DemoPanel extends JPanel {

    public DemoPanel() {
        setBackground(Color.black);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        AffineTransform transform = g2.getTransform();

        Dimension preferredSize = getPreferredSize();
        int width = preferredSize.width;
        int height = preferredSize.height;
        g2.translate(width / 2,height / 2);
        g2.scale(1,-1);
        g2.setColor(Color.cyan);
        RectangularShape shape = new Rectangle2D.Double(0,0,50,50);
        RectangularShape shape2 = new Rectangle2D.Double(-50,-50,50,50);
        g2.drawLine(0,0,width / 2,0);
        g2.drawLine(0,0,0,height / 2);
        g2.draw(shape);
        g2.draw(shape2);
        g2.setTransform(transform);
        g2.drawString("你好世界",50,50);
        g2.dispose();
    }

    static void createAndShowUi() {
        JFrame frame = new JFrame("测试框架");
        var p = new DemoPanel();
        frame.getContentPane().add(p);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Utils.centerContainer(frame);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(DemoPanel::createAndShowUi);
    }

}
