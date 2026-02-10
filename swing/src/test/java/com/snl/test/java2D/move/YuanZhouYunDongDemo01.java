package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class YuanZhouYunDongDemo01 extends DiKaErPlus {

    Vector2D c0,c0Pos;
    double r0; //圆球半径
    ImageIcon image01;
    boolean moving;

    double rot,theta;


    Vector2D[] pol,polCopy;


    public YuanZhouYunDongDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        image01 = new ImageIcon("images/龙王.png");
        initialPos();
        r0 = 0.25;
        theta = Math.PI / 2;
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
    }

    private void initialPos() {
        c0Pos = new Vector2D(2,0);
        rot = 0;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += delta * theta;
        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        mat = mat.mul(Matrix3x3f.translate(c0Pos.getX(), c0Pos.getY()));
        c0 = mat.mul(new Vector2D());

        handleView(c0);
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
        drawImage(g2,image01.getImage(),c0);
        g2.setColor(Color.cyan);
        drawPolygon(g2,polCopy);
        g2.drawString("按下 SPACE 开始",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new YuanZhouYunDongDemo01());
    }
}
