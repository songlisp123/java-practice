package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.Objects;

public class CustomButton extends JButton {
    private int WIDTH = 20;
    private int HEIGHT = 20;

    public CustomButton(String name) {
        super(name);
        //不绘制默认背景
        setContentAreaFilled(false);
        //设置无焦点
        setFocusPainted(false);
        //背景透明
        setOpaque(false);
        if (Objects.equals(name,"=")) setBackground(Color.CYAN);
        else setBackground(new Color(54, 61, 66));
        setForeground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("背景色:"+getBackground());
//        System.out.println("前景色:"+getForeground());
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),WIDTH,HEIGHT));
        g2.dispose();
        super.paintComponent(g);

    }

    public void paintBorder(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,
                WIDTH,HEIGHT));
        g2.dispose();

    }

    public void setArcSize(int width ,int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        repaint();
    }
}
