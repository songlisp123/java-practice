package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ZhiXianYunDongDemo02 extends DiKaErPlus {

    ImageIcon image01;
    Vector2D v1,v1Pos;

    double a,oldXSpeed;

    boolean moving;
    Vector2D[] pol,polCopy;

    public ZhiXianYunDongDemo02() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        image01 = new ImageIcon("images/龙王.png");
        initialSpeed();
        a = 0.5;
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
    }

    private void initialSpeed() {
        v1 = v1Pos = new Vector2D();
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
        if (moving)
            v1Pos = handlePos(delta,v1,a,v1Pos);
        handleView(v1Pos);

    }

    private Vector2D handlePos(double delta, Vector2D speed, double a, Vector2D pos) {
        oldXSpeed = speed.getX();
        double newSpeed = oldXSpeed + delta * a;
        double dx = (newSpeed + oldXSpeed) * delta / 2.0;
        v1 = new Vector2D(newSpeed,0);
        return pos.add(new Vector2D(dx,0));
    }

    private void handleView(Vector2D pos) {
        viewMat = Matrix3x3f.translate(-pos.getX(),pos.getY());
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
        drawImage(g2,image01.getImage(),v1Pos);
        g2.setColor(Color.cyan);
        g2.drawString("按下 SPACE 开始",30,130);
        drawPolygon(g2,polCopy);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        initialSpeed();
    }

    public static void main(String[] args) {
        launchGame(new ZhiXianYunDongDemo02());
    }
}
