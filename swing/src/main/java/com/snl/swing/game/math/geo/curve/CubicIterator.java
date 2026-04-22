package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.geo.PathIterator;

public class CubicIterator implements PathIterator {

    protected final CubicCurve curve;
    private int index;
    private final Matrix3x3f transform;

    public CubicIterator(CubicCurve curve, Matrix3x3f transform) {
        this.curve = curve;
        this.transform = transform;
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
            throw new IllegalArgumentException("非法参数");
        int type ;
        if (index == 0)
        {
            coords[0] = this.curve.getStartPointX();
            coords[1] = this.curve.getStartPointY();
            type = PathIterator.SEG_MOVE_TO;
        }else {
            coords[0] = this.curve.getControlPoint01X();
            coords[1] = this.curve.getControlPoint01Y();
            coords[2] = this.curve.getControlPoint02X();
            coords[3] = this.curve.getControlPoint02Y();
            coords[4] = this.curve.getEndPointX();
            coords[5] = this.curve.getEndPointY();
            type = PathIterator.SEG_CUBICTO;
        }
        return type;
    }
}
