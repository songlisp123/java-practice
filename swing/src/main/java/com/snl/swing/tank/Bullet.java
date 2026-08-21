package com.snl.swing.tank;

import com.snl.swing.game.math.Vector2D;


public class Bullet {
    //ai推荐优化,分离世界物体与父节点
    Vector2D position;
    Vector2D direction;

    double speed;

    public Bullet() {
    }


    public void update(double delta) {
        position =
                position.add(
                        direction.mul(speed*delta)
                );
    }


    public static Bullet createBullet(Vector2D position,Vector2D direction,double speed) {
        Bullet bullet = new Bullet();
        bullet.direction = direction;
        bullet.position = position;
        bullet.speed = speed;
        return bullet;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
}
