package com.snl.swing.tank;

import com.snl.swing.game.math.Matrix3x3f;

public class Gun extends RotateComponent {

    public Gun() {
        super();
    }

    @Override
    protected void update(Component parentComponent) {
        //TODO实现有问题？？
        parentForm = parentComponent.modelToWorld().mul(Matrix3x3f.translate(0,outlines[2].y));
    }

}
