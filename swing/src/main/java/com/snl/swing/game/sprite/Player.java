package com.snl.swing.game.sprite;

import com.snl.swing.game.input.CheckInputEvent;
import com.snl.swing.game.input.MouseInputEvent;
import com.snl.swing.game.math.Epsilon;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player {

    private String name;

    private Vector2D pos;
    private BufferedImage image,cache;

    private Vector2D speed;
    private Vector2D previousFrameSpeed;

    //加速度
    private Vector2D acc;

    private boolean rm,lm,up,down,accing;

    public Player(String name,BufferedImage image,Vector2D pos,Vector2D speed) {
        this.name = name;
        this.pos = pos;
        this.image = image;
        this.speed = previousFrameSpeed = speed;
        this.acc = new Vector2D(1,1);
    }


    public void draw(Graphics2D g2, Matrix3x3f view) {
        Vector2D posed = view.mul(pos);
        g2.drawImage(image, (int) posed.x, (int) posed.y,null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void update(double delta) {
        //更新用户
        if (up)
            moveY(delta);
        else if (down) {
            moveY(delta);
        }
        if (rm)
            moveX(delta);
        else if (lm) {
            moveX(-delta);
        }

    }

    private void moveY(double delta) {
        double dy = 0;
        if (!accing) {
            //如果，这一帧与上一帧率的速度为零,物体做匀速直线
            if (speed.y != 0)
                //物体匀速直线运动
                dy = speed.y * delta;
            else
                //静止
                dy = 0;
            //NO TODO
        }else {
            //否则，物体做匀变速运动
            dy = (previousFrameSpeed.y + speed.y) * delta / 2.0;
            previousFrameSpeed.y = speed.y;
            speed.y += acc.y * delta;
        }
        pos.y += dy;
    }

    private void moveX(double delta) {
        double dx = 0;
        if (!accing) {
            //如果，这一帧与上一帧率的速度为零,物体做匀速直线
            if (speed.x != 0)
                //物体匀速直线运动
                dx = speed.x * delta;
            else
                //静止
                dx = 0;
            //NO TODO
        }else {
            //否则，物体做匀变速运动
            dx = (previousFrameSpeed.x + speed.x) * delta / 2.0;
            previousFrameSpeed.x = speed.x;
            speed.x += acc.x * delta;
        }
        pos.x += dx;
    }

    public void processInput(MouseInputEvent mouseInputEvent, CheckInputEvent keyBoardEvent) {
        up = keyBoardEvent.keyDown(KeyEvent.VK_W); //向上
        down = keyBoardEvent.keyDown(KeyEvent.VK_S); //想下
        lm = keyBoardEvent.keyDown(KeyEvent.VK_A); //向左
        rm = keyBoardEvent.keyDown(KeyEvent.VK_D); //向右
        accing = keyBoardEvent.keyDown(KeyEvent.VK_SHIFT);//加速键
    }

    public Vector2D getPos() {
        return pos;
    }
}
