package com.snl.swing.game2026.map.tiled.tiles;

import com.snl.swing.game2026.map.MapObjects;
import com.snl.swing.game2026.map.MapProperties;
import com.snl.swing.game2026.map.tiled.TiledMapTile;

public class StaticTiledMapTile implements TiledMapTile {

    //id编号
    private int id;
    private BlendMode blendMode;
    private MapProperties properties;
    private MapObjects objects;

    private float offsetX,offsetY;

    public StaticTiledMapTile() {
        blendMode = BlendMode.ALPHA;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    @Override
    public float getOffsetX() {
        return offsetX;
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public MapProperties getProperties() {
        if (properties == null)
        {
            properties = new MapProperties();
        }
        return properties;
    }

    @Override
    public MapObjects getObjects() {
        if (objects == null)
        {
            objects = new MapObjects();
        }
        return objects;
    }

}
