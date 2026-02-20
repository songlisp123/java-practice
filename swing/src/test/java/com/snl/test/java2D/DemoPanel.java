package com.snl.test.java2D;

import com.snl.test.java2D.UTIL.Utils;
import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;

public class DemoPanel extends DiKaErPlus implements SlideDataChangeListener {

    SlideDemo slideDemo;
    boolean clicked;
    Font f = new Font("Chiller",Font.PLAIN,25);
    TextLayout tl;

    public DemoPanel() throws HeadlessException {
        drawAxis = false;
        slideDemo = new SlideDemo(100,10,500,20);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D mouse = Utils.vectorCovertToPoint(mousePos);
        if (slideDemo.isClicked(clicked,mouse) ||
            slideDemo.isDragging(dragging,mouse))
            slideDemo.move(mouse);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.WHITE);
        //居中文本
        int w = c.getWidth();
        int h = c.getHeight();

        FontRenderContext frc = g2.getFontRenderContext();
        tl = new TextLayout("music",f,frc);
        float ascent = tl.getAscent();
        float leftX = w / 4.0F;
        float leftY = h / 4.0F;
        //获取滑动条
        double slideX = leftX + 80;
        double slideY =leftY + ascent / 2.0;
        slideDemo.draw(g2);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new DemoPanel());
    }

    @Override
    public void change(Object source, double oldValue, double newValue) {

    }
}
