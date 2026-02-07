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
import java.awt.geom.Point2D;

public class DiKaErPlus extends SimpleGameFramePlus implements MouseWheelListener {

    //是否拖动坐标系
    protected boolean dragging;
    //鼠标点，第二个变量是每帧鼠标移动的距离
    /* 注意：这两个变量都是以像素为单位 */
    protected Vector2D mousePos,mouseDelta;
    //坐标轴
    protected AxisPlus axis;
    //原点
    Point2D originPoint;

    public DiKaErPlus() throws HeadlessException {
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
    }

    @Override
    protected void reset() {
        super.reset();
        mouseDelta = new Vector2D();
        axis.createAxis(getViewportTransform(),c);
        //TODO
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
        super.draw(g);
        //TODO
        axis.draw(g2);
        drawPoint(g2,originPoint);
        g2.dispose();
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
