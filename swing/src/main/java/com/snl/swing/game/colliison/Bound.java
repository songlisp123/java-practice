package com.snl.swing.game.colliison;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Shiftable;
import com.snl.swing.game.math.contract.Translatable;

public interface Bound extends Translatable, Shiftable {

    Vector2D getTranslation();

    boolean isOutSide(AABB aabb);
}
