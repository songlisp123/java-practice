package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CloseEffect implements Part {

    private final Component surf;
    static final int WID = 1;
    static final int HEI = 2;
    static final int OVAL = 4;
    static final int RECT = 8;
    static final int RAND = 16;
    static final int ARC = 32;
    private int type;
    private int beginning, ending;
    private BufferedImage bimg;
    private Shape shape;
    private double zoom, extent;
    private double zIncr, eIncr;
    private boolean doRandom;

    public CloseEffect(int type, int beg, int end, Component surf) {
        this.type = type;
        this.beginning = beg;
        this.ending = end;
        this.surf = surf;
        zIncr = -(2.0 / (ending - beginning));
        eIncr = 360.0 / (ending - beginning);
        doRandom = (type & RAND) != 0;
        reset(0,0);
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
        g2.clip(shape);
        g2.drawImage(bimg, 0, 0, null);
    }

    @Override
    public void step(int w, int h) {
        if (bimg == null) {
            int biw = surf.getWidth();
            int bih = surf.getHeight();
            bimg = new BufferedImage(biw, bih,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bimg.createGraphics();
//            big.drawImage(surf.bimg, 0, 0, null);
            big.dispose();
        }
        double z = Math.min(w, h) * zoom;
        if ((type & OVAL) != 0) {
            shape = new Ellipse2D.Double(w / 2 - z / 2, h / 2 - z / 2, z,
                    z);
        } else if ((type & ARC) != 0) {
            shape = new Arc2D.Double(-100, -100, w + 200, h + 200, 90,
                    extent, Arc2D.PIE);
            extent -= eIncr;
        } else if ((type & RECT) != 0) {
            if ((type & WID) != 0) {
                shape = new Rectangle2D.Double(w / 2 - z / 2, 0, z, h);
            } else if ((type & HEI) != 0) {
                shape = new Rectangle2D.Double(0, h / 2 - z / 2, w, z);
            } else {
                shape = new Rectangle2D.Double(w / 2 - z / 2, h / 2 - z
                        / 2, z, z);
            }
        }
        zoom += zIncr;
    }

    @Override
    public void reset(int w, int h) {
        if (doRandom) {
            int num = (int) (Math.random() * 5.0);
            switch (num) {
                case 1:
                    type = RECT;
                    break;
                case 2:
                    type = RECT | WID;
                    break;
                case 3:
                    type = RECT | HEI;
                    break;
                case 4:
                    type = ARC;
                    break;
                case 0:
                default:
                    type = OVAL;
            }
        }
        shape = null;
        bimg = null;
        extent = 360.0;
        zoom = 2.0;
    }
}
