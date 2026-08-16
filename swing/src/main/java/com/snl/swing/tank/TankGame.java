//package com.snl.swing.tank;
//
//import com.snl.swing.game.gameFrame.DiKaErPlus;
//import com.snl.swing.game.math.Matrix3x3f;
//import com.snl.swing.game.math.Vector2D;
//import com.snl.swing.geom.Problem1_5;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//
//public class TankGame extends DiKaErPlus  {
//
//    Tank tank;
//    Vector2D[] temp,gun;
//
//    //底盘移动
//    double baseRot,baseTheta;
//    double bodyRot,bodyTheta;
//    double speed;
//    double scaleX,scaleY;
//    private double bulletSpeed;
//    private Bullet bullet;
//
//    boolean shooting;
//
//    @Override
//    protected void gameInitial() {
//
//        super.gameInitial();
//
//        temp = new Vector2D[]{
//                //本地坐标
//                new Vector2D(-1.25,-0.5),new Vector2D(1.25,-0.5),new Vector2D(1.25,0.5),new Vector2D(-1.25,0.5)
//        };
//        gun =  new Vector2D[] {
//                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
//        };
////        tank.base.setOutlines(temp);
//        tank = TankFactory.createTank(temp,1,gun,gun);
//        tank.gun.transFrom = Matrix3x3f.translate(0,1);
//        baseRot = bodyRot = 0;
//        baseTheta = bodyTheta =  Math.PI / 4;
//        speed = 1.25;
//
//        scaleX = scaleY = 1.0;
//        bulletSpeed = 3;
//    }
//
//
//    @Override
//    protected void updateSprite(double delta) {
//        super.updateSprite(delta);
//        if (bullet != null)
//            bullet.update(delta);
//    }
//
//    @Override
//    protected void processInput(double delta) {
//        super.processInput(delta);
//        if (keyBoardEvent.keyDown(KeyEvent.VK_W))
//        {
//            //向上旋转
//            baseRot += delta * baseTheta;
//        }
//
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_S))
//        {
//            baseRot -= delta * baseTheta;
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_UP))
//        {
//            //向上旋转
//            bodyRot += delta * bodyTheta;
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN))
//        {
//            bodyRot -= delta * bodyTheta;
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_Y)) {
//            scaleY += delta;
//            scaleX = scaleY;
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_U)) {
//            scaleY -= delta;
//            scaleX = scaleY;
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_A)) {
//            //TODO
//            double d = delta * speed;
//            Matrix3x3f rotateForm = tank.base.rotateForm;
//            Vector2D moved = rotateForm.mul(new Vector2D(d, 0));
//            Vector2D c2 = tank.base.transFrom.getColumn(2);
//            c2 = c2.add(moved);
//            tank.base.transFrom.setColumn(2,c2);
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_D)) {
//            //TODO
//            double d = delta * speed;
//            Matrix3x3f rotateForm = tank.base.rotateForm;
//            Vector2D moved = rotateForm.mul(new Vector2D(-d, 0));
//            Vector2D c2 = tank.base.transFrom.getColumn(2);
//            c2 = c2.add(moved);
//            tank.base.transFrom.setColumn(2,c2);
//        }
//
//        shooting = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
//
//
//        tank.base.rotateForm = Matrix3x3f.rotate(baseRot);
//        tank.pt.rotateForm = Matrix3x3f.rotate(bodyRot);
//        tank.base.scaleForm = Matrix3x3f.scale(scaleX,scaleY);
//    }
//
//    @Override
//    protected void draw(Graphics g) {
//        super.draw(g);
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(Color.MAGENTA);
//
//        Vector2D[ ] t = new Vector2D[temp.length];
//        for (int  i = 0;i<t.length;i++) {
//            Vector2D v = temp[i];
//            t[i] = tank.base.modelToWorld().mul(v);
//        }
//        tank.pt.parentForm = tank.base.transFrom.mul(tank.base.scaleForm);
//        Matrix3x3f ptoWorld = tank.pt.modelToWorld();
//        drawPoly(g2,t, true);
//        drawCircle(g2,ptoWorld.mul(Vector2D.originPoint),tank.pt.r * scaleX,true);
//
//        tank.gun.parentForm = ptoWorld;
//
//        for (int  i = 0;i<t.length;i++) {
//            Vector2D v = gun[i];
//            t[i] = tank.gun.modelToWorld().mul(v);
//        }
//        drawPoly(g2,t, true);
//
//        if (shooting) {
//            createBullet(tank.gun.modelToWorld());
//        }
//
//        if (bullet != null)
//            bullet.draw(g2);
//
//        g2.dispose();
//    }
//
//    @Override
//    protected void animation(double delta) {
//        super.animation(delta);
//        viewMat = tank.base.transFrom.inverse();
//        axis.createAxis(getViewportTransform(),c,wordWidth);
//    }
//
//    private void createBullet(Matrix3x3f parentTransform) {
//        bullet = new Bullet();
//        //炮口世界坐标
//        bullet.position =
//                parentTransform.mul(new Vector2D(0,1));
//        //炮管方向
//        Vector2D end =
//                parentTransform.mul(new Vector2D(0,1));
//
//        Vector2D start =
//                parentTransform.mul(new Vector2D(0,0));
//        bullet.direction =
//                end.sub(start).norm();
//    }
//
//    class Bullet {
//
//        //ai推荐优化,分离世界物体与父节点
//        Vector2D position;
//        Vector2D direction;
//
//        public Bullet() {
//        }
//
//        void draw(Graphics2D g2) {
//            drawCircle(g2,position,0.1,true);
//        }
//
//        public void update(double delta) {
//            position =
//                    position.add(
//                            direction.mul(bulletSpeed*delta)
//                    );
//        }
//    }
//
//    public static void main(String[] args) {
//        launchGame(new TankGame());
//    }
//}
