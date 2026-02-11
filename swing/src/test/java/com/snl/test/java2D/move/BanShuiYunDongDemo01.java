package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class BanShuiYunDongDemo01 extends DiKaErPlus {

    Vector2D c0,c0Pos;
    ImageIcon image01,image02,image03;
    boolean moving;
    double rot,theta;
    Vector2D[] pol,polCopy;
    Vector2D radius,c1;
    Vector2D c2;
    double rot02,theta02;
    double rot03,theta03;
    Vector2D speed;
    Vector2D c0Copy;
    double count;
    Vector2D rotateRadius;

    List<guiJi> guiJis = new ArrayList<>(20);

    public BanShuiYunDongDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        image03 = new ImageIcon("images/愤怒.png" );
        initialPos();
        theta = Math.PI / 2;
        theta02 = Math.PI / 2;
        theta03 = Math.PI ;
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
        radius = new Vector2D(2,0);
        rotateRadius = new Vector2D(8,8);
    }

    private void initialPos() {
        c0Pos = new Vector2D(4,0);
        c0 = new Vector2D(5,5);
        c0Copy = c0.clone();
        speed = new Vector2D(1.5,0);
        rot = 0;
        rot02 = 0;
        rot03 = 0;
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
        if (moving) {
            rot += delta * theta;
            rot02 += delta * theta02;
            rot03 += delta * theta03;
        }
        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        c0 = c0Copy.sub(rotateRadius);
        c0 = mat.mul(c0);
        c0 =c0.add(rotateRadius);
        count += delta;
        if (count >= 4 / 12.0) {
            guiJi guiJi = new guiJi(c0, image01);
            guiJis.add(guiJi);
            count = 0;
        }
        for (guiJi g : guiJis)
        {
            g.update(delta);
        }
        guiJis.removeIf(next -> next.dead);
        handleView(c0);
        Matrix3x3f ro = Matrix3x3f.rotate(rot02);
        ro = ro.mul(Matrix3x3f.translate(6,radius.getY()));
        ro = mat.mul(ro);
        c1 = ro.mul(new Vector2D());

        Matrix3x3f c = Matrix3x3f.rotate(rot03);
        c = c.mul(Matrix3x3f.translate(2,radius.getY()));
        c = ro.mul(c);
        c2 = c.mul(new Vector2D());

    }

    private void handleView(Vector2D pos) {
        viewMat = Matrix3x3f.translate(-c0.getX(), -c0.getY());
        Matrix3x3f view = getViewportTransform();
        axis.createAxis(view,c,wordWidth);
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
        drawImage(g2,image02.getImage(),c1);
        drawImage(g2,image03.getImage(),c2);
        g2.setColor(Color.cyan);
        drawPolygon(g2,polCopy);
        for (guiJi j : guiJis)
        {
            drawImage(g2,j.icon.getImage(),j.pos);
        }
        g2.drawString("按下 SPACE 开始",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new BanShuiYunDongDemo01());
    }

    class guiJi {
        Vector2D pos;
        ImageIcon icon;
        boolean dead;
        double start;

        public guiJi(Vector2D pos, ImageIcon icon) {
            this.pos = pos;
            this.icon = icon;
            start = 0;
            dead = false;
        }

        void update(double delta) {
            start += delta;
            if (start >= 4) //1秒
            {
                dead = true;
            }
        }
    }
}
