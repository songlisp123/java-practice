package com.snl.swing.game.anime;

import com.snl.swing.game.utils.Utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class TextMoveEffect  {

    private double leftX,leftY;
    private double w,h;
    private int beginning,ending;
    private final String string;

    public TextMoveEffect(String string, double leftX,
                          double leftY, double w,
                          double h, int beginning, int ending) {
        this.string = string;
        this.leftX = leftX;
        this.leftY = leftY;
        this.w = w;
        this.h = h;
        this.beginning = beginning;
        this.ending = ending;
    }

    public TextMoveEffect(String string, double w,
                          double h, int beginning, int ending) {
        this.string = string;
        this.w = w;
        this.h = h;
        this.beginning = beginning;
        this.ending = ending;
    }

    public int getEnd() {
        return ending;
    }

    public int getStart() {
        return beginning;
    }

    public void render(int w, int h, Graphics2D g2) {
        FontRenderContext frc = g2.getFontRenderContext();
        AttributedString as = new AttributedString(string);
        AttributedCharacterIterator it = as.getIterator();
        LineBreakMeasurer lbm = new LineBreakMeasurer(it,frc);

        double dy = 20;
        while (lbm.getPosition() < it.getEndIndex()) {
            TextLayout tl = lbm.nextLayout((float) this.w);
            dy = Utils.drawText(g2,0,dy,this.w,tl);
        }

    }
    public void step() {

    }

    public void reset() {

    }
}
