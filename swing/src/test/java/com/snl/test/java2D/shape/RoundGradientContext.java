package com.snl.test.java2D.shape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class RoundGradientContext implements PaintContext {

    private Point2D mPoint;
    private  Point2D mRadius;
    private Color mc1,mc2;

    public RoundGradientContext(Point2D mPoint, Point2D mRadius, Color mc1, Color mc2) {
        this.mPoint = mPoint;
        this.mRadius = mRadius;
        this.mc1 = mc1;
        this.mc2 = mc2;
    }

    @Override
    public void dispose() {}

    @Override
    public ColorModel getColorModel() {
        return ColorModel.getRGBdefault();
    }

    @Override
    public Raster getRaster(int x, int y, int w, int h) {
        WritableRaster raster = this.getColorModel().createCompatibleWritableRaster(w,h);
        int[] data = new int[w * h * 4];
        for (int j = 0;j<h;j++) {
            for (int i =0;i < w ;i++) {
                double distance = mPoint.distance(x + i, y + j);
                double radius = mRadius.distance(0,0);
                double radio = distance / radius;
                if (radio >= 1 ) {
                    radio = 1;
                }
                int base = (j * w + i) * 4;
                data[base] = (int) (mc1.getRed() + radio * (mc2.getRed() - mc1.getRed()));
                data[base + 1]= (int) (mc1.getGreen() + radio * (mc2.getGreen() - mc1.getGreen()));
                data[base + 2]= (int) (mc1.getBlue() + radio * (mc2.getBlue() - mc1.getBlue()));
                data[base + 3]= (int) (mc1.getAlpha() + radio * (mc2.getAlpha() - mc1.getAlpha()));
            }
        }
        raster.setPixels(0,0,w,h,data);
        return raster;
    }
}
