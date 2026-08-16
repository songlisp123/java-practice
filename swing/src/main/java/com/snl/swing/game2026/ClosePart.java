package com.snl.swing.game2026;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ClosePart extends PartAdpater {

    private final Component c;

    //离屏图像
    private BufferedImage bi;
    //剪切形状
    private Shape shape;

    public ClosePart(Component c, double start, double end) {
        super(start,end);
        this.c = c;
    }


    @Override
    public void render(Graphics2D g2) {
        g2.setClip(shape);
        g2.drawImage(bi,0,0,null);
    }

    @Override
    public void timingEvent(double fraction) {
        if (bi == null)
        {
            bi = new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.fillRect(0,0,bi.getWidth(),bi.getHeight());
            g2.dispose();
        }


        int w = c.getWidth();
        int h = c.getHeight();

        if (shape == null) {
            shape = new Rectangle2D.Double(0,0,w,h);
        }
        int x = (int) (w / 2.0 * fraction);
        int y = (int) (h / 2.0 * fraction);
        w = (int) (w + (-w) * fraction);
        h = (int) (h + (-h) * fraction);
        ((Rectangle2D)shape).setRect(x,y,w,h);
    }

    @Override
    public void flush() {
        super.flush();
        bi.flush();
        shape = null;
    }
}
