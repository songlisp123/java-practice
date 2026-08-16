package com.snl.swing.game2026.map;

import java.util.ArrayList;
import java.util.Iterator;

public class MapObjects implements Iterable<MapObject> {
    //地图对象组
    private ArrayList<MapObject> objects;

    public MapObjects() {
        objects = new ArrayList<>();
    }

    public void addMapObject(MapObject object) {
        if (object == null)
            throw new IllegalArgumentException("对象不能为null");
        objects.add(object);
    }

    public void  removeMapObject(MapObject object) {
        objects.remove(object);
    }

    @Override
    public Iterator<MapObject> iterator() {
        return objects.iterator();
    }
}
