package com.snl.swing.game2026.map.tiled.objects;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game2026.map.MapObject;

public class AABBObject extends MapObject {

    private AABB aabb;

    public AABB getAabb() {
        return aabb;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }
}
