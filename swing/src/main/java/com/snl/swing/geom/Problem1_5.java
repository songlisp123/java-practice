package com.snl.swing.geom;

import com.snl.swing.game.math.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Problem1_5 extends ProblemSolver {

    Circle body;
    Vector2D[] gun,base,wheel;

    Matrix3x3f baseToWorld;
    Matrix3x3f rotateTransForm,translateTransForm;
    double speed,bulletSpeed;
    //底盘移动
    double baseRot,baseTheta;
    double bodyRot,bodyTheta;

    Bullet bullet;
    boolean shooting,truning;


    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        sb.append("测试坦克俯视图");

        //本地坐标
        base = new Vector2D[] {
                new Vector2D(-1.25,-0.5),new Vector2D(1.25,-0.5),new Vector2D(1.25,0.5),new Vector2D(-1.25,0.5)
        };

        body = new Circle();
        body.r = 0.75;
        body.center = Vector2D.originPoint;

        baseToWorld = Matrix3x3f.identity();


        gun = new Vector2D[] {
                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
        };

        baseTheta = bodyTheta= Math.PI / 3;
        baseRot = bodyRot =  0;
        speed = 1.25;


        //车轮
        wheel = new Vector2D[] {
                new Vector2D(-0.25,-0.05),new Vector2D(0.25,-0.05),new Vector2D(0.25,0.05),new Vector2D(-0.25,0.05)
        };

        translateTransForm = Matrix3x3f.identity();
        rotateTransForm = Matrix3x3f.rotate(baseRot);

        bulletSpeed = 2.5;
        super.openTextPanel();

    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

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
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_D))
        {
            //坦克前进
            double d = delta * speed;
            Vector2D moved = rotateTransForm.mul(new Vector2D(d, 0));
            Vector2D c2 = translateTransForm.getColumn(2);
            c2 = c2.add(moved);
            translateTransForm.setColumn(2,c2);

        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_A))
        {
            //坦克前进
            double d = delta * speed;
            Vector2D moved = rotateTransForm.mul(new Vector2D(-d, 0));
            Vector2D c2 = translateTransForm.getColumn(2);
            c2 = c2.add(moved);
            translateTransForm.setColumn(2,c2);

        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_W))
        {
            //向上旋转
            baseRot += delta * baseTheta;
            rotateTransForm = Matrix3x3f.rotate(baseRot);
            if (!truning)
                truning = true;
        }


        if (keyBoardEvent.keyDown(KeyEvent.VK_S))
        {
            baseRot -= delta * baseTheta;
            rotateTransForm = Matrix3x3f.rotate(baseRot);
            if (!truning)
                truning = true;
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_UP))
        {
            //向上旋转
            bodyRot += delta * bodyTheta;
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN))
        {
            bodyRot -= delta * bodyTheta;
        }

        shooting = keyBoardEvent.keyDownOnce(KeyEvent.VK_H);
        viewMat = Matrix3x3f.translate(translateTransForm.getColumn(2).inv());
        axis.createAxis(getViewportTransform(),c,wordWidth);

        truning = truning && (keyBoardEvent.keyDown(KeyEvent.VK_W)
                || keyBoardEvent.keyDown(KeyEvent.VK_S));

    }

    @Override
    void drawContent(Graphics2D g2) {


        baseToWorld = translateTransForm.mul(rotateTransForm);

        Vector2D[] vs = new Vector2D[gun.length];
        for (int i = 0;i<vs.length;i++) {
            Vector2D v = base[i];
            vs[i] = baseToWorld.mul(v);
        }

        g2.setColor(Color.gray);
        drawPoly(g2,vs,false);

        Vector2D localCenter = baseToWorld.mul(new Vector2D());


        //车轮
        Matrix3x3f wheelToWorld = baseToWorld.mul(Matrix3x3f.translate(0.75,0.3));;
        if (truning) {
            wheelToWorld = wheelToWorld.mul(
                    Matrix3x3f.rotate(Math.PI / 3)
            );
        }


        for (int i = 0;i<vs.length;i++) {
            Vector2D v = wheel[i];
            vs[i] = wheelToWorld.mul(v);
        }

        g2.setColor(Color.blue);
        drawPoly(g2,vs,true);

        //另三个//关于e1\e2轴的旋转
        Vector2D e2 = baseToWorld.getColumn(1);
        Vector2D e1 = baseToWorld.getColumn(0);
        for (int i = 0;i<vs.length;i++) {
            Vector2D v = vs[i];
            Vector2D vClone = v.clone();
            vClone = vClone.sub(localCenter);
            double dot = 2 * vClone.dot(e2);
            vs[i] = v.sub(e2.scale(dot));
        }

        drawPoly(g2,vs,true);
        for (int i = 0;i<vs.length;i++) {
            Vector2D v = wheelToWorld.mul(wheel[i]);
            Vector2D vClone = v.clone();
            vClone = vClone.sub(localCenter);
            double dot = 2 * vClone.dot(e1);
            vs[i] = v.sub(e1.scale(dot));
        }

        drawPoly(g2,vs,true);


        for (int i = 0;i<vs.length;i++) {
            Vector2D v = wheelToWorld.mul(wheel[i]);
            vs[i] = localCenter.scale(2).sub(v);
        }

        drawPoly(g2,vs,true);



        Matrix3x3f bodyToWorld =Matrix3x3f.rotate(bodyRot);
        bodyToWorld = baseToWorld.mul(bodyToWorld);
        Vector2D vector2D = bodyToWorld.mul(new Vector2D());
        g2.setColor(Color.PINK);
        drawCircle(g2,vector2D,body.r,true);

        Matrix3x3f bolToWorld = bodyToWorld.mul(Matrix3x3f.translate(body.r,0));
        Vector2D boil = bolToWorld.mul(new Vector2D());
        drawCircle(g2,boil,0.1,true);


         bolToWorld = bodyToWorld.mul(Matrix3x3f.translate(-body.r,0));
         boil = bolToWorld.mul(new Vector2D());
         g2.setColor(Color.MAGENTA);
        drawCircle(g2,boil,0.1,true);



        Matrix3x3f gunToWorld = bodyToWorld.mul(Matrix3x3f.translate(0,1));
        for (int i = 0;i<vs.length;i++) {
            Vector2D v = gun[i];
            vs[i] = gunToWorld.mul(v);
        }


        drawPoly(g2,vs,false);

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
                parentTransform.mul(new Vector2D(0,1));
        //炮管方向
        Vector2D end =
                parentTransform.mul(new Vector2D(0,1));

        Vector2D start =
                parentTransform.mul(new Vector2D(0,0));
        bullet.direction =
                end.sub(start).norm();
    }

    class Bullet {

        //ai推荐优化,分离世界物体与父节点
        Vector2D position;
        Vector2D direction;

        public Bullet() {
        }

        void draw(Graphics2D g2) {
            drawCircle(g2,position,0.1,true);
        }

        public void update(double delta) {
            position =
                    position.add(
                            direction.mul(bulletSpeed*delta)
                    );
        }
    }

    public static void main(String[] args) {
        launchGame(new Problem1_5());
    }
}
