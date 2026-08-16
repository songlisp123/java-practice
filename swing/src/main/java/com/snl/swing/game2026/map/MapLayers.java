package com.snl.swing.game2026.map;

import java.util.ArrayList;
import java.util.Iterator;

public class MapLayers implements Iterable<MapLayer> {

    private ArrayList<MapLayer> layers =  new ArrayList<>();

    @Override
    public Iterator<MapLayer> iterator() {
        return layers.iterator();
    }

    public ArrayList<MapLayer> getLayers() {
        return layers;
    }

    //
}
