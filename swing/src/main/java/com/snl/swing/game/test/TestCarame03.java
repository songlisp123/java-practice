package com.snl.swing.game.test;

import com.snl.swing.game.OrthographicCamera;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.tank.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;

public class TestCarame03 extends DiKaErPlus {

    SimpleTank tank;
    OrthographicCamera camera;

    DrawAABB[] aabb;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        createTank();
        camera = new OrthographicCamera(3);
        aabb = new DrawAABB[10];

        for (int j = 0; j < aabb.length; j ++) {
            DrawAABB temp = new DrawAABB();
            temp.aabb = new AABB(
                    new Vector2D(-0.5 + (j),-0.5),new Vector2D(0.5 + (j),0.5)
            );

            temp.color = (j % 2) == 0 ? Color.cyan : Color.magenta;
            aabb[j] = temp;
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        boolean kewDown_A = keyBoardEvent.keyDown(KeyEvent.VK_A);
        boolean kewDown_W = keyBoardEvent.keyDown(KeyEvent.VK_W);
        boolean kewDown_D = keyBoardEvent.keyDown(KeyEvent.VK_D);
        boolean kewDown_S = keyBoardEvent.keyDown(KeyEvent.VK_S);
        boolean kewDown_U = keyBoardEvent.keyDown(KeyEvent.VK_U);
        boolean kewDown_I = keyBoardEvent.keyDown(KeyEvent.VK_I);
        boolean keyDown_Space = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
        boolean keyDown_UP = keyBoardEvent.keyDown(KeyEvent.VK_UP);
        boolean keyDown_DOWN = keyBoardEvent.keyDown(KeyEvent.VK_DOWN);
        boolean keyDown_Tab = keyBoardEvent.keyDown(KeyEvent.VK_C);

        if (kewDown_W)
            tank.rotateClockWise(delta);
        if (kewDown_S)
            tank.rotateClock(delta);
        if (kewDown_A) {
            tank.backward(delta, 1.5);
            tank.getWheel().rotateLocalClockWise(delta);
        }
        if (kewDown_D) {
            tank.forward(delta, 3);
            tank.getWheel().rotateLocalOnClock(delta);
        }

        if (keyDown_UP)
            tank.rotatePaoTaiClockWise(delta);
        if (keyDown_DOWN)
            tank.rotatePaoTaiClock(delta);

        if (keyDown_Space)
            if (!tank.isFiring())
                tank.setFiring(true);

        tank.setFiring(tank.isFiring() && keyDown_Space);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        tank.update(delta);
        camera.update();
        if (tank.getBullet() != null) {
            //todo
            camera.getPosition().x = tank.getBullet().getPosition().x;
            camera.getPosition().y = tank.getBullet().getPosition().y;
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        tank.draw(g2,this);
        //绘制文本
        drawText(g2,375,225,150,new TextLayout("摄像机视图",g2.getFont(),g2.getFontRenderContext()));
        AABB viewBoundingBox = camera.getViewBoundingBox();
        //摄像机
        //摄像机框架
        g2.drawRect(375,75,150,150);
        drawAAbb(g2,viewBoundingBox,false);
        for (DrawAABB a : aabb) {
            g2.setColor(a.color);
            drawAAbb(g2,a.aabb,true);
            if (viewBoundingBox.collisionAABB(a.aabb)) {
                AABB aabb1 = viewBoundingBox.intersection(a.aabb);
                Vector2D v3 = camera.projectionToScreen(aabb1.getMin().toVector3DinZisZero(), 150, 150, 375, 75);
                Vector2D v4 = camera.projectionToScreen(aabb1.getMax().toVector3DinZisZero(), 150, 150, 375, 75);
                drawAAbb(g2, aabb1, true);
                g2.fillRect((int) v3.x, (int) v4.y, (int) (v4.x - v3.x), (int) (v3.y -v4.y));

            }
        }
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestCarame03());
    }

    public void createTank() {
        tank = new SimpleTank();
        Base base = new Base();
        base.setOutlines(
                new Vector2D[]{
                        new Vector2D(-1.5,-1.25),new Vector2D(1.5,-1.25),new Vector2D(1.5,1.25),new Vector2D(-1.5,1.25)
                }
        );

        base.setAnglerSpeed(Math.PI);
        base.setPaint(Color.green);

        tank.setBase(base);

        Wheel wheel = new Wheel(10,0.5);
        wheel.setAnglerSpeed(Math.PI);
        wheel.setShowPattern(true);

        PaoTai paoTai = new PaoTai(12,1);
        paoTai.setAnglerSpeed(Math.PI);


        Gun gun = new Gun();
        gun.setOutlines(new Vector2D[] {
                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
        });

        tank.setWheel(wheel);
        tank.setPaoTai(paoTai);
        tank.setGun(gun);
    }


    class  DrawAABB {
        AABB aabb;
        Color color;

        public DrawAABB() {
            aabb = new AABB(new Vector2D(-0.5,-0.5),new Vector2D(0.5,0.5));
            color = Color.magenta;
        }
    }
}
