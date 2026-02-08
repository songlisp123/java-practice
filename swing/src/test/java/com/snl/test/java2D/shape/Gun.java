package com.snl.test.java2D.shape;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class Gun  {

    Shape shape;

    //枪托
    private float gunStockWidth;
    private float gunStockHeight;

    //握把
    private float handleWidth;
    private float handleHeight;

    //枪管
    private float barrelWidth;
    private float barrelHeight;

    //狙击镜
    private float scopeWidth;
    private float scopeHeight;

    private Point2D leftPoint;

    public Gun() {
        init();
    }

    public  Gun(float x,float y) {
        leftPoint = new Point2D.Float(x,y);
        init();
    }


    private void init() {
        float x = (float) leftPoint.getX();
        float y = (float) leftPoint.getY();
        gunStockWidth = 50;
        gunStockHeight = 25;
        handleWidth = gunStockWidth;
        handleHeight = gunStockHeight;
        barrelWidth = 4 * gunStockWidth;
        barrelHeight = gunStockHeight / 2;

        scopeWidth = 5;
        scopeHeight = 10;

        Shape stock = new Rectangle2D.Double(x,y,gunStockWidth,gunStockHeight);
        Area a1 = new Area(stock);
        stock = new Rectangle2D.Double(x + gunStockWidth,y,handleWidth,handleHeight);
        Area a2;
        a2 = new Area(stock);
        a1.add(a2);
        //圆心的位置
        float rX = x + gunStockWidth;
        float rY = y + gunStockHeight / 2;
        stock = new Ellipse2D.Double(rX,rY,scopeHeight * 8,scopeHeight * 8);
        a2 = new Area(stock);
        a1.subtract(a2);
        stock = new Rectangle2D.Double(x + gunStockWidth + handleWidth,
                y-barrelHeight / 2,barrelWidth,barrelHeight);
        a2 = new Area(stock);
        a1.add(a2);
        float x0 = x + gunStockWidth + handleWidth + barrelWidth /2;
        float y0 = y-barrelHeight / 2;
        float x1 = x0  + barrelWidth / 2;

        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,2);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        y0 += barrelHeight / 2;
        stock = new Rectangle2D.Double(x0,y0,200,2);
        a2 = new Area(stock);
        a1.subtract(a2);

        x0 = x + gunStockWidth + handleWidth + barrelWidth / 4;

        x1 = x0 + barrelWidth / 4;
        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,barrelHeight);
            AffineTransform af = AffineTransform.getShearInstance(-0.1,0);
            stock = af.createTransformedShape(stock);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        x0 = x + gunStockWidth / 10;
        x1 = x0 + gunStockWidth / 2;
        y0 = y+gunStockHeight / 4;
        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,gunStockHeight/2);
            AffineTransform af = AffineTransform.getShearInstance(0.1,0);
            stock = af.createTransformedShape(stock);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        x0 = x + gunStockWidth + handleWidth;
        y0 = y-barrelHeight / 2;

        x0 += barrelWidth / 8;
        y0 -= scopeHeight;
        stock = new Rectangle2D.Double(x0,y0,scopeWidth,scopeHeight);
        a2 = new Area(stock);
        a1.add(a2);

        x0 += barrelWidth / 6;
        stock = new Rectangle2D.Double(x0,y0,scopeWidth,scopeHeight);
        a2 = new Area(stock);
        a1.add(a2);

        x0 = x + gunStockWidth + handleWidth + barrelWidth - barrelWidth / 6;
        y0 = y+barrelHeight / 8;
        stock = new Ellipse2D.Double(x0,y0,barrelHeight / 2,barrelHeight / 2);
        a2 = new Area(stock);
        a1.add(a2);

        shape = a1;
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//        //设置纹理
//        BufferedImage bi = getTextureImage();
//        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
//        TexturePaint paint = new TexturePaint(bi,r);
//        g2.setPaint(Color.yellow);
//        g2.setPaint(paint);
//        g2.draw(shape);
//        AffineTransform af = AffineTransform.getScaleInstance(0.8,0.8);
//        shape = af.createTransformedShape(shape);
//        af = AffineTransform.getTranslateInstance(0.2 * leftPoint.getX(),0.2 * leftPoint.getY());
//        shape = af.createTransformedShape(shape);
//        g2.fill(shape);
//        for (int i= 1;i<=3;i++) {
//            int i1 = i * 50;
//            af = AffineTransform.getTranslateInstance(0,i1);
//            Shape a;
//            a = af.createTransformedShape(o);
//            g2.fill(a);
//        }
//        g2.dispose();
//    }

    private BufferedImage getTextureImage() {
        int size = 2;
        BufferedImage bi = new BufferedImage(
                size,size,BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(Color.lightGray);
        g2.fillRect(0,0,size / 2 ,size /2);
        g2.setPaint(Color.green);
        g2.fillRect(size / 2,0,size,size / 2);
        g2.setPaint(Color.blue);
        g2.fillRect(0,size / 2,size /2 ,size);
        g2.setPaint(Color.white);
        g2.fillRect(size / 2,size /2 ,size,size);
        return bi;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        //设置纹理
        BufferedImage bi = getTextureImage();
        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        TexturePaint paint = new TexturePaint(bi,r);
        g2.setPaint(Color.yellow);
        g2.setPaint(paint);
        g2.draw(shape);
        g2.dispose();
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
