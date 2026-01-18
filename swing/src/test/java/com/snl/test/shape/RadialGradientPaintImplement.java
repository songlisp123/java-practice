package com.snl.test.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class RadialGradientPaintImplement extends JPanel implements  KeyListener {

    private double pX;
    private double pY;
    private boolean pressed;
    private boolean showing;
    private Shape shape;
    private final double SIZE = 100;
    private RadialGradientPaint paint;
    private float radius;
    private Point2D focus;

    public RadialGradientPaintImplement() {
        this.addKeyListener(this);
        setBackground(Color.black);
        pX = 400 / 2.0 ;
        pY = 300 / 2.0 ;
        shape = new Ellipse2D.Double(pX - SIZE / 2,pY - SIZE /2 ,SIZE,SIZE);
        focus = new Point2D.Double(pX - 15,pY - 15);
        radius = 50.f;
        showing = true;
    }


    @Override
    public void updateUI() {
        super.updateUI();
        if (pressed && showing) {
            radius = Math.max(1,radius-=0.5f);
            if (radius == 1) {
                showing = false;
            }
        }
        if (!showing) {
            radius  = Math.min(50.f,radius +=0.5f);
            if (radius == 50.f) {
                showing = true;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Point2D center = new Point2D.Double(pX,pY);
        float[] f = {0.5f,1.0f};
        Color[] colors = {
                Color.BLACK,
//                Color.ORANGE,
//                Color.GREEN,
//                Color.MAGENTA,
                Color.GRAY
        };
        paint = new RadialGradientPaint(center,radius,focus,f,colors,MultipleGradientPaint.CycleMethod.REPEAT);
        g2.setPaint(paint);
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
        RadialGradientPaintImplement radialGradientPaintImplement = new RadialGradientPaintImplement();
        f.add(radialGradientPaintImplement);
        f.addKeyListener(radialGradientPaintImplement);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RadialGradientPaintImplement::createUi);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            pressed = true;
            updateUI();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            pressed = false;
        } else {
            return;
        }
    }
}
