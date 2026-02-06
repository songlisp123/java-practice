package com.snl.test.TIME.UTIL;

import com.snl.test.frame.util.Utils;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public final class Axis {

    //当前组件
    Component component;
    //x轴
    Shape xAxis;
    //y轴
    Shape yAxis;

    //原点形状
    Shape originPointShape;
    //坐标点
    final List<Shape> coords = new ArrayList<>();
    //网格
    final List<Grid> grids = new ArrayList<>();

    //斜率为45度的交叉线
    Shape x_45du;
    Shape x_135Du;

    int step = 125;

    final RandomGenerator g = RandomGenerator.getDefault();

    public void createAxis(Component component,int worldWidth) {
        if (component == null)
            return;
        if (!coords.isEmpty())
            coords.clear();
        if (!grids.isEmpty())
            grids.clear();
        this.component = component;
        Dimension size = component.getSize();
        //x轴
        int gap = size.width / worldWidth;
        int w = size.width;
        int h = size.height;
        int x = w / 2;
        int y = h / 2;
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.translate(x,y));
        Vector2D v = mat.mul(new Vector2D());
        var originPoint = Utils.vectorCovertToPoint(v);

        xAxis = new Line2D.Double(0,y,w,y);
        fillCoords(gap,originPoint);
        createYAxis(originPoint);
        originPointShape = new Ellipse2D.Double(x- 6,y-6,12,12);
        fillGrids(gap);
        createAxisOf45(originPoint);
    }

    //填充网格
    private void fillCoords(int gap, Point2D originPoint) {
        Shape s,y;
        //这都是屏幕坐标
        Shape r = new Rectangle2D.Double(0,0,1,8);
        //这一点是否是对的？？？？
        AffineTransform af = AffineTransform.getTranslateInstance(originPoint.getX()-0.5,
                originPoint.getY()-8);
        AffineTransform afCopy = (AffineTransform) af.clone(); //保留副本
        //使用矩阵进行转换
        int width = component.getWidth() / 2;
        int count = width / gap;
        for (int i = 1;i<=count;i++) {
            af.translate( gap,0);
            s = af.createTransformedShape(r);
            coords.add(s);
            afCopy.translate(-gap,0);
            y = afCopy.createTransformedShape(r);
            coords.add(y);
        }
    }

    //创建y轴
    private void createYAxis(Point2D originPoint) {
        AffineTransform af = AffineTransform.getRotateInstance(Math.PI / 2,
                originPoint.getX(),originPoint.getY());
        yAxis = af.createTransformedShape(xAxis);
        Shape x;
        List<Shape> shapes = new ArrayList<>();
        for (Shape s : coords)
        {
            x = af.createTransformedShape(s);
            shapes.add(x);
        }
        coords.addAll(shapes);
        shapes.clear();
    }

    //填充网格
    private void fillGrids(int gap) {
//        Matrix3x3f mat = Matrix3x3f.identity();
        Shape l,x,l2,x2;
        Grid g;
        AffineTransform af = AffineTransform.getTranslateInstance(0,0);
        AffineTransform afCopy = (AffineTransform) af.clone();
        AffineTransform afCopy02 = (AffineTransform) af.clone();
        AffineTransform afCopy03 = (AffineTransform) af.clone();
//        Vector2D v;
//        mat = mat.mul(Matrix3x3f.translate(originPoint.getX(),originPoint.getY()));
//        mat = mat.mul(Matrix3x3f.translate(gap,0));
//        v = mat.mul(new Vector2D());
//        l = new Line2D.Double(v.getX(),v.getY(),v.getX(),0);
        int w = component.getWidth() / 2;
        int c = w / gap;
        for (int i=1;i<=c;i++) {
            af.translate(gap,0);
            l = af.createTransformedShape(yAxis);
            g = new Grid(l);
            grids.add(g);
            afCopy02.translate(-gap,0);
            l2 = afCopy02.createTransformedShape(yAxis);
            g = new Grid(l2);
            grids.add(g);
            afCopy.translate(0,-gap);
            x = afCopy.createTransformedShape(xAxis);
            g = new Grid(x);
            grids.add(g);
            afCopy03.translate(0,gap);
            x2 = afCopy03.createTransformedShape(xAxis);
            g = new Grid(x2);
            grids.add(g);
        }
    }

    //创建斜率45°的对角线
    private void createAxisOf45(Point2D originPoint) {
        AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 4,
                originPoint.getX(),originPoint.getY());
        x_45du = at.createTransformedShape(xAxis);
        at.rotate(Math.PI / 2,originPoint.getX(),originPoint.getY());
        x_135Du = at.createTransformedShape(xAxis);
    }

    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        drawAxis(g2);
        g2.draw(originPointShape);
        drawCoords(g2);
        drawGrid(g2);
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
        for (Grid s : grids) {
            g2.setColor(s.c);
            g2.draw(s.s);
        }
    }

    private void drawAxis(Graphics2D g2) {
        g2.draw(xAxis);
        g2.draw(yAxis);
//        Stroke stroke = g2.getStroke();
//        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1.0f,
//                new float[]{3,5,3},1));
//        g2.setColor(Color.cyan);
//        g2.draw(x_45du);
//        g2.draw(x_135Du);
//        g2.setStroke(stroke);
    }

    class Grid {
        int alpha;
        Color c;
        Shape s;
        boolean shinning;

        public Grid(Shape s) {
            this.s = s;
            alpha = g.nextInt(255);
            c = new Color(0, 255, 255, alpha);
        }

        public void update(double delta)
        {
            if (shinning)
            {
                alpha += (int) (g.nextInt(step)* delta);
                if (alpha >= 255)
                {
                    shinning = false;
                    alpha = 255;
                }
            }else {
                alpha -= (int) (g.nextInt(step) * delta);
                if (alpha <= 0)
                {
                    shinning = true;
                    alpha = 0;
                }
            }

            c = new Color(0,255,255,alpha);
        }

    }

    public void updateAxis(double delta) {
        if (grids.isEmpty())
            return;
        for (Grid g : grids)
            g.update(delta);
    }
}

//会发生并发修改异常
