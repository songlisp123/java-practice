package com.snl.swing.game2026.map;

import java.awt.*;

//图层对象
public class MapObject {

    private static int id;
    private String name;
    private final static String DEFAULT_NAME = "对象";
    private float opacity;
    private boolean visible;

    private Color color;

    //对象属性
    private MapProperties properties;


    private static int advanceId() {
        return id++;
    }

    public MapObject() {
        init();
        advanceId();
    }

    private void init() {
        this.name = DEFAULT_NAME;
        opacity = 1.0f;
        visible = true;
        color = Color.cyan;
        properties = new MapProperties();
    }

    public String getName() {
        return name;
    }

    public float getOpacity() {
        return opacity;
    }

    public boolean isVisible() {
        return visible;
    }

    public Color getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return ((name == null) ? DEFAULT_NAME + "_" + id : name) ;
    }
}
