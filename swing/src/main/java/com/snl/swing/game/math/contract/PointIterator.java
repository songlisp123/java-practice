package com.snl.swing.game.math.contract;

import com.snl.swing.game.math.Vector2D;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 点 迭代器,保留的是副本
 * @since 2026年4月2日20:39:19
 */
public class PointIterator implements Iterator<Vector2D> {

    private final Vector2D[] vector2DS;
    private int index;

    public PointIterator(Vector2D[] vector2DS) {
        this.vector2DS = new Vector2D[vector2DS.length];
        System.arraycopy(vector2DS,0,this.vector2DS,0,vector2DS.length);
        this.index = -1;
    }

    @Override
    public boolean hasNext() {
        return index < vector2DS.length - 1;
    }

    @Override
    public Vector2D next() {
        if (!hasNext())
            throw new NoSuchElementException("暂无更多元素");
        return this.vector2DS[++index];
    }


}
