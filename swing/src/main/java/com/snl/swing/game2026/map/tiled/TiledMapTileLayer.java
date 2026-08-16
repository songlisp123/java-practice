package com.snl.swing.game2026.map.tiled;

import com.snl.swing.game2026.map.MapLayer;

public class TiledMapTileLayer extends MapLayer {

    private int weight,height;
    private int tileWeight,tileHeight;

    private Cell cell[];

    public TiledMapTileLayer(int weight, int height, int tileWeight, int tileHeight) {
        super();
        this.weight = weight;
        this.height = height;
        this.tileWeight = tileWeight;
        this.tileHeight = tileHeight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTileWeight() {
        return tileWeight;
    }

    public void setTileWeight(int tileWeight) {
        this.tileWeight = tileWeight;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public class Cell {
        //一个单元胞维护了一个tile瓦片
        private TiledMapTile tile;
    }
}
