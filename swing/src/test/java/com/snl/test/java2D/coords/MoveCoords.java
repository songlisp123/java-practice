package com.snl.test.java2D.coords;

import com.snl.test.java2D.UTIL.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class MoveCoords extends DiKaEr {

    //0,1,2……
    int moveMode;
    Body body;
    Body body02;
    Body body03;
    boolean moving;
    //匀加速运动
    double a; //加速度
    static final double G = -10;

    public MoveCoords() throws HeadlessException {
        super();
        moveMode = 0;
        a = 1.0;
        body = new Body(2.0,.0);
        body02 = new Body(0.0,0.0);
        body03 = new Body(1.0,10.0);
    }


    @Override
    public void processInput(double delta) {
        super.processInput(delta);
        //处理按键事件
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_1))
        {
            moveMode = 0;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_2))
        {
            moveMode = 1;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_3))
        {
            moveMode = 2;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_W))
        {
            a = Math.min(5,++a);
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_S))
        {
            a = Math.max(-5,--a);
        }

    }

    @Override
    public void reset() {
        super.reset();
        body = new Body(2.0,.0);
        body02 = new Body(.0,.0);
        body03 = new Body(1.0,10.0);
    }

    @Override
    public void updateSprite(double delta) {
        super.updateSprite(delta);
        //TODO
        if (moving)
            handleMoving(delta);
    }

    private void handleMoving(double delta) {
        body.update(delta,0,0);
        body02.update(delta,1,a);
        body03.update(delta,2,G);
        handleCollide();
    }

    private void handleCollide() {
        int width = c.getWidth();
        Point2D sP = camera.worldToScreen(body02.getO());
        if (sP.getX() + body.getW() > width) {
            body02 = new Body(0.0,0.0);
            body = new Body(2.0,.0);
            body03 = new Body(1.0,10.0);
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setColor(Color.WHITE);
        g2.drawString("运动 : [%s]".formatted(moving),30,170);
        g2.drawString("加速度: %.2f".formatted(a),30,190);
        g2.drawString("a1距离: %.2f".formatted(body.getD()),30,210);
        g2.drawString("a2距离: %.2f".formatted(body02.getD()),30,230);
        g2.drawString("a3距离: %.2f".formatted(body03.getD()),30,250);
        g2.drawString("a1速度: %.2f".formatted(body.getSpeed()),30,270);
        g2.drawString("a2速度: %.2f".formatted(body02.getSpeed()),30,290);
        g2.drawString("a3速度: %.2f".formatted(body03.getSpeed()),30,310);
        //TODO
        drawBody(g2);
        g2.dispose();
    }

    private void drawBody(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        Point2D sP,bp2,bp3;
        sP = camera.worldToScreen(body.getO());
        int w = body.getW();
        int h = body.getH();
        var s = new Ellipse2D.Double(
                sP.getX() - w / 2.0, sP.getY() - h / 2.0, w,h
        );
        g2.drawString("a1", (int) sP.getX(), (int) (sP.getY() - 10));
        g2.fill(s);

        g2.setColor(Color.red);
        bp2 = camera.worldToScreen(body02.getO());
        var body2 = new Ellipse2D.Double(
                bp2.getX() - w / 2.0, bp2.getY() - h / 2.0, w,h
        );
        g2.drawString("a2", (int) bp2.getX(), (int) (bp2.getY() - 10));
        g2.fill(body2);

        g2.setColor(Color.green);
        bp3 = camera.worldToScreen(body03.getO());
        var b3 = new Ellipse2D.Double(
                bp3.getX() - w / 2.0, bp3.getY() - h / 2.0, w,h
        );
        g2.drawString("a3", (int) bp3.getX(), (int) (bp3.getY() - 10));
        g2.fill(b3);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MoveCoords::new);
    }
}
