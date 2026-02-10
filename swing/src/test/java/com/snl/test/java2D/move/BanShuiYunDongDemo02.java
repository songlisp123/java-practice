package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class BanShuiYunDongDemo02 extends DiKaErPlus {

    Vector2D c0Pos;
    ImageIcon image01,image02,image03;
    boolean moving;
    Vector2D[] pol,polCopy;
    Vector2D speed,moveDelta;
    boolean center;

    //小球
    Vector2D radius;
    Vector2D c1;
    double rot,theta;

    public BanShuiYunDongDemo02() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        initialPos();
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
        radius = new Vector2D(3,0);
        theta = Math.PI / 4;
    }

    private void initialPos() {
        c0Pos = new Vector2D();
        speed = new Vector2D(1.5,0);
        rot = 0;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R))
        {
            center = true;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_H))
        {
            center = false;
            viewMat = Matrix3x3f.identity();
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (center)
        {
            viewMat = Matrix3x3f.identity()
                    .mul(Matrix3x3f.translate(-c0Pos.getX(), c0Pos.getY()));
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }
        updateSpeed(speed,delta);
        handleView(c0Pos);
    }

    private void updateSpeed(Vector2D speed, double delta) {
        double dx = speed.getX() * delta;
        moveDelta= new Vector2D(dx,0);
        c0Pos = c0Pos.add(moveDelta);

        rot += theta * delta;
        Matrix3x3f rot = Matrix3x3f.rotate(this.rot);
        rot = rot.mul(Matrix3x3f.translate(radius.getX(),radius.getY()));
        Vector2D v = rot.mul(new Vector2D());
        c1 = c0Pos.add(v);
    }

    private void handleView(Vector2D pos) {
        Matrix3x3f view = getViewportTransform();
        for (int i=0;i<polCopy.length;i++)
        {
            polCopy[i] = view.mul(pos.add(pol[i]));
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        drawImage(g2,image01.getImage(),c0Pos);
        drawImage(g2,image02.getImage(),c1);
        g2.setColor(Color.cyan);
        drawPolygon(g2,polCopy);
        g2.drawString("按下 SPACE 开始",30,130);
        g2.drawString("按下 R 键居中",30,150);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new BanShuiYunDongDemo02());
    }
}
