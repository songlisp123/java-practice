package com.snl.swing.game2026.map.tiled;

import com.snl.swing.game2026.map.MapLayer;
import com.snl.swing.game2026.map.MapObject;
import com.snl.swing.game2026.map.MapRender;

public interface TiledMapRenderer extends MapRender {
    void renderObjects(MapLayer layer);

    void renderObject(MapObject object);

    void renderTileLayer(TiledMapTileLayer layer);

    void renderImageLayer(TiledMapImageLayer layer);
}