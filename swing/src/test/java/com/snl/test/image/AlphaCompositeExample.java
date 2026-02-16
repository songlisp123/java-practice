package com.snl.test.image;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import static java.awt.AlphaComposite.*;
import static java.awt.AlphaComposite.Xor;

public class AlphaCompositeExample extends DiKaErPlus {

    BufferedImage statBi,anBi;
    BufferedImage dBi;
    BufferedImage sBi;
    LineMetrics lm;
    int padLeft,padRight,hd;
    int padAbove,padBottom,vd;
    int padHeight;
    int rw,rh;
    GradientPaint srcPaint,desPaint;
    final String[] compositeRules = new String[] {"Src",
            "SrcOver",
            "SrcIn",
            "SrcOut",
            "SrcAtop",
            "Clear",
            "Dst",
            "DstOver",
            "DstIn",
            "DstOut",
            "DstAtop",
            "Xor",
    };
    private static final AlphaComposite[] compObjs = {
            Src, SrcOver, SrcIn, SrcOut, SrcAtop, Clear,
            Dst, DstOver, DstIn, DstOut, DstAtop, Xor, };
    final int NUM_RULES =compositeRules.length;
    final int HALF_NUM_RULED =NUM_RULES / 2;
    final Font f = new Font("隶书",Font.PLAIN|Font.BOLD,15);
    int fadeIndex;
    final String[] fadeNames = new String[]{
            "源 => 透明  目的:不透明",
            "源 => 不透明  目的 => 透明",
            "源 不透明  目的 => 不透明",
    };
    final float[][] fadeFractors = {
            {1.0f,-0.1f,0.0f,1.0f,0.0f,1.0f},
            {.0f,0.1f,1.0f,1.0f,-0.1f,0.0f},
            {1.0f,0.0f,1.0f,0.0f,0.1f,1.0f},
    };

    float desAlpha,srcAlpha;
    String fadeS = fadeNames[0];
    GeneralPath srcPath,desPath;


    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetView();
        initialBi();
    }

    private void initialBi() {
        FontRenderContext frc = new FontRenderContext(null,false,false);
        lm = f.getLineMetrics(compositeRules[0],frc);

        int w = c.getWidth();
        int h = c.getHeight();
        padLeft = (w < 150) ? 10 : 15;
        padRight = padLeft;
        hd = padLeft + padRight;
        padBottom = (h < 250) ? 1 : 2;
        padAbove = (int) (padBottom + lm.getHeight());
        vd = padAbove + padBottom;
        rw = w / 4 -hd;
        rw = Math.max(6,rw);
        rh = (h - vd) / HALF_NUM_RULED - vd;
        rh = Math.max(6,rh);
        padHeight = rh + vd;

        srcPath = new GeneralPath();
        srcPath.reset();
        srcPath.moveTo(0,0);
        srcPath.lineTo(rw,0);
        srcPath.lineTo(0,rh);
        srcPath.closePath();

        desPath = new GeneralPath();
        desPath.reset();
        desPath.moveTo(0,0);
        desPath.lineTo(rw,0);
        desPath.lineTo(rw,rh);
        desPath.closePath();

        dBi = new BufferedImage(rw,rh,BufferedImage.TYPE_INT_ARGB);
        sBi = new BufferedImage(rw,rh,BufferedImage.TYPE_INT_ARGB);

        statBi = new BufferedImage(w/2,h,BufferedImage.TYPE_INT_RGB);
        anBi = new BufferedImage(w/2,h,BufferedImage.TYPE_INT_RGB);
        desPaint = new GradientPaint(
                0,0,new Color(1.0f,0.0f,0.0f,1.0f),
                0,rh,new Color(1.0f,0.0f,.0f,.0f)
        );
        srcPaint = new GradientPaint(
                0,0,new Color(0.0f,.0f,1.0f,1.0f),
                rw,0,new Color(.0f,.0f,1.0f,.0f)
        );
        drawComBi(statBi,true);

        srcAlpha = fadeFractors[fadeIndex][0];
        desAlpha = fadeFractors[fadeIndex][3];
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        srcAlpha += (float) (fadeFractors[fadeIndex][1] * delta);
        desAlpha += (float) (fadeFractors[fadeIndex][4] * delta);
        fadeS = fadeNames[fadeIndex];
        if (srcAlpha < 0.0F || srcAlpha > 1.0f || desAlpha < .0F || desAlpha > 1.0F)
        {
            srcAlpha = fadeFractors[fadeIndex][2];
            desAlpha = fadeFractors[fadeIndex][5];
            if (fadeIndex++ == fadeFractors.length - 1)
            {
                fadeIndex = 0;
            }
        }
    }

    private void drawComBi(BufferedImage bi, boolean gradient) {
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,bi.getWidth(), bi.getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(f);

        Graphics2D gd = dBi.createGraphics();
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Graphics2D gs = sBi.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 0,y = 0;
        int yy = (int) (lm.getHeight() + vd);
        for (int i = 0;i<NUM_RULES;i++) {
            y = (i == 0 || i == HALF_NUM_RULED) ? yy : y + padHeight;
            x = (i >= HALF_NUM_RULED) ? bi.getWidth() / 2 + padLeft : padLeft;
            g2.translate(x,y);

            //绘制目标图像
            gd.setComposite(AlphaComposite.Clear);
            gd.fillRect(0,0,rw,rh);
            //设置源
            gd.setComposite(AlphaComposite.Src);
            if (gradient)
            {
                gd.setPaint(desPaint);
                gd.fillRect(0,0,rw,rh);
            }
            else {
                gd.setPaint(new Color(1.0f,.0f,.0f,desAlpha));
                gd.fill(desPath);
            }

            //绘制源
            gs.setComposite(AlphaComposite.Clear);
            gs.fillRect(0,0,rw,rh);
            gs.setComposite(AlphaComposite.Src);
            if (gradient){
                gs.setPaint(srcPaint);
                gs.fillRect(0,0,rw,rh);
            }else {
                gs.setPaint(new Color(0.0f,.0f,1.0f,srcAlpha));
                gs.fill(srcPath);
            }

            //将源复制到目的图像上
            gd.setComposite(compObjs[i]);
            gd.drawImage(sBi,0,0,null);

            //绘制目的图像
            g2.drawImage(dBi,0,0,null);
            g2.setColor(Color.WHITE);
            g2.drawString(compositeRules[i],0,-lm.getDescent());
            g2.drawRect(0,0,rw,rh);
            g2.translate(-x,-y);
        }
        gs.dispose();
        gd.dispose();
        g2.dispose();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        g2.drawImage(statBi,0,0,null);
        drawComBi(anBi,false);
        g2.drawImage(anBi,c.getHeight() / 2,0,null);
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout layout = new TextLayout("alpha混合规则",f,frc);
        layout.draw(g2,15.0f, (float) (layout.getBounds().getHeight()));

        layout = new TextLayout(fadeS,f,frc);
        float x = (float) (c.getWidth() * 0.75 - layout.getBounds().getWidth() / 2);
        if ((x + layout.getBounds().getWidth()) > c.getWidth()) {
            x = (float) (c.getWidth() - layout.getBounds().getWidth());
        }
        layout.draw(g2,x,(float) (layout.getBounds().getHeight() + 3.0));
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new AlphaCompositeExample());
    }
}
