package com.snl.test.java2D.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class RoundGradientPaint extends JPanel implements Paint {

    private Point2D mPoint;
    private Point2D mRadius;
    protected Color mPointColor,mBackGroundColor;

    public RoundGradientPaint(Point2D mPoint, Point2D mRadius, Color mPointColor, Color mBackGroundColor) {
        if (mRadius.distance(0,0) <= 0) {
            throw new IllegalArgumentException("非法参数异常");
        }
        this.mPoint = mPoint;
        this.mRadius = mRadius;
        this.mPointColor = mPointColor;
        this.mBackGroundColor = mBackGroundColor;
    }


    public RoundGradientPaint(double x,double y,Color mPointColor, Color mBackGroundColor, Point2D mRadius) {
        if (mRadius.distance(0,0) <= 0) {
            throw new IllegalArgumentException("非法参数异常");
        }
        mPoint = new Point2D.Double(x,y);
        this.mPointColor = mPointColor;
        this.mBackGroundColor = mBackGroundColor;
        this.mRadius = mRadius;
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
                                      Rectangle2D userBounds, AffineTransform xform,
                                      RenderingHints hints)
    {
        Point2D transformedPoint = xform.transform(mPoint, null);
        Point2D transformedRadius = xform.deltaTransform(mRadius, null);
        return new RoundGradientContext(transformedPoint,transformedRadius,mPointColor,mBackGroundColor);
    }

    @Override
    public int getTransparency() {
        int a = mPointColor.getAlpha();
        int b = mBackGroundColor.getAlpha();
        return ((a & b) == 0xff) ? Transparency.OPAQUE : Transparency.TRANSLUCENT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Color.green);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame f = new JFrame("Java2D Gun Outline");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var p = new RoundGradientPaint(10,10,Color.magenta,Color.CYAN,new Point2D.Double(50,50));
        f.getContentPane().add(p);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoundGradientPaint::createUi);
    }
}
