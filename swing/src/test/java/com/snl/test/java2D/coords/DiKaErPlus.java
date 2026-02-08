package com.snl.test.java2D.coords;

import com.snl.test.java2D.UTIL.AxisPlus;
import com.snl.test.frame.SimpleGameFramePlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

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
    //窗口矩阵
    protected Vector2D minS,maxS;

    public DiKaErPlus() throws HeadlessException {
        super();
        addMouseWheelListener(this);
    }

    //**********************************************************************//
    /* ******************          游戏初始化         *********************** */
    //**********************************************************************//

    @Override
    protected void gameInitial() {
        super.gameInitial();
        mousePos = new Vector2D();
        mouseDelta = new Vector2D();
        //创建轴
        axis = new AxisPlus();
        axis.createAxis(getViewportTransform(),c,wordWidth);
        //初始化原点
        originPoint = new Point2D.Double();
        //初始化屏幕左下角点
        resetPos();
        //TODO
    }

    //**********************************************************************//
    /* ******************          游戏循环         *********************** */
    //**********************************************************************//

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_H))
        {
            viewMat = Matrix3x3f.identity();
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }
        //获取当前坐标点
        Vector2D pos = new Vector2D(mouseInputEvent.getCurrentPoint());
        //获取坐标在当前帧移动的距离
        mouseDelta = pos.sub(mousePos);
        //将当前帧的鼠标坐标复制
        mousePos = pos;
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON2);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (dragging)
        {
            // 像素 → 世界单位
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D v = re.mul(mouseDelta);
            viewMat = Matrix3x3f.translate(-v.getX(),v.getY()).mul(viewMat);
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR));
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }
        else
            setCursor(null);
        //TODO
        resetPos();
        Matrix3x3f r = viewMat.getReverseTranslation();
        minS = r.mul(minS);
        maxS = r.mul(maxS);
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

    //**********************************************************************//
    /* ******************          绘制多边形         *********************** */
    //**********************************************************************//

    //绘制点
    protected void drawPoint(Graphics2D g2, Point2D point) {
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

    //绘制AABB矩形
    protected void drawAABB(Graphics2D g2, Vector2D min, Vector2D max) {
        Vector2D left = new Vector2D(min.getX(),max.getY());
        Vector2D bottom = new Vector2D(max.getX(), min.getY());

        Matrix3x3f view = getViewportTransform();
        Vector2D leftS = view.mul(left);
        Vector2D bottomS = view.mul(bottom);

        //左上角点
        double sx = leftS.getX();
        double sy = leftS.getY();

        double w = Math.abs(bottomS.getX() - leftS.getX());
        double h = Math.abs(bottomS.getY() - leftS.getY());

        Shape s = new Rectangle2D.Double(sx,sy,w,h);
        g2.fill(s);
    }

    //绘制圆形
    protected void drawCircle(Graphics2D g2,Vector2D p,double r) {
        this.drawEllipse(g2,p,r,r);
    }

    //绘制椭圆
    protected void drawEllipse(Graphics2D g2,Vector2D p,double ra,double rb) {
        Matrix3x3f vt = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        Vector2D c0 = vt.mul(p);
        Vector2D v = new Vector2D(2 * ra,2 * rb);
        v = scale.mul(v);
        //获取坐标
        double w = v.getX();
        double h = v.getY();
        double leftX = c0.getX() - w / 2.0;
        double leftY =c0.getY() - h / 2.0;
        Shape s = new Ellipse2D.Double(leftX,leftY,w,h);
        g2.fill(s);
        //g2.drawString("[%.2f,%.2f]".formatted(p.getX(),p.getY()),
        // (int) c0.getX(), (int) (c0.getY() - 10));
        // Shape radius = new Ellipse2D.Double(leftX,leftY,4,4);
        // g2.fill(radius);
    }

    //绘制多边形
    protected void drawPolygon(Graphics2D g2, Vector2D[] polygon) {
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
            g2.fill(l);
            f = p;
        }
    }

    //绘制多边形2-该方法接受一个坐标列表
    protected void drawPolygon(Graphics2D g2, List<Vector2D> polygon) {
        this.drawPolygon(g2,polygon.toArray(Vector2D[]::new));
    }

    protected void drawShape(Graphics2D g2, TextLayout textLayout, Vector2D p) {
        Matrix3x3f vt = getViewportTransform();
        Vector2D c0 = vt.mul(p);
        //左上角
        Rectangle2D bounds = textLayout.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        float leftX = (float) (c0.getX() - width / 2.0);
        float leftY = (float) (c0.getY() - height / 2.0);
        textLayout.draw(g2,leftX,leftY);
    }

    protected void drawImage(Graphics2D g2,Image image,Vector2D p)
    {
        Matrix3x3f vt = getViewportTransform();
        Vector2D c0 = vt.mul(p);
        //获取缩放
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        //获取坐标
        double leftX = c0.getX() - w / 2.0;
        double leftY = c0.getY() - h / 2.0;
        g2.drawImage(image, (int) leftX, (int) leftY,null);
    }

    //**********************************************************************//
    /* ******************          重置状态         *********************** */
    //**********************************************************************//

    @Override
    protected void reset() {
        super.reset();
        mouseDelta = new Vector2D();
        axis.createAxis(getViewportTransform(),c,wordWidth);
        resetPos();
        //TODO
    }

    private void resetPos() {
        minS = new Vector2D(-wordWidth / 2.0,-wordHeight / 2.0);
        maxS = new Vector2D(wordWidth / 2.0,wordHeight / 2.0);
    }

    //**********************************************************************//
    /* ******************          重置窗口         *********************** */
    //**********************************************************************//

    @Override
    protected void handleResizeEvent(ComponentEvent e) {
        super.handleResizeEvent(e);
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            wordWidth--;
            wordHeight--;
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }

        if (wheelRotation == 1) {
            wordWidth++;
            wordHeight++;
            axis.createAxis(getViewportTransform(),c,wordWidth);
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
