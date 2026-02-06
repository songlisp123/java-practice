package com.snl.test.frame.collide;

import com.snl.test.frame.SimpleGameFrame;
import com.snl.test.frame.util.Utils;
import com.snl.test.shape.Gun;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TestCollision extends SimpleGameFrame implements MouseWheelListener {

    private Shape rec01,rec01Copy;
    private Shape rec02,rec02Copy;
    Shape selectShape;
    Color selectColor = Color.CYAN;
    Color defaultColor = Color.lightGray;
    Gun gun;
    Shape gunShape,copy;
    double theta,rot;
    double theta2,rot2;

    Point2D o1 , o1Copy;
    Point2D o2 , o2Copy;
    Point2D o3,o4;
    double r1,r2,r3;
    double rot3,theta3;
    Paint paint;

    public TestCollision() throws HeadlessException {
        super();
        addMouseWheelListener(this);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetShape();
    }

    private void resetShape() {
        theta = Math.PI / 4;
        theta2 = Math.PI / 2;
        theta3 = Math.PI ;
        rec01 = new Rectangle2D.Double(0,0,1,1); //对于遍历来说，前两个参数并不是左上角点，而是左下角点
        rec01Copy = rec01;

        rec02 = new Rectangle2D.Double(-2,-1,1.5,2.5);
        rec02Copy = rec02;

        Matrix3x3f mat = getViewportTransform();
        rec01Copy = Utils.reShape(rec01Copy,mat);
        rec02Copy = Utils.reShape(rec02Copy, mat);
        Matrix3x3f translationMat = getTranslationMat();
        gun = new Gun(0,0);
        copy = gun.getShape();
        gun.setShape(
                Utils.reShape(copy,translationMat)
        );
        gunShape = gun.getShape();

        o1 = new Point2D.Double(0,0);
        r1 = 2;
        r2 = 0.5;
        r3 = 0.1;

        BufferedImage bi = getTextureImage();
        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        paint = new TexturePaint(bi,r);

    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //TODO
        Point2D mouse = mouseInputEvent.getCurrentPoint();
        if (rec02Copy.contains(mouse))
            selectShape = rec02Copy;
        else
            if (rec01Copy.contains(mouse))
                selectShape = rec01Copy;
            else
                selectShape = null;
        if (selectShape != null)
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
            ));
        else
            setCursor(null);


        rot += theta * delta;
        rot2 += theta2 * delta;
        rot3 += theta3 * delta;
        Matrix3x3f mat = getViewportTransform();
        mat = mat.mul(
                Matrix3x3f.rotate(rot)
        );
        rec01Copy = Utils.reShape(rec01,mat);
        rec02Copy = Utils.reShape(rec02,mat);

        Matrix3x3f translationMat = getTranslationMat();
        translationMat = translationMat.mul(
                Matrix3x3f.rotate(rot)
        );
        gunShape = Utils.reShape(copy,translationMat);
        gun.setShape(gunShape);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(defaultColor);
        //TODO
        g2.draw(rec01Copy);
        g2.draw(rec02Copy);
        if (selectShape != null)
        {
            g2.setColor(selectColor);
            g2.fill(selectShape);
        }
//        g2.draw(gunShape);
        gun.draw(g2);
        drawC(g2);
        g2.drawString("按下 鼠标左键 添加点",30,250);
        //字符串
        g2.dispose();
    }

    private void drawC(Graphics2D g2) {
        //绘制圆形
        g2.setPaint(paint);
        Matrix3x3f viewportTransform = getViewportTransform();
        viewportTransform = viewportTransform.mul(
                Matrix3x3f.rotate(rot)
        );
        viewportTransform = viewportTransform.mul(Matrix3x3f.translate(
                2,0
        ));
        o1Copy = viewportTransform.mul(o1);

        Matrix3x3f scale = getScaleViewPortMat();
        Vector2D v = scale.mul(new Vector2D(r1, r1));
        double w = v.getX();
        double h = v.getY();

        Shape s = new Ellipse2D.Double(
                o1Copy.getX() - w / 2,o1Copy.getY() - h / 2.0,
                w,h
        );
        g2.fill(s);

        Matrix3x3f m = Matrix3x3f.identity();
        m = m.mul(Matrix3x3f.rotate(rot2));
        m = viewportTransform.mul(m);
        m = m.mul(Matrix3x3f.translate(r1 ,0));
        o2Copy = m.mul(new Point2D.Double());
        Vector2D v2 = scale.mul(new Vector2D(r2,r2));
        double w2 = v2.getX();
        double h2 = v2.getY();
        Shape s2 = new Ellipse2D.Double(
                o2Copy.getX() - w2 / 2,o2Copy.getY() - h2 / 2,
                w2,h2
        );
        g2.fill(s2);

        Matrix3x3f t = Matrix3x3f.identity();
        t = t.mul(Matrix3x3f.rotate(rot3));
        t = m.mul(t);
        t = t.mul(Matrix3x3f.translate(r2,0));
        o3 = t.mul(new Point2D.Double());
        var v3 = scale.mul(new Vector2D(r3,r3));
        var w3 = v3.getX();
        var h3 = v3.getY();
        var s3 = new Ellipse2D.Double(
                o3.getX() - w3 / 2,o3.getY() - h3 / 2,
                w3, h3
        );
        g2.fill(s3);
        t = t.mul(Matrix3x3f.translate(-2 * r2,0));
        o4 = t.mul(new Point2D.Double());
        var s4 = new Ellipse2D.Double(
                o4.getX() - w3 / 2,o4.getY() - h3 / 2,
                w3, h3
        );
        g2.fill(s4);

    }

    @Override
    protected void reset() {
        super.reset();
        resetShape();
    }

    private BufferedImage getTextureImage() {
        int size = 20;
        BufferedImage bi = new BufferedImage(
                size,size,BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0,0,size / 2 ,size /2);
        g2.setPaint(Color.BLACK);
        g2.fillRect(size / 2,0,size,size / 2);
        g2.setPaint(Color.BLACK);
        g2.fillRect(0,size / 2,size /2 ,size);
        g2.setPaint(Color.WHITE);
        g2.fillRect(size / 2,size /2 ,size,size);
        return bi;
    }

    public static void main(String[] args) {
        launchGame(new TestCollision());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            wordWidth--;
            wordHeight--;
            axis.createAxis(c,wordWidth);
            resetShape();
        }

        if (wheelRotation == 1) {
            wordWidth++;
            wordHeight++;
            axis.createAxis(c,wordWidth);
            resetShape();
        }

        if (wordWidth <= 2)
            wordWidth = wordHeight = 2;
    }
}
