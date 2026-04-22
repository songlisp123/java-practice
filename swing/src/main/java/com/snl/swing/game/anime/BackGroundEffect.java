package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BackGroundEffect implements Part {

    //结束帧和开始帧
    private int end,start;
    //背景颜色数组
    private Color[] colors;

    private int index;

    BufferedImage bim;

    private Graphics2D big;

    private final Canvas c;

    public BackGroundEffect(int start, int end,Canvas c,Color...colors) {
        this.start = start;
        this.end = end;
        this.colors = colors;
        this.c = c;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        big.setPaint(colors[index++ % colors.length]);
        big.fillRect(0,0, bim.getWidth(),bim.getHeight());
        g2.drawImage(bim, 0, 0, null);
    }

    @Override
    public void step(int w, int h) {
        if (bim == null) {
            bim = new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
            big = bim.createGraphics();
        }
    }

    @Override
    public void reset(int w, int h) {

    }
}
