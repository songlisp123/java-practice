package com.snl.swing.tank;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class SimpleTank implements TimingTarget {

    Base base;
    Wheel wheel;
    PaoTai paoTai;
    Gun gun;
    Bullet bullet;

    //攻击状态
    private boolean firing;
    private Animator firingAnimator;


    public SimpleTank() {
        base = new Base();
        wheel = new Wheel();
        paoTai = new PaoTai();
        gun = new Gun();
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }

    public void setPaoTai(PaoTai paoTai) {
        this.paoTai = paoTai;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }

    public Base getBase() {
        return base;
    }

    public Wheel getWheel() {
        return wheel;
    }

    public PaoTai getPaoTai() {
        return paoTai;
    }

    public Gun getGun() {
        return gun;
    }

    public void draw(Graphics2D g2, DiKaErPlus gameFrame) {
        base.draw(g2,gameFrame);
        wheel.draw(g2,gameFrame);
        paoTai.draw(g2,gameFrame);
        gun.draw(g2,gameFrame);
        if (bullet != null)
            gameFrame.drawCircle(g2,bullet.position,0.1,true);
    }


    public void update(double delta) {
        base.update(delta,null);
        wheel.update(delta,base);
        paoTai.update(delta,base);
        gun.update(delta,paoTai);
        if (isFiring()) {
            //平移效果
            createBullet(gun.modelToWorld());
            createrAnimator();
            showAnimator();
        }

        if (bullet != null)
            bullet.update(delta);
    }

    private void createBullet(Matrix3x3f parentTransform) {
        //炮口世界坐标
        Vector2D position =
                parentTransform.mul(new Vector2D(0,1));
        //炮管方向
        Vector2D end =
                parentTransform.mul(new Vector2D(0,1));

        Vector2D start =
                parentTransform.mul(new Vector2D(0,0));
        Vector2D direction =
                end.sub(start).norm();
        bullet = Bullet.createBullet(position,direction,3.4);
    }

    private void showAnimator() {
        if (firingAnimator.isRunning())
            firingAnimator.stop();
        firingAnimator.start();
    }

    private void createrAnimator() {
        if (firingAnimator == null) {
            firingAnimator = new Animator(1000L,this);
            firingAnimator.setRepeatCount(1);
            firingAnimator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
            firingAnimator.setEndBehavior(Animator.EndBehavior.HOLD);
        }
    }

    //局部坐标系逆时针旋转
    public void rotateClockWise(double delta) {
        base.rotateLocalClockWise(delta);
    }

    //局部坐标系顺时针旋转
    public void rotateClock(double delta) {
        base.rotateLocalOnClock(delta);
    }

    //局部坐标系逆时针旋转
    public void rotatePaoTaiClockWise(double delta) {
        paoTai.rotateLocalClockWise(delta);
    }

    //局部坐标系顺时针旋转
    public void rotatePaoTaiClock(double delta) {
        paoTai.rotateLocalOnClock(delta);
    }


    public void forward(double delta,double moveSpeed) {
        base.forward(delta,moveSpeed);
    }

    public void backward(double delta,double moveSpeed) {
        base.backward(delta,moveSpeed);
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        Vector2D e2 = gun.rotateForm.getColumn(1);

        double dy =  -0.06 + 0.06 * fraction;
        double s = 0.9 + 0.1 * fraction;
        gun.transFrom = Matrix3x3f.translate(e2.mul(0,dy));
        gun.scaleForm = Matrix3x3f.scale(s,s);
    }


    public Vector2D getCenter() {
        return base.transFrom.mul(Vector2D.originPoint);
    }
}
