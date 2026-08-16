//package com.snl.swing.tank;
//
//import com.snl.swing.game.gameFrame.DiKaErPlus;
//import com.snl.swing.game.math.Vector2D;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//
//public class TankGame02 extends DiKaErPlus {
//
//    Tank tank;
//    Tank atank;
//
//    @Override
//    protected void gameInitial() {
//        super.gameInitial();
//        Vector2D[ ] temp = new Vector2D[]{
//                //本地坐标
//                new Vector2D(-1.75,-1.25),new Vector2D(1.75,-1.25),new Vector2D(1.75,1.25),new Vector2D(-1.75,1.25)
//        };
//        Vector2D[] gun =  new Vector2D[] {
//                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
//        };
//
//        Vector2D wheel[] = new Vector2D[] {
//                new Vector2D(-0.45,-0.25),new Vector2D(0.45,-0.25),new Vector2D(0.45,0.25),new Vector2D(-0.45,0.25)
//        };
////        tank.base.setOutlines(temp);
//        tank = TankFactory.createTank(temp,1,gun,wheel);
//        atank = tank;
//    }
//
//    @Override
//    protected void processInput(double delta) {
//        super.processInput(delta);
//        boolean kewDown_A = keyBoardEvent.keyDown(KeyEvent.VK_A);
//        boolean kewDown_W = keyBoardEvent.keyDown(KeyEvent.VK_W);
//        boolean kewDown_D = keyBoardEvent.keyDown(KeyEvent.VK_D);
//        boolean kewDown_S = keyBoardEvent.keyDown(KeyEvent.VK_S);
//        boolean keyDown_Space = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
//        if (kewDown_A) {
//            tank.setBackwarding(true);
//        }
//
//        if (kewDown_D)
//            tank.setForwarding(true);
//
//        if (kewDown_W) {
//            tank.rotateBaseClockWise(delta);
//            if (!tank.isLeftTurning()) {
//                tank.setLeftTurning(true);
//            }
//        }
//        if (kewDown_S) {
//            tank.rotateBaseOnClock(delta);
//            if (!tank.isRightTurning()) {
//                tank.setRightTurning(true);
//            }
//        }
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_UP))
//            tank.rotatePtClockWise(delta);
//        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN))
//            tank.rotatePtOnClock(delta);
//
//        if (keyDown_Space)
//            tank.setFiring(true);
//
//        if (keyBoardEvent.keyDown(KeyEvent.VK_U))
//            tank.scale(delta);
//        if (keyBoardEvent.keyDown(0x49))
//            tank.deScale(delta);
//
//        tank.setForwarding(kewDown_D && tank.isForwarding());
//        tank.setBackwarding(kewDown_A && tank.isBackwarding());
//        tank.setFiring(keyDown_Space && tank.isFiring());
//        tank.setLeftTurning(tank.isLeftTurning() && kewDown_W );
//        tank.setRightTurning(tank.isRightTurning() && kewDown_S );
//
//        if (tank.leftTurning)
//            tank.rotateWheelClockWise();
//        else if (tank.rightTurning)
//            tank.rotateWheelClock();
//        else
//            tank.wheelRot = 0;
//    }
//
//    @Override
//    protected void updateSprite(double delta) {
//        super.updateSprite(delta);
//        tank.update(delta);
//    }
//
//
//    @Override
//    protected void draw(Graphics g) {
//        super.draw(g);
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(Color.MAGENTA);
//        tank.draw(g2,this);
//
//        g2.dispose();
//    }
//
//    public static void main(String[] args) {
//        launchGame(new TankGame02());
//    }
//}
