package com.snl.data.demo.test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;


public class DrawGun extends JPanel {

    private GeneralPath path;


    public DrawGun() {
        setBackground(Color.black);
        path = new GeneralPath();
        path.moveTo(0,0);
        path.moveTo(0,90);
        path.lineTo(30,90);
        path.lineTo(35,60);
        //绘制圆弧
        Ellipse2D.Double aDouble = new Ellipse2D.Double(20, 20, 20, 20);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //绘制手枪
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Color.CYAN);
        g2.draw(path);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("绘制");
        frame.setLocationRelativeTo(null);
        var p = new DrawGun();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawGun::createUi);
    }
}
