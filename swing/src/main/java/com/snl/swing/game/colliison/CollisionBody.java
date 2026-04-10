package com.snl.swing.game.colliison;

import com.snl.swing.game.DataContainer;
import com.snl.swing.game.Ownable;
import com.snl.swing.game.math.contract.Shiftable;
import com.snl.swing.game.math.contract.Transformable;

public interface CollisionBody<T extends Fixture> extends Transformable, Shiftable,
        DataContainer, Ownable {


}
