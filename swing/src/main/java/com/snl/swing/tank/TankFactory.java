//package com.snl.swing.tank;
//
//import com.snl.swing.game.math.Matrix3x3f;
//import com.snl.swing.game.math.Vector2D;
//
//public class TankFactory {
//
//    public static Tank createTank(Vector2D[] base,double r,Vector2D[] gun,Vector2D[] wheel) {
//        Tank tank = new Tank();
//        tank.base.setOutlines(base);
//        tank.pt.r = r;
//        tank.gun.setOutlines(gun);
//        tank.wheel.setOutlines(wheel);
//        tank.wheel.setWheelPattern(
//                new Vector2D[]{
//                        new Vector2D(-0.1,-0.25),new Vector2D(0.1,-0.25),new Vector2D(0.1,0.25),new Vector2D(-0.1,0.25)
//                }
//        );
//        tank.gun.transFrom = Matrix3x3f.translate(0,r);
//        return tank;
//    }
//}
