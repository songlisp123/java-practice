package com.snl.swing.tank;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestTank extends DiKaErPlus {

    SimpleTank tank;
    Gun gun2;


    @Override
    protected void gameInitial() {
        super.gameInitial();
        createTank();
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

        gun2 = new Gun();
        gun2.setOutlines(
                new Vector2D[]{
                        new Vector2D(-0.1,-0.5),new Vector2D(0.1,-.5),new Vector2D(0.1,.5),new Vector2D(-0.1,.5)
                }
        );

        tank.setWheel(wheel);
        tank.setPaoTai(paoTai);
        tank.setGun(gun);
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
            tank.wheel.rotateLocalClockWise(delta);
        }
        if (kewDown_D) {
            tank.forward(delta, 3);
            tank.wheel.rotateLocalOnClock(delta);
        }

        if (keyDown_UP)
            tank.rotatePaoTaiClockWise(delta);
        if (keyDown_DOWN)
            tank.rotatePaoTaiClock(delta);

        if (keyDown_Space)
            if (!tank.isFiring())
                tank.setFiring(true);

        if (keyDown_Tab)
            tank.setGun(gun2);

        tank.setFiring(tank.isFiring() && keyDown_Space);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        tank.update(delta);
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        tank.draw(g2,this);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestTank());
    }
}
