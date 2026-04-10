package com.snl.swing.game.math.geo;

import com.snl.swing.game.math.Vector2D;

public class EdgeFeature extends Feature {

    final PointFeature v1;
    final PointFeature v2;
    final PointFeature max;
    final Vector2D edge;

    public EdgeFeature(int index, PointFeature pf1, PointFeature pf2, PointFeature max, Vector2D edge) {
        super(index);
        this.v1 = pf1;
        this.v2 = pf2;
        this.max = max;
        this.edge = edge;
    }

    public PointFeature getV1() {
        return v1;
    }

    public PointFeature getV2() {
        return v2;
    }

    public PointFeature getMax() {
        return max;
    }

    public Vector2D getEdge() {
        return edge;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EdgeFeature[Vertex1=").append(v1)
                .append("|Vertex2=").append(v2)
                .append("|Edge=").append(this.edge).append("|Max=")
                .append(this.max).append("|Index=")
                .append(this.index).append("]");
        return sb.toString();
    }
}
