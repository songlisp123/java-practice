package com.snl.test.java2D.renderTime;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.UTIL.Star;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class RenderTestDemo01 extends DiKaErPlus {

    final int MAX_STARS = 5000;
    Star[] stars;

    Vector2D min,minCopy;
    Vector2D max,maxCopy;
    Paint paint;
    Vector2D c0,c0Pos;
    double r0;

    public RenderTestDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        fillStarts();
        resetPos();
        createPaint();
    }

    private void createPaint() {
        BufferedImage bi = getBufferImage();
        Rectangle2D r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        paint = new TexturePaint(bi,r);
    }

    private void resetPos() {
        min = new Vector2D(8,0);
        max = new Vector2D(12,4);

        c0Pos = new Vector2D(0,8);
        r0 = 3.32;
    }

    private void fillStarts() {
        stars = new Star[MAX_STARS];
        Vector2D v;
        double w = wordWidth;
        double h = wordHeight;
        for (int i = 0;i<stars.length;i++) {
            v = new Vector2D(
                    RandomGeneratorClass.random(w),
                    RandomGeneratorClass.random(h));
            stars[i] = new Star(v);
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        updateStars(delta,stars);
        Matrix3x3f m = Matrix3x3f.translate(min.getX(),min.getY());
        minCopy = m.mul(min);

        m = Matrix3x3f.translate(max.getX(),max.getY());
        maxCopy = m.mul(max);

        m = Matrix3x3f.translate(c0Pos.getX(),c0Pos.getY());
        c0 = m.mul(c0Pos);
    }

    @Override
    protected void draw(Graphics g) {
        long start = System.currentTimeMillis();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //TODo
        drawStars(g2,stars);
        g2.setPaint(paint);
        drawAABB(g2,minCopy,maxCopy,true);
        drawCircle(g2,c0,r0);
        g2.setColor(Color.YELLOW);
        g2.drawString("按下 空格键 显示/关闭星空",30,130);
        g2.dispose();
        long end = System.currentTimeMillis();
        System.out.printf("渲染时间：%dms%n", end - start);
    }

    private <T extends Star> void drawStars(Graphics2D g2, T[] stars) {
        Matrix3x3f view = getViewportTransform();
        for (Star star : stars)
//            star.draw(g2, view);
            star.draw(g2, view, minS,maxS); //改善后的版本

    }

    private  <T extends Star> void updateStars(double delta, T[] stars) {
        for (Star star : stars)
//            star.update(delta);
            star.update(delta,minS,maxS); //优化后版本
    }

    public static void main(String[] args) {
        launchGame(new RenderTestDemo01());
    }
}
