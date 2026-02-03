package com.snl.test.TIME.coords;

import com.snl.test.TIME.Body;
import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class CollisionDemo extends DiKaEr {

    Body body;
    Body body02;

    boolean moving;

    public CollisionDemo() throws HeadlessException {
        super();
        createBody01();
        createBody02();
    }

    private void createBody02() {
        body02 = new Body(5.0,.0);
        body02.setO(new Point2D.Double(3.0,.0));
    }

    private void createBody01() {
        body = new Body(2.0,.0);
        body.setW(20);
        body.setH(20);
    }

    @Override
    public void reset() {
        super.reset();
        createBody01();
        createBody02();
    }

    @Override
    public void processInput(double delta) {
        super.processInput(delta);
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }
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
        body02.update(delta,0,0);
        handleCollide(delta);
    }

    private void handleCollide(double delta) {
        int width = c.getWidth();
        Point2D sP = camera.worldToScreen(body02.getO());
        if (sP.getX() + body.getW() > width || sP.getX() <= 0) {
            //碰到
            body02.flipXSpeed();
        }

        Point2D sp2 = camera.worldToScreen(body.getO());
        if (sp2.getX() + body.getW() > width || sp2.getX() <= 0) {
            //碰到
            body.flipXSpeed();
        }

        //检测碰撞
        double right = sP.getX() + body02.getW() ;
        double left = sP.getX() ;
        double dxLeft = Math.abs(right - sp2.getX());
        double dxRight = Math.abs(left - sp2.getX() + body.getW());

        double min = Math.min(dxLeft,dxRight);

        if (min<=body.getW())
        {
            body02.flipXSpeed();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setColor(Color.WHITE);
        g2.drawString("运动 : [%s]".formatted(moving),30,170);
        g2.drawString("a1距离: %.2f".formatted(body.getD()),30,210);
        g2.drawString("a2距离: %.2f".formatted(body02.getD()),30,230);
        g2.drawString("a1速度: %.2f".formatted(body.getSpeed()),30,250);
        g2.drawString("a2速度: %.2f".formatted(body02.getSpeed()),30,270);
        //TODO
        drawBody(g2);
        g2.dispose();
    }

    private void drawBody(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        Point2D sP,bp2;
        sP = camera.worldToScreen(body.getO());
        int w = body.getW();
        int h = body.getH();
        var s = new Ellipse2D.Double(
                sP.getX() - w / 2.0, sP.getY() - h / 2.0, w,h
        );
        g2.drawString("a1", (int) sP.getX(), (int) (sP.getY() - 10));
        g2.fill(s);


        w = body02.getW();
        h = body02.getH();
        g2.setColor(Color.red);
        bp2 = camera.worldToScreen(body02.getO());
        var body2 = new Ellipse2D.Double(
                bp2.getX() - w / 2.0, bp2.getY() - h / 2.0, w,h
        );
        g2.drawString("a2", (int) bp2.getX(), (int) (bp2.getY() - 10));
        g2.fill(body2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CollisionDemo::new);
    }
}
