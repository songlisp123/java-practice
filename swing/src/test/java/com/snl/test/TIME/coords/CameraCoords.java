package com.snl.test.TIME.coords;

import java.awt.geom.Point2D;

public class CameraCoords {

    double scale;
    Point2D origin; // 世界原点在屏幕的位置

    public CameraCoords(double scale, Point2D origin) {
        this.scale = scale;
        this.origin = origin;
    }

    public Point2D worldToScreen(Point2D p) {
        return new Point2D.Double(
                origin.getX() + p.getX() * scale,
                origin.getY() - p.getY() * scale
        );
    }

    public Point2D ScreenToWorld(Point2D p){
        return new Point2D.Double(
                (p.getX() - origin.getY()) / scale,
                (origin.getY() - p.getY()) / scale
        );
    }

    public static CameraCoords createCamera(double scale,Point2D origin) {
        return new CameraCoords(scale,origin);
    }
}
