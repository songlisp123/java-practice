package com.snl.swing.tank;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public abstract class RotateComponent extends Component implements RotateTransForm {

    private double rot,theta,lastRot;

    public RotateComponent() {
        super();
    }

    public RotateComponent(double theta) {
        super();
        setAnglerSpeed(theta);
    }

    @Override
    public double getRot() {
        return rot;
    }

    @Override
    public double getLastRot() {
        return lastRot;
    }

    @Override
    public double getAnglerSpeed() {
        return theta;
    }

    @Override
    public void setAnglerSpeed(double angleSpeed) {
        this.theta = angleSpeed;
    }

    @Override
    public void setRot(double rot) {
        lastRot = this.rot;
        this.rot = rot;
    }

    //局部坐标系逆时针旋转
    public void rotateLocalClockWise(double delta) {
        lastRot = rot;
        rot += theta * delta;
    }

    //局部坐标系顺时针旋转
    public void rotateLocalOnClock(double delta) {
        lastRot = rot;
        rot -= theta * delta;
    }

    public void update(double delta,Component parentComponent) {
        if (outlines == null || outlines.length == 0)
            return;
        if (copy == null)
            copy = new Vector2D[outlines.length];

        update(parentComponent);
        if(getLastRot() != getRot())
            rotateForm = Matrix3x3f.rotate(getRot());
        Matrix3x3f WORLDtRANSFORM = modelToWorld();
        //绘制大圆
        for (int i = 0; i < outlines.length; i++) {
            copy[i] = WORLDtRANSFORM.mul(outlines[i]);
        }
    }

    protected abstract void update(Component parentComponent);

    public void draw(Graphics2D g2, DiKaErPlus d) {
//        Paint origin = g2.getPaint();
        g2.setPaint(getPaint() == null ? Color.CYAN : getPaint());
        d.drawPoly(g2,copy,true);
        d.drawCircle(g2,modelToWorld().mul(Vector2D.originPoint),0.1,true);
//        g2.setPaint(origin);
    }

}
