package com.snl.swing.tank;


import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;


public class Base extends RotateComponent {

    double scaleX,scaleY,lastScaleX,lastScaleY,scaleFactor;


    public Base(Vector2D...outlines) {
        super();
        scaleFactor = 1.0;
        super.outlines = outlines;
        scaleX = scaleY = 1.0;
        lastScaleX = scaleX;
        lastScaleY = scaleY;
    }

    public void rotateClockWiseWithCenter(double delta,Vector2D center) {
        rotateLocalClockWise(delta);
        setRotateCenter(center);
    }

    public void rotateClockWithCenter(double delta,Vector2D center) {
        rotateLocalOnClock(delta);
        setRotateCenter(center);
    }

    public void scale(double delta) {
        lastScaleX = scaleX;
        lastScaleY = scaleY;
        double scaled = scaleFactor * delta;
        scaleX += scaled;
        scaleY += scaled;
    }

    public void deScale(double delta) {
        lastScaleX = scaleX;
        lastScaleY = scaleY;
        double scaled = scaleFactor * delta;
        scaleX -= scaled;
        scaleY -= scaled;
    }


    public void forward(double delta,double moveSpeed) {
        double d = delta * moveSpeed;
        Vector2D moved = rotateForm.mul(new Vector2D(d, 0));
        Vector2D c2 = transFrom.getColumn(2);
        c2 = c2.add(moved);
        transFrom.setColumn(2,c2);
    }

    public void backward(double delta,double moveSpeed) {
        double d = delta * moveSpeed;
        Vector2D moved = rotateForm.mul(new Vector2D(-d, 0));
        Vector2D c2 = transFrom.getColumn(2);
        c2 = c2.add(moved);
        transFrom.setColumn(2,c2);
    }


    @Override
    protected void update(Component parentComponent) {
        if (parentComponent == null) {
            parentForm = Matrix3x3f.getIdentity();
        }
    }
}
