package com.snl.swing.geom;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.utils.Utils;
import com.snl.swing.game2026.comp.ScrollPanel;
import com.snl.swing.game2d.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public abstract class ProblemSolver extends DiKaErPlus implements TimingTarget {

    Animator animator;
    long duration;
    ScrollPanel scrollPanel;
    StringBuilder sb;
    TextPanel panel;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        sb = new StringBuilder();
        showAnti = true;
        scrollPanel = new ScrollPanel(-1,-1,3,2, ScrollPanel.Mode.VERTICAL);
        startAnimation();
    }

    private void startAnimation() {
        animator = new Animator(duration, this);
        animator.start();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            this.reset();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P)) {
            animator.pause();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R))
            animator.resume();

        if (keyBoardEvent.keyDown(KeyEvent.VK_LEFT) && !animator.isRunning()) {
            //左，回退
            backUp();
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_RIGHT) && !animator.isRunning()) {
            //右边，前进
            forwardUp();
        }

    }

    abstract void forwardUp();

    abstract void backUp();

    @Override
    protected void reset() {
        super.reset();
        animator.stop();
        animator.start();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        var g2 = (Graphics2D) g.create();
        if (showAnti)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.CYAN);
//        drawContext(g2);
        drawContent(g2);
        g2.dispose();
    }

    protected void drawContext(Graphics2D g2) {
        if (sb == null || sb.length() <= 0)
            return;
        float textCor = 130.0f;
        String content = sb.toString();
        AttributedString as = new AttributedString(content);
        AttributedCharacterIterator iterator = as.getIterator();
        FontRenderContext frc = g2.getFontRenderContext();

        LineBreakMeasurer lbm = new LineBreakMeasurer(iterator,frc);
        TextLayout tl;
        while (lbm.getPosition() < iterator.getEndIndex()) {
            tl = lbm.nextLayout(wordWidth * scaleX - 60);
            tl.draw(g2,30.0f,textCor);
            textCor += tl.getDescent() + tl.getAscent() + tl.getLeading();

        }
    }

    abstract void drawContent(Graphics2D g2) ;

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {

    }

    public void openTextPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame F = new JFrame("测试");

            panel = new TextPanel(sb.toString());
            F.setContentPane(panel);
            F.setResizable(false);

            Utils.centerContainer(F);
            System.out.println("F.getX() = " + F.getX());
            int xw = F.getX() - (c.getWidth() - F.getWidth()) /2 - F.getWidth();
            F.setLocation(xw,F.getY());
            F.pack();
            F.setVisible(true);
        });
    }
}