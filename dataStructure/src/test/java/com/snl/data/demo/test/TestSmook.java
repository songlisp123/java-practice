package com.snl.data.demo.test;

import com.snl.swing.practice01.article.Smoke;
import com.snl.swing.practice01.article.SmokeImplement;

import javax.swing.*;
import java.awt.*;

public class TestSmook extends JPanel {


    protected Smoke smoke;

    public TestSmook() {
        smoke = new SmokeImplement(10,10,2,2,Color.CYAN);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(smoke.getColor());
        g2.fillOval((int) smoke.getxPos(), (int) smoke.getyPos(),smoke.getWEIGHT(),smoke.getHEIGHT());
        g2.dispose();
    }

    private static void createUi() {
        JFrame frame = new JFrame("绘制");
        frame.setLocationRelativeTo(null);
        var p = new TestSmook();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TestSmook::createUi);
    }


}
