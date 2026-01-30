package com.snl.swing.TwoDimensionDemo.transitionDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TransformationDemo extends JPanel {

    protected SimpleRectangleDemo rec;
    protected final List<Point2D> points =
            new ArrayList<>();

    public TransformationDemo() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);
        rec = new SimpleRectangleDemo(200,200,200,200);

        var centerPoint = rec.getCenterPoint();
        var locationPoint = rec.getLeftUpperPoint();
        var upperRightPoint = rec.getRightUpperPoint();
        var bottomLeftPoint = rec.getLeftBottomPoint();
        var bottomRightPoint = rec.getRightBottomPoint();

        points.add(centerPoint);
        points.add(locationPoint);
        points.add(upperRightPoint);
        points.add(bottomLeftPoint);
        points.add(bottomRightPoint);
    }

    @Override
    public Dimension getPreferredSize() {
        return  new Dimension(600,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(Color.CYAN);
        for (Point2D point : points) {
            g2.fillOval((int) (point.getX()-5), (int) (point.getY()-5),
                    10,10);
        }

        g2.setColor(Color.red);
        g2.draw(rec);

//        g2.translate(50,50);
//        for (Point2D point : points) {
//            g2.fillOval((int) (point.getX()-5), (int) (point.getY()-5),
//                    10,10);
//        }
//        g2.draw(rec);

        g2.rotate(Math.PI/4,rec.x,rec.y);
//        AffineTransform transform = g2.getTransform();
//        System.out.println("transform = " + transform);
        for (Point2D point : points) {
            g2.fillOval((int) (point.getX()-5), (int) (point.getY()-5),
                    10,10);
        }
        g2.draw(rec);
        Point2D rightUpperPoint = rec.getRightUpperPoint();
//        g2.translate(300,0);
//        AffineTransform transform3 = g2.getTransform();
//        System.out.println("transform3 = " + transform3);
        g2.scale(0.5,0.5);
        g2.draw(rec);
//
//        System.out.println("transform = " + transform);
//
//
        g2.shear(0.5,0.5);
        g2.draw(rec);
//
//        System.out.println("transform = " + transform);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.add(new TransformationDemo());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(TransformationDemo::createUi);
    }

}
