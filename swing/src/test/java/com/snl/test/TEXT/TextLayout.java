package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class TextLayout extends JPanel {

    private ImageIcon icon;

    public TextLayout() {
        icon = new ImageIcon("ten.gif");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("隶书",Font.PLAIN,72);
        String s = "我是大傻逼";
//        var hashtable = new Hashtable<AttributedCharacterIterator.Attribute,Object>();
//        hashtable.put(TextAttribute.FONT,font);
//        hashtable.put(TextAttribute.FOREGROUND,Color.RED);
        java.awt.font.TextLayout textLayout =
                new java.awt.font.TextLayout(s, font, g2.getFontRenderContext());

        AffineTransform af = AffineTransform.getTranslateInstance(80, 80);
        Shape shape = textLayout.getOutline(af);
        Rectangle2D bounds = textLayout.getBounds();
        bounds.setFrame(0,0,bounds.getWidth(),bounds.getHeight());

        g2.draw(shape);
        g2.draw(bounds);
        //绘制基线
        g2.drawLine(80,80,160,80);
        //绘制下降线
        Line2D descend = new Line2D.Double(80,80 + textLayout.getDescent(),160,80 + textLayout.getDescent());
        g2.draw(descend);

        //上升线
        double y = 80 - textLayout.getAscent();
        descend = new Line2D.Double(80,y,160,y);
        g2.draw(descend);
        //绘制行间距
        y = 80 + textLayout.getDescent() + textLayout.getLeading();
        descend = new Line2D.Double(80,y,160,y);
        g2.draw(descend);
        g2.setPaint(Color.MAGENTA);
//        textLayout.draw(g2,85,85);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new TextLayout();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextLayout::createUi);
    }
}
