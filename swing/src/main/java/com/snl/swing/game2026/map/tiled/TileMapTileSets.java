package com.snl.swing.game2026.map.tiled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileMapTileSets implements Iterable<TiledMapTileSet> {
    private List<TiledMapTileSet> tileSets;

    public TileMapTileSets() {
        tileSets = new ArrayList<>();
    }

    @Override
    public Iterator<TiledMapTileSet> iterator() {
        return tileSets.iterator();
    }
}
