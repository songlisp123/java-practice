package com.snl.test.image;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

public class ColorInterpolationExample extends DiKaErPlus {

    private BufferedImage bi;
    private int[] pixels;
    private int[] clears;
    Vector2D min,max;
    Paint paint;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        min = new Vector2D(-2,-2);
        max = new Vector2D(2,2);
        bi = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = bi.getRaster();
        DataBuffer dataBuffer = raster.getDataBuffer();
        DataBufferInt dataBufferInt = (DataBufferInt) dataBuffer;
        pixels = dataBufferInt.getData();
        clears = new int[pixels.length];
        createPaint();
    }

    private void createPaint() {
        Rectangle2D r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        paint = new TexturePaint(bi,r);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        createColorSquare();
    }

    private void createColorSquare() {
        int w = bi.getWidth();
        int h = bi.getHeight();
        System.arraycopy(clears,0,pixels,0,clears.length);
        //左上
        double tlr = 255;
        double tlg = 0;
        double tlb = 0;

        //左下
        double blr = 0;
        double blg = 0;
        double blb = 255;

        //右上
        double trr = 0;
        double trg = 255;
        double trb = 255;

        //右下
        double brr = 255;
        double brg= 0;
        double brb = 255;

        for (int row=0;row<h;row++)
        {
            //左上
            int lr = (int) (tlr + row * (blr - tlr) / h);
            int lg = (int) (tlg + row * (blg - tlg) / h);
            int lb = (int) (tlb + row * (blb - trb) / h);
            //右侧像素
            int rr = (int) (trr + row * (brr - trr) / h);
            int rg = (int) (trg + row * (brg - trg) / h);
            int rb = (int) (trb + row * (brb - trb) / h);
            for (int col = 0;col < w ;++col)
            {
                int r = lr + col * (rr - lr) / w;
                int g = lg + col * (rg - lg) / w;
                int b = lb + col * (rb - lb) / w;
                int p = row * w + col;
                pixels[p] = 0xFF << 24 | r << 16 | g << 8 | b;
            }
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(paint);
        drawAABB(g2,min,max,true);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new ColorInterpolationExample());
    }
}
