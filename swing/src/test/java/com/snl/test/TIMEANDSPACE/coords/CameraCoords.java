package com.snl.test.TIMEANDSPACE.coords;

import java.awt.geom.Point2D;

public class CameraCoords {

    double scale; //每单位多少像素
    Point2D origin; // 世界原点在屏幕的位置

    public CameraCoords(double scale, Point2D origin) {
        this.scale = scale;
        this.origin = origin;
    }

    /**
     * 将世界坐标转换成视图坐标
     * @param p 世界坐标
     * @return 基于视图的坐标
     */
    public Point2D worldToScreen(Point2D p) {
        return new Point2D.Double(
                origin.getX() + p.getX() * scale,
                origin.getY() - p.getY() * scale
        );
    }

    /**
     * 讲视图坐标转换成世界坐标
     * @param p 视图坐标
     * @return 新的转换后的世界坐标
     */
    public Point2D ScreenToWorld(Point2D p){
        return new Point2D.Double(
                (p.getX() - origin.getY()) / scale,
                (origin.getY() - p.getY()) / scale
        );
    }

}
