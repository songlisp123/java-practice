package com.snl.test.java2D.game;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class FlyingShipExample extends DiKaErPlus {

    private PrototypeShip ship;
    private PolygonWrapper wrapper;
    List<PrototypeBullet> bullets;

    public FlyingShipExample() {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        bullets = new ArrayList<>();
        wrapper = new PolygonWrapper(wordWidth,wordHeight);
        ship = new PrototypeShip(wrapper);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_LEFT))
            //左旋
            ship.rotateLeft(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_RIGHT))
            //右旋
            ship.rotateRight(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            bullets.add(ship.launchBullets());
            //装填弹药
        }
        ship.setThrustion(keyBoardEvent.keyDown(KeyEvent.VK_UP));
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        ship.update(delta);
        List<PrototypeBullet> copy = new ArrayList<>(bullets);
        for (PrototypeBullet b : copy)
        {
            b.update(delta);
            if (!(wrapper.hasInWorld(b.getPos())))
                bullets.remove(b);
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        Matrix3x3f view = getViewportTransform();
        ship.draw(g2,view);
        for (PrototypeBullet bullet : bullets)
            bullet.draw(g2,view);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new FlyingShipExample());
    }
}
