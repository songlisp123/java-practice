package com.snl.swing.geom;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Problem1_4 extends ProblemSolver {

    AABB base,body,paoTa,gun;


    Matrix3x3f lToWorld;
    Vector2D[] vs = new Vector2D[4];

    double upTheta,dowTheta,rot;
    double speed = 1.5;
    boolean shooting;
    Bullet bullet;


    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        sb.append("试验坦克构造");
        base = new AABB(
                new Vector2D(-1.5,-0.5),new Vector2D(1.5,0.5)
        );
        body = new AABB(
                new Vector2D(-1,-0.25),new Vector2D(1,0.25)
        );
        paoTa = new AABB(
                new Vector2D(-0.5,-1),new Vector2D(0.5,1)
        );
        gun = new AABB(new Vector2D(-1,-0.5),new Vector2D(1,.5));

        vs[0] = new Vector2D(-1,0.125);
        vs[1] = new Vector2D(1,0.125);
        vs[2] = new Vector2D(1,-0.125);
        vs[3] = new Vector2D(-1,-0.125);


        lToWorld = Matrix3x3f.translate(1,1);
        upTheta = Math.PI / 6;
        dowTheta = -Math.PI / 6;
        rot = 0;
        super.openTextPanel();
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_UP)) {
            rot += delta * upTheta;
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN))
            rot += delta * dowTheta;

        if (keyBoardEvent.keyDown(KeyEvent.VK_A))
        {
            double dx = speed * delta;
            double element = lToWorld.getElement(2, 0);
            lToWorld.setValue(element - dx,2,0);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_D)) {
            double dx = speed * delta;
            double element = lToWorld.getElement(2, 0);
            lToWorld.setValue(element + dx,2,0);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_W)) {
            double dy = speed * delta;
            double element = lToWorld.getElement(2, 1);
            lToWorld.setValue(element +  dy,2,1);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_S)) {
            double dy = speed * delta;
            double element = lToWorld.getElement(2, 1);
            lToWorld.setValue(element - dy,2,1);
        }

        shooting = keyBoardEvent.keyDownOnce(KeyEvent.VK_H);

        rot = Math.min(upTheta,rot);
        rot = Math.max(rot,dowTheta);
    }


    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (bullet != null) {
            //TODO
            bullet.update(delta);
        }
    }

    @Override
    void drawContent(Graphics2D g2) {
        Vector2D min = base.getMin();
        Vector2D max = base.getMax();
        max = lToWorld.mul(max);
        min = lToWorld.mul(min);
        Matrix3x3f bodyToWorld = Matrix3x3f.translate(0,00.75).mul(lToWorld);
        Vector2D bodyMin = body.getMin();
        Vector2D bodyMax = body.getMax();
        bodyMin = bodyToWorld.mul(bodyMin);
        bodyMax = bodyToWorld.mul(bodyMax);
        Matrix3x3f paoToWorld = Matrix3x3f.translate(0,1.25).mul(bodyToWorld);
        Vector2D paoTaMin = paoTa.getMin();
        Vector2D paoTaMax = paoTa.getMax();
        paoTaMin = paoToWorld.mul(paoTaMin);
        paoTaMax = paoToWorld.mul(paoTaMax);
        Matrix3x3f gunToWorld = Matrix3x3f.translate(1,0).mul(paoToWorld);
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);

        gunToWorld = gunToWorld.mul(
                Matrix3x3f.translate(-1,0).mul(rotate.mul(Matrix3x3f.translate(1,0)))
        );

        Vector2D vsCopy[] = new Vector2D[vs.length];
        for (int  i = 0;i<vs.length;i++) {
            Vector2D v = vs[i];
            v = gunToWorld.mul(v);
            vsCopy[i] = v;
        }
        drawAAbb(g2,new AABB(min,max),false);
        drawAAbb(g2,new AABB(bodyMin,bodyMax),false);
        drawAAbb(g2,new AABB(paoTaMin,paoTaMax),false);
        drawPoly(g2,vsCopy,true);

        if (shooting) {
            createBullet(gunToWorld);
        }
        if (bullet != null) {
            bullet.draw(g2);
        }
    }

    private void createBullet(Matrix3x3f parentTransform) {
        bullet = new Bullet();
        //炮口世界坐标
        bullet.position =
                parentTransform.mul(new Vector2D(1,0));
        //炮管方向
        Vector2D end =
                parentTransform.mul(new Vector2D(1,0));

        Vector2D start =
                parentTransform.mul(new Vector2D(0,0));
        bullet.direction =
                end.sub(start).norm();
    }

    class Bullet {
        //被ai嘲讽使用父亲坐标
//        Matrix3x3f trans;
//        double d;

        //ai推荐优化,分离世界物体与父节点
        Vector2D position;
        Vector2D direction;

        public Bullet() {
        }

        void draw(Graphics2D g2) {
//            Vector2D d1 = trans.mul(new Vector2D(d, 0));
            drawCircle(g2,position,0.1,true);
        }

        public void update(double delta) {
            position =
                    position.add(
                            direction.mul(speed*delta)
                    );
        }
    }

    public static void main(String[] args) {
        launchGame(new Problem1_4());
    }
}
