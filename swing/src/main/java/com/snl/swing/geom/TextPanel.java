package com.snl.swing.geom;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class TextPanel extends JPanel {

    private final String content;

    public TextPanel(String content) {
        this.content = content;
        setBackground(Color.black);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.green);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        float textCor = 30.f;
        AttributedString as = new AttributedString(content);
        AttributedCharacterIterator iterator = as.getIterator();
        FontRenderContext frc = g2.getFontRenderContext();

        LineBreakMeasurer lbm = new LineBreakMeasurer(iterator,frc);
        TextLayout tl;
        while (lbm.getPosition() < iterator.getEndIndex()) {
            tl = lbm.nextLayout(getWidth() - 40);
            tl.draw(g2,20.f,textCor);
            textCor += tl.getDescent() + tl.getAscent() + tl.getLeading();

        }
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300,300);
    }
}
