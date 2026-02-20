package com.snl.swing.game.keyBoard;

import com.snl.swing.game.utils.Utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


public class InputFrame {
    //记录按下的字符
    protected StringBuilder builder;
    //当前字符的索引
    private int charIndex = -1;
    //字符是否超出输入框距离
    private boolean filling;

    //包围矩形
    protected double leftX,leftY;
    protected double w,h;

    public InputFrame(double x, double y, double w, double h) {
        leftX = x;
        leftY = y - 2 * h;
        this.w = w;
        this.h = h;
        builder = new StringBuilder();
    }

    public void addString(String s) {
        if (filling) return;
        builder.append(s);
        charIndex = builder.length() - 1;
    }

    public void deleteChar() {
        if (charIndex < 0)
            return;
        builder.deleteCharAt(charIndex);
        charIndex = builder.isEmpty() ? 0 : --charIndex;
    }

    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Shape r = new Rectangle2D.Double(leftX,leftY,w,h);
        g2.draw(r);
        //
        if (builder.isEmpty())
            return;
        FontRenderContext frc = g2.getFontRenderContext();
        var tl = new TextLayout(builder.toString(), Utils.font02,frc);
        float ascent = tl.getAscent();
        float descent = tl.getDescent();
        float advance = tl.getAdvance();
        tl.draw(g2, (float) leftX + 10, (float) (leftY + ascent + descent));
        filling =  (leftX +advance) >= (w - 20);
        if (filling) {
            g2.setColor(Color.red);
            g2.draw(r);
        }
        Shape caretShape = tl.getCaretShape(TextHitInfo.trailing(charIndex));
        AffineTransform af = AffineTransform.getTranslateInstance(leftX + 10,leftY + ascent + descent);
        caretShape = af.createTransformedShape(caretShape);
        g2.draw(caretShape);
    }

    public int getSize() {
        return builder.length();
    }

    public void addCharIndex(int i) {
        charIndex+= i;
        charIndex = Math.max(0,Math.min(charIndex,builder.length()-1));
    }
}
