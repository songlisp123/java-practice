package com.snl.test.java2D.UTIL;

import com.snl.test.java2D.vector.Matrix3x3f;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public final class AxisPlus {

    //x轴
    Shape xAxis;
    //y轴
    Shape yAxis;
    //斜率为45度的交叉线
    Shape x_45du;
    //坐标点
    final List<Shape> cords = new ArrayList<>();

    public void createAxis(Matrix3x3f viewport,Component c)
    {
        if (c == null)
            return;
        Point2D p;
        double maxWidth = c.getWidth();
        double maxHeight = c.getHeight();
        //x轴
        Point2D originPoint = new Point2D.Double(); //世界坐标
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(viewport);
        p = mat.mul(originPoint);
        xAxis = new Line2D.Double(p.getX(),p.getY(),maxWidth,p.getY());
        yAxis = new Line2D.Double(p.getX(),p.getY(),p.getX(),-maxHeight);
        createAxisOf45(p);
    }

    public void createAxis(Matrix3x3f viewport,Component c,int worldWidth)
    {
        if (c == null)
            return;
        if (!cords.isEmpty())
            cords.clear();
        Point2D p;
        double maxWidth = c.getWidth();
        double maxHeight = c.getHeight();
        //x轴
        Point2D originPoint = new Point2D.Double(); //世界坐标
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(viewport);
        p = mat.mul(originPoint);
        xAxis = new Line2D.Double(p.getX(),p.getY(),maxWidth,p.getY());
        yAxis = new Line2D.Double(p.getX(),p.getY(),p.getX(),-maxHeight);
        createAxisOf45(p);
        fillCords(maxWidth,worldWidth,p,true);
        fillCords(maxHeight,worldWidth,p,false);
    }

    private void fillCords(double w, int world, Point2D p,boolean x)
    {
        Shape s,r;
        double v;
        AffineTransform af;
        //这都是屏幕坐标
        if  (x) {
            r = new Rectangle2D.Double(0, 0, 1, 8);
            af = AffineTransform.getTranslateInstance(p.getX()-0.5, p.getY()-8);
            v = w - p.getX();
        }
        else {
            r = new Rectangle2D.Double(0, 0, 8, 1);
            af = AffineTransform.getTranslateInstance(p.getX(), p.getY()-0.5);
            v = p.getY();
        }
        //这一点是否是对的？？？？
        //使用矩阵进行转换
        int gap = (int) (w / world);
        int count = (int) (v / gap);
        for (int i = 1;i<=count;i++) {
            if (x)
                af.translate( gap,0);
            else
                af.translate(0,-gap);
            s = af.createTransformedShape(r);
            cords.add(s);
        }
    }

    //创建斜率45°的对角线
    private void createAxisOf45(Point2D p)
    {
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI / 4, p.getX(),p.getY());
        x_45du = at.createTransformedShape(xAxis);
    }

    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.green);
        drawAxis(g2);
        drawCords(g2);
        g2.dispose();
    }

    private void drawCords(Graphics2D g2)
    {
        if (cords.isEmpty())
            return;
        for (Shape s : cords) {
            g2.fill(s);
        }
    }

    private void drawAxis(Graphics2D g2)
    {
        g2.draw(xAxis);
        g2.draw(yAxis);
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1.0f,
                new float[]{3,5,3},1));
        g2.setColor(Color.lightGray);
        g2.draw(x_45du);
        g2.setStroke(stroke);
    }
}
