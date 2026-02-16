package com.snl.test.image;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TransparentImageExample extends DiKaErPlus {

    BufferedImage bi;
    BufferedImage bi01;
    double splitx;
    double shift;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        int w = c.getWidth();
        int h = c.getHeight();
        bi = createBufferedImage(w,h);
        fillImage(bi,Color.green);
        bi01 = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
        setBufferedImage(bi01);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1))
            splitx = mousePos.getX();
    }

    private void setBufferedImage(BufferedImage bi) {
        int sqareSize = 60;
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(Color.WHITE);
        int w = bi.getWidth();
        int h = bi.getHeight();
        int row = w / sqareSize;
        int col = h / sqareSize;
        for (int j = 0;j<row;j++)
        {
            for (int i=0;i<col;i++)
            {
                if ((i + j) % 2 == 0) {
                    g2.fillRect( i * sqareSize,j * sqareSize,sqareSize,sqareSize);
                }
            }
        }
        g2.dispose();
    }

    private void fillImage(Image bi, Color c) {
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setColor(c);
        g2.fillRect(0,0, bi.getWidth(null), bi.getHeight(null) );
        g2.dispose();
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        double d = c.getHeight() / 8.0;
        shift += d * delta;
        if (shift > d)
            shift -= d;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        //绘制边框
        g2.setColor(Color.PINK);
        double h = c.getHeight() / 8.0;
        for (int i=-1;i<8;i++) {
            g2.fillRect(0, (int) (h * i + shift),c.getWidth(), (int) (h / 2));
        }
        g2.drawImage(bi01,null,0,0);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TransparentImageExample());
    }
}
