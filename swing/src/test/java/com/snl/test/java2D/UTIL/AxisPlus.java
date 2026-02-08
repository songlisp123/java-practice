package com.snl.test.java2D.UTIL;

import com.snl.test.java2D.vector.Matrix3x3f;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.random.RandomGenerator;

public final class AxisPlus {

    //x轴
    Shape xAxis;
    //y轴
    Shape yAxis;
    //坐标点
    final java.util.List<Shape> coords = new ArrayList<>();
    //网格
    final java.util.List<Axis.Grid> grids = new ArrayList<>();
    //斜率为45度的交叉线
    Shape x_45du;
    Shape x_135Du;

    final RandomGenerator g = RandomGenerator.getDefault();

    public void createAxis(Matrix3x3f viewport,Component c) {
        if (c == null)
            return;
        if (!coords.isEmpty())
            coords.clear();
        Point2D p;
        double maxWidth = c.getWidth();
        double maxHeight = c.getHeight();
        //x轴
        Point2D originPoint = new Point2D.Double();
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(viewport);
        p = mat.mul(originPoint);
        xAxis = new Line2D.Double(p.getX(),p.getY(),900,p.getY());
//        fillCoords(gap,originPoint);
        createYAxis(p,maxHeight);
        createAxisOf45(p);
    }

    //TODO填充网格
    //创建y轴
    private void createYAxis(Point2D p, double maxHeight) {
        yAxis = new Line2D.Double(p.getX(),p.getY(),p.getX(),-maxHeight);
    }

    //创建斜率45°的对角线
    private void createAxisOf45(Point2D originPoint) {
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI / 4,
                originPoint.getX(),originPoint.getY());
        x_45du = at.createTransformedShape(xAxis);
        at.rotate(Math.PI / 2,originPoint.getX(),originPoint.getY());
        x_135Du = at.createTransformedShape(xAxis);
    }

    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.green);
        drawAxis(g2);
//        drawCoords(g2);
        g2.dispose();
    }

    private void drawCoords(Graphics2D g2) {
        if (coords.isEmpty())
            return;
        for (Shape s : coords) {
            g2.draw(s);
        }
    }

    private void drawGrid(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,0.4F
        ));
        for (Axis.Grid s : grids) {
            g2.setColor(s.c);
            g2.draw(s.s);
        }
    }

    private void drawAxis(Graphics2D g2) {
        g2.draw(xAxis);
        g2.draw(yAxis);
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1.0f,
                new float[]{3,5,3},1));
        g2.setColor(Color.cyan);
        g2.draw(x_45du);
        g2.draw(x_135Du);
        g2.setStroke(stroke);
    }

    public void updateAxis(double delta) {

    }
}
