package com.snl.data.demo.test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class DrawSomething extends JPanel {

    private GeneralPath shape;
    private Point2D start;
    //剑柄宽
    private double swordHitWeight;

    //剑柄高
    private double swordHitHeight;

    //剑翘宽
    private double height;

    //剑刃款
    private double swordBladeWeight;
    //剑刃高
    private double swordBladeHeight;

    private double swordTipWeight;

    public DrawSomething(Point2D start) {
        this.start = start;
    }

    public DrawSomething(Point2D start, double swordHitWeight, double swordHitHeight,
                         double swordBladeWeight, double swordBladeHeight, double swordTipWeight,
                         double height) {
        this.start = start;
        this.swordHitWeight = swordHitWeight;
        this.swordHitHeight = swordHitHeight;
        this.swordBladeWeight = swordBladeWeight;
        this.swordBladeHeight = swordBladeHeight;
        this.swordTipWeight = swordTipWeight;
        this.height = height;
        initData();
    }

    public DrawSomething() {
        initData();
    }


    private void initData() {
        double x = start.getX();
        double y = start.getY();
        setBackground(Color.black);
        shape = new GeneralPath();
        shape.moveTo(x,y);
        //剑柄绘制
        x = x +swordHitWeight;
        shape.lineTo(x,y);
        y = y - swordHitHeight;
        shape.lineTo( x,y);
        x = x  + height;
        shape.lineTo(x,y);
        y = start.getY() - (swordBladeHeight - swordHitHeight) / 2;
        shape.lineTo(x,y);
        x += swordBladeWeight;
        shape.lineTo(x,y);
        x += swordTipWeight;
        y = start.getY() + swordHitHeight/2;
        shape.lineTo(x,y);

        x -= swordTipWeight;
        y = start.getY() + (swordBladeHeight - swordHitHeight) / 2 + swordHitHeight;
        shape.lineTo(x,y);

        x -= swordBladeWeight;
        shape.lineTo(x,y);

        y = start.getY() + 2 * swordHitHeight;
        shape.lineTo(x,y);

        x -= height;
        shape.lineTo(x,y);

        y = start.getY() + swordHitHeight;
        shape.lineTo(x,y);

        x = start.getX();
        shape.lineTo(x,y);
        shape.closePath();



//        shape.lineTo(90,300);
//        shape.lineTo(60,340);
//        shape.lineTo(30,300);
//        shape.lineTo(30,100);
//        shape.lineTo(0,100);
//        shape.lineTo(0,80);
//        shape.lineTo(40,80);
//        shape.lineTo(40,0);
//        shape.closePath();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.cyan);
        g2.draw(shape);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("绘制");
        frame.setLocationRelativeTo(null);
        Point2D start = new Point2D.Double(0,40);
        var d = new DrawSomething(start,8,4,
                20,6,4,2);
        frame.getContentPane().add(d);
        frame.pack();
        frame.setFocusable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawSomething::createUi);
    }
}
