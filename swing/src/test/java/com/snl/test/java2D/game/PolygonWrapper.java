package com.snl.test.java2D.game;

import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.util.List;

public class PolygonWrapper {

    int worldWidth;
    int worldHeight;
    Vector2D min,max;

    public PolygonWrapper(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        min = new Vector2D(-worldWidth / 2.0,-worldHeight / 2.0);
        max = min.inv();
    }

    public boolean hasInWorld(Vector2D pos) {
        return pos.getX() < min.getX() ||
                pos.getX() > max.getX() ||
                pos.getY() < min.getY() ||
                pos.getY() < max.getY();
    }

    public Vector2D wrapPos(Vector2D pos)
    {
        Vector2D result = new Vector2D(pos);
        if (pos.getX() < min.getX())
        {
            result.setX(pos.getX()+worldWidth);
        }else if (pos.getX() > max.getX())
        {
            result.setX(pos.getX() - worldHeight);
        }

        if (pos.getY() < min.getY())
        {
            result.setY(pos.getY()+worldHeight);
        } else if (pos.getY() > max.getY()) {
            result.setY(pos.getY() - worldHeight);
        }
        return result;
    }


    public void wrapPolygon(Vector2D[] poly, List<Vector2D[]> polys) {
        Vector2D min = getMin(poly);
        Vector2D max = getMax(poly);
        boolean north = max.getY() > this.max.getY(); //超过上部分
        boolean south = min.getY() < this.min.getY(); //超过下部分
        boolean east = max.getX() > this.max.getX();//超过右侧
        boolean west = min.getX() < this.min.getX();//超过左侧
        if (north)
            polys.add(wrapNorth(poly));
        if (south)
            polys.add(wrapSouth(poly));
        if (east)
            polys.add(worpEast(poly));
        if (west)
            polys.add(wrapWest(poly));
        if (north && west)
            polys.add(wrapNorthWest(poly));
        if (north && east)
            polys.add(wrapNorthEast(poly));
        if (south && west)
            polys.add(wrapSouthWest(poly));
        if (south && east)
            polys.add(wrapSouthEast(poly));
    }

    private Vector2D[] wrapSouthEast(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(-worldWidth,worldHeight));
    }

    private Vector2D[] wrapSouthWest(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(worldWidth,worldHeight));
    }

    private Vector2D[] wrapNorthEast(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(-worldWidth,worldHeight));
    }

    private Vector2D[] wrapNorthWest(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(worldWidth,-worldHeight));
    }

    private Vector2D[] wrapWest(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(worldWidth,0));
    }

    private Vector2D[] worpEast(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(-worldWidth,0));
    }

    private Vector2D[] wrapSouth(Vector2D[] poly) {
        return transform(poly,Matrix3x3f.translate(0,worldHeight));
    }

    private Vector2D[] wrapNorth(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(0,-worldHeight));
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] v = new Vector2D[poly.length];
        for (int i=0;i<v.length;i++) {
            v[i] = mat.mul(poly[i]);
        }
        return v;
    }

    private Vector2D getMax(Vector2D[] poly) {
        double maxY,maxX;
        maxX = maxY = -Double.MAX_VALUE;
        for (Vector2D v : poly)
        {
            maxX = Math.max(v.getX(), maxX);
            maxY = Math.min(v.getY(),maxY);
        }
        return new Vector2D(maxX,maxY);
    }

    private Vector2D getMin(Vector2D[] poly) {
        double minx = Double.MAX_VALUE,miny = Double.MAX_VALUE;
        for (Vector2D v : poly)
        {
            minx = Math.min(v.getX(), minx);
            miny = Math.min(v.getY(),miny);
        }
        return new Vector2D(minx,miny);
    }
}
