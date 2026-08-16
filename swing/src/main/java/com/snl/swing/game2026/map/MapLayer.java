package com.snl.swing.game2026.map;

public class MapLayer {
    //图层属性
    private MapProperties properties;
    //图层包含的精灵
    private MapObjects objects;
    //父亲引用
    private MapLayer parent;
    //是否视觉可见
    private boolean visible;

    public MapLayer() {
        objects = new MapObjects();
        properties = new MapProperties();
        visible = true;
        parent = null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MapObjects getObjects() {
        return objects;
    }

    public void setObjects(MapObjects objects) {
        this.objects = objects;
    }
}
