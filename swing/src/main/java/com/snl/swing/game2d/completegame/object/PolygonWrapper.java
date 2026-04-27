package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;

import java.util.List;

public class PolygonWrapper {
    private float worldWidth;
    private float worldHeight;
    private Vector2D worldMin;
    private Vector2D worldMax;

    public PolygonWrapper(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        worldMax = new Vector2D(worldWidth / 2.0f, worldHeight / 2.0f);
        worldMin = worldMax.inv();
    }

    public boolean hasLeftWorld(Vector2D position) {
        return position.x < worldMin.x || position.x > worldMax.x
                || position.y < worldMin.y || position.y > worldMax.y;
    }

    public Vector2D wrapPosition(Vector2D position) {
        Vector2D wrapped = new Vector2D(position);
        if (position.x < worldMin.x) {
            wrapped.x = position.x + worldWidth;
        } else if (position.x > worldMax.x) {
            wrapped.x = position.x - worldWidth;
        }
        if (position.y < worldMin.y) {
            wrapped.y = position.y + worldHeight;
        } else if (position.y > worldMax.y) {
            wrapped.y = position.y - worldHeight;
        }
        return wrapped;
    }

    public void wrapPolygon(Vector2D[] poly, List<Vector2D[]> renderList) {
        Vector2D min = getMin(poly);
        Vector2D max = getMax(poly);
        boolean north = max.y > worldMax.y;
        boolean south = min.y < worldMin.y;
        boolean west = min.x < worldMin.x;
        boolean east = max.x > worldMax.x;
        if (west)
            renderList.add(wrapEast(poly));
        if (east)
            renderList.add(wrapWest(poly));
        if (north)
            renderList.add(wrapSouth(poly));
        if (south)
            renderList.add(wrapNorth(poly));
        if (north && west)
            renderList.add(wrapSouthEast(poly));
        if (north && east)
            renderList.add(wrapSouthWest(poly));
        if (south && west)
            renderList.add(wrapNorthEast(poly));
        if (south && east)
            renderList.add(wrapNorthWest(poly));
    }

    public void wrapPositions(Vector2D[] poly, Vector2D position, List<Vector2D> centerList) {
        Vector2D min = getMin(poly);
        Vector2D max = getMax(poly);
        boolean north = max.y > worldMax.y;
        boolean south = min.y < worldMin.y;
        boolean west = min.x < worldMin.x;
        boolean east = max.x > worldMax.x;
        if (west)
            centerList.add(position.add(new Vector2D(worldWidth, 0.0f)));
        if (east)
            centerList.add(position.add(new Vector2D(-worldWidth, 0.0f)));
        if (north)
            centerList.add(position.add(new Vector2D(0.0f, -worldHeight)));
        if (south)
            centerList.add(position.add(new Vector2D(0.0f, worldHeight)));
        if (north && west)
            centerList.add(position.add(new Vector2D(worldWidth, -worldHeight)));
        if (north && east)
            centerList.add(position.add(new Vector2D(-worldWidth, -worldHeight)));
        if (south && west)
            centerList.add(position.add(new Vector2D(worldWidth, worldHeight)));
        if (south && east)
            centerList.add(position.add(new Vector2D(-worldWidth, worldHeight)));
    }

    private Vector2D getMin(Vector2D[] poly) {
        Vector2D min = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
        for (Vector2D v : poly) {
            min.x = Math.min(v.x, min.x);
            min.y = Math.min(v.y, min.y);
        }
        return min;
    }

    private Vector2D getMax(Vector2D[] poly) {
        Vector2D max = new Vector2D(-Float.MAX_VALUE, -Float.MAX_VALUE);
        for (Vector2D v : poly) {
            max.x = Math.max(v.x, max.x);
            max.y = Math.max(v.y, max.y);
        }
        return max;
    }

    private Vector2D[] wrapNorth(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(0.0f, worldHeight));
    }

    private Vector2D[] wrapSouth(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(0.0f, -worldHeight));
    }

    private Vector2D[] wrapEast(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(worldWidth, 0.0f));
    }

    private Vector2D[] wrapWest(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(-worldWidth, 0.0f));
    }

    private Vector2D[] wrapNorthWest(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(-worldWidth, worldHeight));
    }

    private Vector2D[] wrapNorthEast(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(worldWidth, worldHeight));
    }

    private Vector2D[] wrapSouthEast(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(worldWidth, -worldHeight));
    }

    private Vector2D[] wrapSouthWest(Vector2D[] poly) {
        return transform(poly, Matrix3x3f.translate(-worldWidth, -worldHeight));
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] copy = new Vector2D[poly.length];
        for (int i = 0; i < poly.length; ++i) {
            copy[i] = mat.mul(poly[i]);
        }
        return copy;
    }
}
