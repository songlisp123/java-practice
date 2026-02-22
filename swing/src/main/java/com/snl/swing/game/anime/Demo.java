package com.snl.swing.game.anime;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.keyBoard.SimpleCleanKeyBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Demo extends DiKaErPlus {

    Scene scene;
    List<Part> parts;
    SimpleCleanKeyBoard keyBoard;
    TextMoveEffect tm;

    public Demo() throws HeadlessException {
        drawAxis = false;
        parts = new ArrayList<>();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        TexturePaintEffect t1 =  new TexturePaintEffect(
                TexturePaintEffect.OI, Color.BLACK, Color.YELLOW, 30, 20,20);
        DitherDissolveEffect de = new DitherDissolveEffect(0,30,4,c);
        GunDongEffect effect = new GunDongEffect(0,300,c);
        tm = new TextMoveEffect("你好，我是一个傻逼",c.getWidth(),c.getHeight(),50,100);
        parts.add(effect);
        scene = new Scene(parts,"演示","56");
        keyBoard = new SimpleCleanKeyBoard(50,50,300,300);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        scene.step(c.getWidth(),c.getHeight());
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        keyBoard.draw(g2,Color.lightGray);
        g2.setColor(Color.green);
//        g2.fillRect(0,0,c.getWidth(),c.getHeight());
        scene.render(c.getWidth(),c.getHeight(),g2);
        tm.render(c.getWidth(),c.getHeight(),g2);
        g2.dispose();
    }

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (isBeyond())
            scene.increment();
        else
            scene.reset(c.getWidth(),c.getHeight());
    }

    private boolean isBeyond() {
        return scene.getIndex() <= scene.getLength();
    }

    public static void main(String[] args) {
        launchGame(new Demo());
    }
}
