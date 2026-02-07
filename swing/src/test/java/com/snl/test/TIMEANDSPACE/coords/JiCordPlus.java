package com.snl.test.TIMEANDSPACE.coords;

import com.snl.test.TIMEANDSPACE.UTIL.AxisPlus;
import com.snl.test.frame.SimpleGameFramePlus;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class JiCordPlus extends SimpleGameFramePlus implements MouseWheelListener {

    //是否拖动坐标系
    protected boolean dragging;
    //鼠标点，第二个变量是每帧鼠标移动的距离
    /* 注意：这两个变量都是以像素为单位 */
    protected Vector2D mousePos,mouseDelta;
    //坐标轴
    protected AxisPlus axis;
    //原点
    Point2D originPoint;
    Point2D cord;

    public JiCordPlus() throws HeadlessException {
        super();
        addMouseWheelListener(this);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        mousePos = new Vector2D();
        mouseDelta = new Vector2D();
        //创建轴
        axis = new AxisPlus();
        axis.createAxis(getViewportTransform(),c);
        //初始化原点
        originPoint = new Point2D.Double();
        //TODO
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //获取当前坐标点
        Vector2D pos = new Vector2D(mouseInputEvent.getCurrentPoint());
        //获取坐标在当前帧移动的距离
        mouseDelta = pos.sub(mousePos);
        //将当前帧的鼠标坐标复制
        mousePos = pos;
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON2);
        //TODO
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1))
        {
            Point2D p = mouseInputEvent.getCurrentPoint();
            Matrix3x3f rt = getReverseWorldTransForm();
            cord = rt.mul(p);
        }
    }

    @Override
    protected void reset() {
        super.reset();
        mouseDelta = new Vector2D();
        axis.createAxis(getViewportTransform(),c);
        //TODO
        cord = null;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (dragging)
        {
            // 像素 → 世界单位
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D v = re.mul(mouseDelta);
            viewMat = Matrix3x3f.translate(v.getX(),v.getY()).mul(viewMat);
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
            ));
            axis.createAxis(getViewportTransform(),c);
        }
        else
            setCursor(null);
        //TODO
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //TODO
        axis.draw(g2);
        drawPoint(g2,originPoint);
        drawPolarCord(g2);
        g2.dispose();
    }

    private void drawPolarCord(Graphics2D g2) {
        if (cord == null)
            return;
        g2.setColor(Color.MAGENTA);
        Matrix3x3f t = getViewportTransform();
        Point2D o = t.mul(cord);//世界坐标
        Point2D p = t.mul(new Point2D.Double()); //世界坐标
        double x = cord.getX(); //世界坐标
        double y = cord.getY(); //世界坐标
        double x1 = o.getX(); //屏幕坐标
        double y1 = o.getY();//屏幕坐标
        double x2 = p.getX(); //原点x坐标
        double y2 = p.getY();//原点y坐标
        double xw,yh;
        xw = x1 - x2;
        yh = y2 - y1;
        Shape l = new Line2D.Double(p,o);
        g2.draw(l);
        double theta = Math.atan2(y,x);
        double degree = Math.toDegrees(theta);
        double r = cord.distance(new Point2D.Double()); //世界距离
        double rD = Math.sqrt(Math.pow(xw,2) + Math.pow(yh,2)); //屏幕距离
        g2.drawString("[%.2f,%.2f°]".formatted(r,degree), (float)x1, (float) y1+ 20);
        g2.drawArc((int) (x2- rD),
                (int) (y2 - rD),
                (int) (2 * rD),
                (int) (2 * rD),0, (int) degree);

    }

    private void drawPoint(Graphics2D g2, Point2D point) {
        g2.setColor(Color.MAGENTA);
        Matrix3x3f mat = getViewportTransform();
        Point2D p = mat.mul(point);
        Shape o = new Ellipse2D.Double(
                p.getX() - 4,p.getY() - 4,
                8,8
        );
        g2.fill(o);
        g2.drawString("[%.2f,%.2f]".formatted(point.getX(),point.getY()),
                (int) p.getX(), (int) (p.getY() - 10));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            wordWidth--;
            wordHeight--;
            axis.createAxis(getViewportTransform(),c);
        }

        if (wheelRotation == 1) {
            wordWidth++;
            wordHeight++;
            axis.createAxis(getViewportTransform(),c);
        }

        if (wordWidth <= 2)
            wordWidth = wordHeight = 2;

        if (wordWidth == WIDTH || wordHeight == HEIGHT)
        {
            wordWidth = WIDTH;
            wordHeight = HEIGHT;
        }
    }
}
