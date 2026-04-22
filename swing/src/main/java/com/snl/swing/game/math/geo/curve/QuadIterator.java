package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.geo.PathIterator;

public class QuadIterator implements PathIterator {

    protected final QuadCurve quadCurve;
    private int index;
    private final Matrix3x3f mat;

    public QuadIterator(QuadCurve quadCurve,Matrix3x3f transform) {
        this.quadCurve = quadCurve;
        this.mat = transform;
    }

    @Override
    public boolean done() {
        return index > 1;
    }

    @Override
    public void next() {
        ++index;
    }

    @Override
    public int currentSegment(double[] coords) {
        if (done())
            throw new IllegalArgumentException("非法参数异常");
        int type;
        if (index == 0)
        {
            coords[0] = this.quadCurve.getStartPointX();
            coords[1] = this.quadCurve.getStartPointY();
            type = PathIterator.SEG_MOVE_TO;
        }else {
            coords[0] = this.quadCurve.getControlPoint01X();
            coords[1] = this.quadCurve.getControlPoint01Y();
            coords[2] = this.quadCurve.getEndPointX();
            coords[3] = this.quadCurve.getEndPointY();
            type = PathIterator.SEG_QUADTO;
        }
        return type;
    }
}
