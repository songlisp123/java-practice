package com.snl.swing.game2026;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class PartImplement extends PartAdpater implements  ImageObserver {

    private  BufferedImage bi;


    public PartImplement(double start, double end) {
        super(start, end);
    }

    public PartImplement(double start, double end, BufferedImage bi) {
        super(start, end);
        this.bi = bi;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(bi, 200, 200, this);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }

    @Override
    public void timingEvent(double fraction) {
        super.timingEvent(fraction);
    }
}
