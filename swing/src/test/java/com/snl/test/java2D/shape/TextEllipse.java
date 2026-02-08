package com.snl.test.java2D.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.RectangularShape;

public class TextEllipse extends JPanel {

    private RectangularShape shape;

    public TextEllipse() {
        setBackground(Color.black);
        shape = new Ellipse2D.Double(10,10,50,50);
        showPathInfo();
    }

    private void showPathInfo() {
        PathIterator pathIterator = shape.getPathIterator(null);
        System.out.println("pathIterator = " + pathIterator);
        while (pathIterator.isDone() == false) {
            double[] coordinates = new double[6];
            int i = pathIterator.currentSegment(coordinates);
            switch (i) {
                case PathIterator.SEG_MOVETO:
                    System.out.println("move to " +
                            coordinates[0] + ", " + coordinates[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    System.out.println("line to " +
                            coordinates[0] + ", " + coordinates[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    System.out.println("quadratic to " +
                            coordinates[0] + ", " + coordinates[1] + ", " +
                            coordinates[2] + ", " + coordinates[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    System.out.println("cubic to " +
                            coordinates[0] + ", " + coordinates[1] + ", " +
                            coordinates[2] + ", " + coordinates[3] + ", " +
                            coordinates[4] + ", " + coordinates[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    System.out.println("close");
                    break;
                default:
                    break;
            }
            pathIterator.next();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Color.green);
        g2.fill(shape);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame f = new JFrame("Java2D Gun Outline");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.add(new TextEllipse());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextEllipse::createUi);
    }
}
