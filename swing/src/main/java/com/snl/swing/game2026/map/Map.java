package com.snl.swing.game2026.map;

import com.snl.swing.game2026.util.Dispose;

public class Map implements Dispose {
    //地图拥有多个图层，所以是一对多关系
    private MapLayers layers;
    private MapProperties properties;

    public Map() {
        //无参构造器
        init();
    }

    private void init() {
        layers = new MapLayers();
        properties = new MapProperties();
    }

    public MapLayers getLayers() {
        return layers;
    }

    public MapProperties getProperties() {
        return properties;
    }

    @Override
    public void dispose() {

    }


    public void setLayers(MapLayers layers) {
        this.layers = layers;
    }
}
