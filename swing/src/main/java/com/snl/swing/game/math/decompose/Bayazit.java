package com.snl.swing.game.math.decompose;

import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.Convex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bayazit extends AbstractDecomposer implements Decomposer {



    @Override
    public List<Convex> decompose(Vector2D... vectors) {
        if (vectors == null)
            throw new IllegalArgumentException("非法参数异常");
        else {
            int size = vectors.length;
            if (size < 4)
                throw new ArrayIndexOutOfBoundsException("数组长度必须大于等于四");
            List<Vector2D> polygon = new ArrayList<>();
            Collections.addAll(polygon,vectors);
            List<Convex> polygons = new ArrayList<>();
            decomposePolygon(polygon,polygons);
            return polygons;
        }
    }

    private void decomposePolygon(List<Vector2D> polygon, List<Convex> polygons) {
        //TODO
        //时间 暂时还不会
    }
}
