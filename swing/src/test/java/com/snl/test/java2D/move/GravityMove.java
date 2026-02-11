package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;

public class GravityMove extends DiKaErPlus {

    Vector2D c0pos,c1pos;
    Vector2D speed,speed01;
    double g = 9.98;
    ImageIcon image01,image02;
    boolean m1,m2;

    public GravityMove() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        c0pos = new Vector2D(0,5);
        c1pos = new Vector2D(2,5);
        speed = new Vector2D(0,0);
        speed01 = speed.clone();
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        m1 = m2 = true;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        c0pos = handleMovtion(c0pos,delta,speed,1.0);
        c1pos = handleMovtion(c1pos,delta,speed01,0.95);
    }

    private Vector2D handleMovtion(Vector2D pos, double delta, Vector2D speed, double fractor) {
        double oldSpeed = speed.getY();
        double newYSpeed = oldSpeed - g * delta;
        if (pos.getY() < 0) {
            newYSpeed = -fractor * newYSpeed;
            pos.setY(0);
        }else {
            double dy = (newYSpeed + oldSpeed) * delta / 2.0;
            pos = pos.add(new Vector2D(0, dy));
        }
        speed.setX(speed.getX());
        speed.setY(newYSpeed);
        return pos;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        //TODO
        drawImage(g2,image01.getImage(),c0pos);
        drawImage(g2,image02.getImage(),c1pos);
        g2.dispose();
    }


    public static void main(String[] args) {
        launchGame(new GravityMove());
    }
}
