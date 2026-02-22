package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TexturePaintEffect implements Part {

    public static final int INC = 1;             // increasing
    public static final int DEC = 2;             // decreasing
    public static final int OVAL = 4;             // oval
    public static final int RECT = 8;             // rectangle
    public static final int HAF = 16;             // half oval or rect size
    public static final int NF = 32;             // no fill
    public static final int OI = OVAL | INC;
    public static final int OD = OVAL | DEC;
    public static final int RI = RECT | INC;
    public static final int RD = RECT | DEC;
    private Paint p1, p2;
    private int beginning, ending;
    private float incr, index;
    private TexturePaint texture;
    private int type;
    private int size;
    private BufferedImage bimg;
    private Rectangle rect;

    public TexturePaintEffect(int type, Paint p1, Paint p2, int size,
               int beg, int end) {
        this.type = type;
        this.p1 = p1;
        this.p2 = p2;
        this.beginning = beg;
        this.ending = end;
        setTextureSize(size);
        reset(0,0);
    }

    private void setTextureSize(int size) {
        this.size = size;
        bimg = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        rect = new Rectangle(0, 0, size, size);
    }

    @Override
    public int getEnd() {
        return ending;
    }

    @Override
    public int getStart() {
        return beginning;
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        g2.setPaint(texture);
        if ((type & NF) == 0) {
            g2.fillRect(0, 0, w, h);
        }
    }

    @Override
    public void step(int w, int h) {
        Graphics2D g2 = bimg.createGraphics();
        g2.setPaint(p1);
        g2.fillRect(0, 0, size, size);
        g2.setPaint(p2);
        if ((type & OVAL) != 0) {
            g2.fill(new Ellipse2D.Float(0, 0, index, index));
        } else if ((type & RECT) != 0) {
            g2.fill(new Rectangle2D.Float(0, 0, index, index));
        }
        texture = new TexturePaint(bimg, rect);
        g2.dispose();
        index += incr;
    }

    @Override
    public void reset(int w, int h) {
        incr = (float) (size) / (float) (ending - beginning);
        if ((type & HAF) != 0) {
            incr /= 2;
        }
        if ((type & DEC) != 0) {
            index = size;
            if ((type & HAF) != 0) {
                index /= 2;
            }
            incr = -incr;
        } else {
            index = 0.0f;
        }
        index += incr;
    }
}
