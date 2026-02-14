package com.snl.test.java2D.UTIL;

import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    //这个函数在本地设备上居中容器
    public static void centerContainer(Container container) {
        //判断是否可现实
        if (!container.isVisible())
            container.setVisible(true);
        //判断是否可显示
        if (!container.isDisplayable())
            container.addNotify();
        //获取本地设备
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = container.getPreferredSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / 2;
        container.setLocation(x,y);
    }

    /**
     * 居中顶级框架
     * @param container 顶级容器
     * @param component 容器内子组件
     */
    public static void resizeFrame(Container container,Component component) {
        if (!container.isDisplayable()) container.addNotify();
        Insets insets = container.getInsets();
        Dimension size = component.getPreferredSize();
        int w = insets.left + size.width + insets.right;
        int h = insets.top + size.height + insets.bottom;
        container.setSize(w, h);
    }

    /**
     * 窗口被关闭时的请求
     */
    public static void showClosingDialog(Component c) {
        int answer = JOptionPane.showConfirmDialog(c,
                "是否要退出",
                "退出",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null);
        if(answer == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    public static void sleep(long l)
    {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void getPingMuViewPort(Component c) {}

    /**
     * 获取当前底层设备的显示模式
     * @return 底层设备的显示模式
     */
    public static DisplayMode getCurrentDisplayMode() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        return screenDevice.getDisplayMode();
    }

    /**
     * 获取当前图形设备的所有显示模式
     * @return 所有显示模式
     */
    public static DisplayMode[] listAllDisplayModes() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        return screenDevice.getDisplayModes();
    }

    /**
     * 获取视口缩放矩阵
     * @param c 窗口组件
     * @param wordWidth 世界坐标系宽
     * @param wordHeight 世界坐标系高
     * @return 视口矩阵
     */
    public static Matrix3x3f getViewportTransform(Component c, int wordWidth, int wordHeight) {
        Dimension screenSize = c.getSize();
        int sx = screenSize.width / wordWidth;
        int sy = screenSize.height / wordHeight;
        int tx = screenSize.width / 2;
        int ty = screenSize.height / 2;
        Matrix3x3f mat  = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.translate(tx,ty));
        mat = mat.mul(Matrix3x3f.scale(sx,-sy));
        return mat;
    }

    /**
     * 将点转换成向量
     * @param p 点
     * @return 新的向量
     */
    public static Vector2D pointConvertToVector(Point2D p) {
        return new Vector2D(p.getX(),p.getY());
    }

    /**
     * 将向量转换成点
     * @param v 转换向量
     * @return 新的点
     */
    public static Point2D vectorCovertToPoint(Vector2D v) {
        return new Point2D.Double(v.getX(),v.getY());
    }

    /**
     * 获取世界坐标转换系
     * @param c 屏幕组件
     * @param wordWidth 世界高度
     * @param wordHeight 世界宽度
     * @return 世界坐标转换矩阵
     */
    public static Matrix3x3f getReverseWorldTransForm(Component c, int wordWidth, int wordHeight) {
        Dimension screenSize = c.getSize();
        double sx = (double) wordWidth / screenSize.width;
        double sy =  (double) wordHeight / screenSize.height;
        int tx = screenSize.width / 2;
        int ty = screenSize.height / 2;
        Matrix3x3f mat  = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.scale(sx,-sy));
        mat = mat.mul(Matrix3x3f.translate(-tx,-ty));
        return mat;
    }

    public static void drawPolygon(Graphics2D g2, Vector2D[] polygon) {
        if(polygon.length == 0)
            return;
        Vector2D p;
        Vector2D f = polygon[polygon.length -1];
        for (Vector2D v : polygon) {
            p = v;
            Line2D l = new Line2D.Double(
                    f.getX(),f.getY(),
                    p.getX(),p.getY()
            );
            g2.draw(l);
            f = p;
        }
    }

    public static void drawPolygon(Graphics2D g2, Point2D[] polygon) {
        if(polygon.length == 0)
            return;
        Point2D p;
        Point2D f = polygon[polygon.length -1];
        for (Point2D point : polygon) {
            p = point;
            Line2D l = new Line2D.Double(f,p);
            g2.draw(l);
            f = p;
        }
    }

    public static void drawPolygonForVector(Graphics2D g2, List<Vector2D> polygon)
    {
        drawPolygon(g2,polygon.toArray(Vector2D[]::new));
    }

    public static void drawPolygonForPoint(Graphics2D g2, List<Point2D> polygon)
    {
        drawPolygon(g2,polygon.toArray(Point2D[]::new));
    }

    public static Matrix3x3f getScaleViewPortMat(Component c, int wordWidth,int worldHeight) {
        Dimension screenSize = c.getSize();
        int sx = screenSize.width / wordWidth;
        int sy = screenSize.height / worldHeight;
        Matrix3x3f mat  = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.scale(sx,sy));
        return mat;
    }

    public static Shape reShape(Shape shape, Matrix3x3f mat) {
        if (shape == null)
            return null;
        if (mat == null)
            return null;
        AffineTransform af =
                Matrix3x3f.convertIntoAffineTransform(mat);
        PathIterator pi = shape.getPathIterator(af);
        double x,y;
        Point2D p,f;
        List<Point2D> points = new ArrayList<>();
        GeneralPath result;
        while (!pi.isDone()) {
            double[] cords = new double[6];
            int i = pi.currentSegment(cords);
            switch (i) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    x = cords[0];
                    y = cords[1];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    break;
                case PathIterator.SEG_QUADTO:
                    x = cords[0];
                    y = cords[1];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    x = cords[2];
                    y = cords[3];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    break;
                case PathIterator.SEG_CUBICTO:
                    x = cords[0];
                    y = cords[1];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    x = cords[2];
                    y = cords[3];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    x = cords[4];
                    y = cords[5];
                    p = new Point2D.Double(x,y);
                    points.add(p);
                    break;
                case PathIterator.SEG_CLOSE:
                default:
                    break;
            }
            pi.next();
        }
        result = new GeneralPath();
        Point2D last = points.getLast();
        result.moveTo(last.getX(),last.getY());
        for (int i = 1;i<points.size();i++) {
            f = points.get(i);
            result.lineTo(f.getX(), f.getY());
        }
        result.closePath();
        return result;
    }

    public static Matrix3x3f getTranslationMat(Component c, int wordWidth, int wordHeight) {
        Dimension screenSize = c.getSize();
        int tx = screenSize.width / 2;
        int ty = screenSize.height / 2;
        Matrix3x3f mat  = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.translate(tx,ty));
        return mat;
    }

    public static Matrix3x3f getReverseScaleViewPortMat(Component c, int wordWidth, int wordHeight) {
        Dimension screenSize = c.getSize();
        double sx = (double) wordWidth / screenSize.width;
        double sy =  (double) wordHeight / screenSize.height;
        Matrix3x3f mat  = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.scale(sx,-sy));
        return mat;
    }

    public static boolean pointInAABB(Vector2D pos,Vector2D min,Vector2D max)
    {
        return pos.getX() > min.getX() && pos.getX() < max.getX()
                && pos.getY() > min.getY() && pos.getY() < max.getY();
    }

}
