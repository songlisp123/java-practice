package com.snl.swing.game;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.keyBoard.SimpleCleanKeyBoard;
import com.snl.swing.game.utils.Utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GameStartPanel extends DiKaErPlus {

    private SimpleCleanKeyBoard keyBoard;

    public GameStartPanel() throws HeadlessException {
        WIDTH = 600;
        HEIGHT = 600;
        appSleep = 14;
        drawAxis = false;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        keyBoard = new SimpleCleanKeyBoard(0,c.getHeight() / 2.0,
                c.getWidth(),c.getHeight() / 2.0);
        keyBoard.setShowingInputFrame(false);
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
        drawString(g2);
        g2.dispose();
    }

    private void drawString(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        int w = c.getWidth();
        int h = c.getHeight();
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout("您的名字:",appFont.deriveFont(30.0F),frc);
        float lx = (w - tl.getAdvance()) / 2.0F;
        float ly = h / 4.0F;
        tl.draw(g2,lx,ly - tl.getAscent());

        if (keyBoard.getInputString().isEmpty())
            return;
        tl = new TextLayout(keyBoard.getInputString(), Utils.font.deriveFont(30.F),frc);
        float ascent = tl.getAscent();
        float height = ascent + tl.getDescent() + tl.getLeading();
        float advance = tl.getAdvance();
        lx = (w - advance) / 2.0F;
        tl.draw(g2,lx,ly + ascent);
        //绘制基线
        Line2D l = new Line2D.Double(lx,ly+height,lx + advance,ly + height);
        g2.draw(l);
    }

    public static void main(String[] args) {
        launchGame(new GameStartPanel());
    }
}
