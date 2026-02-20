package com.snl.test.java2D;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class DemoPanel02 extends DiKaErPlus {

    int rw,rh;
    int hgap;
    int padeLeft,padRight;
    GeneralPath[] paths;
    boolean clicked;
    int pathIndex = -1,clickedIndex = -1;
    float alpha;

    int selectIndex;
    final String[] strings = {
            "普通难度" , "地狱难度" ,"噩梦难度"
    };
    Font font = new Font("隶书",Font.ITALIC|Font.BOLD,25);

    public DemoPanel02() throws HeadlessException {
        drawAxis = false;
        rw = 50;
        rh = 50;
        hgap = 30;
        padeLeft = padRight = 150;
        paths = new GeneralPath[2];
        alpha = 1.0F;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        fill();
    }

    private void fill() {
        //获取中心
        int w = c.getWidth();
        int h = c.getHeight();

        double leftX = padeLeft;
        double leftY = h * 3 / 4.0 - rh / 2.0;
        var s1 = new GeneralPath();
        s1.moveTo(leftX,leftY + rh / 2.0);
        s1.lineTo(leftX + rw,leftY);
        s1.lineTo(leftX + rw,leftY + rh);
        s1.closePath();
        paths[0] = s1;
        //绘制第二个
        var s2 = new GeneralPath();
        leftX = w - padRight;
        s2.moveTo(leftX,leftY + rh / 2.0);
        s2.lineTo(leftX - rw,leftY);
        s2.lineTo(leftX - rw,leftY + rh);
        s2.closePath();
        paths[1] = s2;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D mouse = mouseInputEvent.getCurrentPoint();
        pathIndex = checkPos(mouse);
        clicked = clicked && pathIndex != -1;
        if (clicked) {
            clickedIndex = pathIndex;
            alpha = 1.0F;
            if (clickedIndex == 0)
                //点击左边
                selectIndex = Math.max(0,--selectIndex);
            else
                selectIndex = Math.min(strings.length-1,++selectIndex);
        }
        clicked = false;
        updateAlpha(delta);
    }

    private void updateAlpha(double delta) {
        alpha = (float) Math.max(0,alpha-delta);
    }

    private int checkPos(Point2D mouse) {
        for (int i=0;i<paths.length;i++)
        {
            GeneralPath path = paths[i];
            if (path.contains(mouse))
            {
                return i;
            }
        }
        return  -1;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //获取中心
        for (GeneralPath p: paths)
            g2.draw(p);
        if (pathIndex != -1) {
            GeneralPath path = paths[pathIndex];
            g2.setColor(new Color(1.0F,1.0F,1.0F,0.5F));
            g2.fill(path);
        }

        if (clickedIndex != -1)
        {
            GeneralPath path = paths[clickedIndex];
            g2.setColor(new Color(1.0F,0.0F,1.0F,alpha));
            g2.fill(path);
        }

        //绘制文字
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout(strings[selectIndex], font, frc);
        //获取中心
        g2.setColor(Color.WHITE);
        float ascent = tl.getAscent();
        float advance = tl.getAdvance();
        float descent = tl.getDescent();
        float leading = tl.getLeading();

        float leftX = (c.getWidth() - advance) / 2.0F;
        float leftY = c.getHeight() / 4.0F * 3.0f + ascent / 2.0f;
        tl.draw(g2,leftX,leftY);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new DemoPanel02());
    }
}
