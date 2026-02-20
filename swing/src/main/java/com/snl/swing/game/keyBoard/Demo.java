package com.snl.swing.game.keyBoard;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import java.awt.*;
import java.awt.geom.Point2D;

public class Demo extends DiKaErPlus {

    SimpleCleanKeyBoard keyBoard;
    public Demo() throws HeadlessException {
        drawAxis = false;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        keyBoard = new SimpleCleanKeyBoard(0,300,c.getWidth(),c.getHeight() / 2.0);
        keyBoard.setDrawBorder(false);
        keyBoard.setDrawCharBorder(false);
        keyBoard.setShowingInputFrame(true);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        keyBoard.processInput(mouseInputEvent,keyBoardEvent);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D po = mouseInputEvent.getCurrentPoint();
        keyBoard.update(delta,po);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        keyBoard.draw(g2,Color.WHITE);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new Demo());
    }
}
