package com.snl.test.java2D.move;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.UTIL.Star;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class GalleryMotion extends DiKaErPlus {
    final int MAX_STARS = 5000;
    Star[] stars;

    Vector2D sunPos,sunPosCopy;
    double sunRadius;
    boolean showStats;
    double shuixingR,shuiXingRD;
    double shuiXingRot,shuiXingTheta;

    double jinxingR, jingxingRD;
    double jingxingRot, jingxingTheta;

    double earthR,earthRD;
    double earthRot,earthTheta;

    double huoxingR,huoxingRD;
    double huoxingRot,huoxingTheta;

    double muxingR,muxingRD;
    double muxingRot,muxingTheta;

    double tuxingR,tuxingRD;
    double tuxingRot,tuxingTheta;

    double tianwangR,tianwangRD;
    double tianwangRot,tianwangTheta;

    double haiwangR,haiwangRD;
    double haiwangRot,haiwangTheta;

    boolean stopping;


    public GalleryMotion() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        //填充星星
        stopping = false;
        stars = new Star[MAX_STARS];
        showStats = true;
        fillStarts();
        sunPos = new Vector2D();
        sunRadius = 3;
        shuixingR = 0.5;
        jinxingR = 0.75;
        earthR = 0.46;
        huoxingR = 1.2;
        muxingR = 1.35;
        tuxingR = 2.4;
        tianwangR = 1.56;
        haiwangR = 2.15;
        shuiXingRD = sunRadius + 0.5;
        jingxingRD = shuiXingRD + 1.3;
        earthRD = jingxingRD + 1.65;
        huoxingRD = earthRD + 3.5;
        muxingRD = huoxingRD + 4;
        tuxingRD = muxingRD + 3.6;
        tianwangRD = tuxingRD + 2.5;
        haiwangRD = tianwangRD + 5;
        shuiXingRot = 0;
        jingxingRot = 0;
        earthRot = 0;
        huoxingRot = 0;
        muxingRot = 0;
        tuxingRot = 0;
        tianwangRot = 0;
        haiwangRot = 0;
        shuiXingTheta = Math.PI / 2;
        jingxingTheta = Math.PI / 3;
        earthTheta = Math.PI / 4;
        huoxingTheta = Math.PI / 5;
        muxingTheta = Math.PI / 6;
        tuxingTheta = Math.PI / 10;
        tianwangTheta = Math.PI / 15;
        haiwangTheta = Math.PI / 30;
    }

    private void fillStarts() {
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
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //TODo
        if (showStats)
            drawStars(g2,stars);
        g2.setColor(Color.YELLOW);
        g2.drawString("按下 空格键 显示/关闭星空",30,130);
        g2.drawString("按下 p 键停止/开启运动",30,150);
        drawSun(g2,sunPos,sunRadius);
        g2.dispose();
    }

    private void drawSun(Graphics2D g, Vector2D sun, double r) {
        Graphics2D g2 = (Graphics2D) g.create();

        Matrix3x3f view = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        Vector2D v = view.mul(sun); //世界坐标

        Vector2D radius = new Vector2D(2 * r,2 * r);
        radius = scale.mul(radius);

        Point2D maskCenter = new Point2D.Double(v.getX(),v.getY());
        float maskR = 10;
        float[] fractions = new float[]{
                0.0f,0.3f,0.6f,1.0f
        };
        Color[] colors = new Color[]{
                Color.YELLOW,Color.ORANGE,new Color(255,200,0,180),
                new Color(255,200,0,120)
        };
        RadialGradientPaint paint = new RadialGradientPaint(maskCenter,maskR,fractions,colors);
        g2.setPaint(paint);

        //获取中心点
        double centerX = v.getX();
        double centerY = v.getY();
        //获取宽高
        double w = radius.getX();
        double h = radius.getY();
        //左上角点
        double leftX = centerX - w / 2.0;
        double lefty = centerY - h / 2.0;

        Shape s = new Ellipse2D.Double(
                leftX,lefty,w,h
        );

        g2.fill(s);
        g2.setColor(Color.WHITE);
        g2.drawString("太阳", (int) centerX, (int) (centerY - 10));

        //绘制水星的轨道
        double d = drawGuiDao(g2, v, shuiXingRD, scale);
        drawPlanet(g2, sun, shuixingR, view, scale,d, shuiXingRot);

         d= drawGuiDao(g2, v, jingxingRD, scale);
         drawPlanet(g2,sun, jinxingR,view,scale,d, jingxingRot);

         d = drawGuiDao(g2,v,earthRD,scale);
         drawPlanet(g2,sun,earthR,view,scale,d,earthRot);

         d = drawGuiDao(g2,v,huoxingRD,scale);
         drawPlanet(g2,sun,huoxingR,view,scale,d,huoxingRot);

         d = drawGuiDao(g2,v,muxingRD,scale);
         drawPlanet(g2,sun,muxingR,view,scale,d,muxingRot);

         d = drawGuiDao(g2,v,tuxingRD,scale);
         drawPlanet(g2,sun,tuxingR,view,scale,d,tuxingRot);

         d = drawGuiDao(g2,v,tianwangRD,scale);
         drawPlanet(g2,sun,tianwangR,view,scale,d,tianwangRot);


        d= drawGuiDao(g2,v,haiwangRD,scale);
        drawPlanet(g2,sun,haiwangR,view,scale,d,haiwangRot);
    }


    private void drawPlanet(Graphics2D g2, Vector2D p, double r, Matrix3x3f mat,Matrix3x3f scale, double v, double rot) {
        g2.setColor(Color.BLUE);
        Matrix3x3f m = Matrix3x3f.identity();
        m = m.mul(Matrix3x3f.rotate(rot));
        m = mat.mul(m);
        m = m.mul(Matrix3x3f.translate(v,0));
        Vector2D v1 = m.mul(p);
        Vector2D radius = new Vector2D(2 * r,2 * r);
        radius = scale.mul(radius);
        //获取中心点
        double centerX = v1.getX();
        double centerY = v1.getY();
        //获取宽高
        double w = radius.getX();
        double h = radius.getY();
        //左上角点
        double leftX = centerX - w / 2.0;
        double lefty = centerY - h / 2.0;

        Shape s = new Ellipse2D.Double(
                leftX,lefty,w,h
        );

        g2.fill(s);
    }

    private double drawGuiDao(Graphics2D g2, Vector2D v, double r, Matrix3x3f scale) {
        g2.setColor(Color.WHITE);
        //获取中心点
        double centerX = v.getX();
        double centerY = v.getY();
        Vector2D radius = new Vector2D(2 * r,2 * r);
        radius = scale.mul(radius);
        //获取宽高
        double w = radius.getX();
        double h = radius.getY();
        //左上角点
        double leftX = centerX - w / 2.0;
        double lefty = centerY - h / 2.0;

        Shape a = new Ellipse2D.Double(
                leftX,lefty,w,h
        );
        g2.draw(a);
        return  r;
    }

    private <T extends Star> void drawStars(Graphics2D g2, T[] stars) {
        Matrix3x3f view = getViewportTransform();
        for (Star star : stars)
            star.draw(g2,view,minS,maxS);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            showStats = !showStats;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P))
        {
            stopping = !stopping;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //更新星星
        if (showStats)
            updateStars(delta,stars);
        if (!stopping) {
            shuiXingRot += shuiXingTheta * delta;
            jingxingRot += jingxingTheta * delta;
            earthRot += earthTheta * delta;
            huoxingRot += huoxingTheta * delta;
            muxingRot += muxingTheta * delta;
            tuxingRot += tuxingTheta * delta;
            tianwangRot += tianwangTheta * delta;
            haiwangRot += haiwangTheta * delta;
        }
    }

    private  <T extends Star> void updateStars(double delta, T[] stars) {
        for (Star star : stars)
            star.update(delta,minS,maxS);
    }

    public static void main(String[] args) {
        launchGame(new GalleryMotion());
    }
}
