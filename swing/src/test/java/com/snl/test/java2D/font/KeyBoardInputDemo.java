package com.snl.test.java2D.font;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class KeyBoardInputDemo extends DiKaErPlus {

    Font f = new Font("隶书",Font.ITALIC,50);
    String testString = "花有重开日，人无再少年";
    boolean kangjuchi;
    int currentPos;
    TextLayout tl;
    double count,animationTime;

    public KeyBoardInputDemo() throws HeadlessException {
        drawAxis = false;
        currentPos = 0;
        animationTime = 0.125;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B))
            kangjuchi = !kangjuchi;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (tl == null)
            return;
        count += delta;
        if (count > animationTime) {
            count = 0;
            if (currentPos++ == tl.getCharacterCount())
                currentPos = 0;
        }
    }


    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(f);
        g2.setColor(Color.WHITE);
        FontRenderContext frc = g2.getFontRenderContext();
        int w = c.getWidth();
        int h = c.getHeight();
        tl = new TextLayout(testString,f,frc);
        float advance = tl.getAdvance();
        float ascent = tl.getAscent();
        float rx = (float) ((w - advance) / 2.0);
        float ry = h / 2.0F;
        Line2D l = new Line2D.Double(0,ry,w,ry);
        g2.draw(l);
        //获取高亮形状
        Shape highlight = tl.getLogicalHighlightShape(0,currentPos);

        AffineTransform at = AffineTransform.getTranslateInstance(rx, ry+ascent);
        highlight = at.createTransformedShape(highlight);

        float fy = (float) highlight.getBounds().getY();
        float hh = highlight.getBounds().height;
        Rectangle2D rect = new Rectangle2D.Double(rx,fy,advance,hh);
        g2.setColor(Color.CYAN);
        g2.fill(highlight);
        // 获取光标
        Shape[] caretShapes = tl.getCaretShapes(currentPos);
        Shape shape = at.createTransformedShape(caretShapes[0]);
        g2.setColor(Color.WHITE);
        tl.draw(g2,rx,ry+ascent);
        g2.draw(shape);
        g2.draw(rect);
        //绘制前进距离
        Font font = f.deriveFont(10F);
        for (int i= 0;i<=tl.getCharacterCount();i++) {
            float[] caretInfo = tl.getCaretInfo(TextHitInfo.leading(i));
            String value = String.valueOf(caretInfo[0]);
            TextLayout tl = new TextLayout(value,font,frc);
            tl.draw(g2, rx + caretInfo[0] - tl.getAdvance() / 2, fy + hh + tl.
                    getAscent() + 1.0f);
        }
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new KeyBoardInputDemo());
    }
}
