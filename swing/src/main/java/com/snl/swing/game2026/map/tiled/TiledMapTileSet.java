package com.snl.swing.game2026.map.tiled;

import com.snl.swing.game2026.map.MapProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TiledMapTileSet implements Iterable<TiledMapTile> {

    private List<TiledMapTile> tiles;

    private String name;

    private MapProperties properties;

    public TiledMapTileSet() {
        tiles = new ArrayList<>();
        properties = new MapProperties();
    }

    @Override
    public Iterator<TiledMapTile> iterator() {
        return tiles.iterator();
    }
}
