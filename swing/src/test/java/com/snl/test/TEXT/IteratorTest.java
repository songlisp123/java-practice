package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.text.AttributedString;

public class IteratorTest extends JPanel {

    private ImageIcon icon;
    private RectangularShape shape;

    public IteratorTest() {
//        setBackground(Color.black);
        icon = new ImageIcon("ten.gif");
        shape = new Rectangle2D.Double(0,0,20,20);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        String s = "我是大傻逼";
        Dimension d = getSize();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font serifFont = new Font("Serif", Font.PLAIN, 48);
        Font sansSerifFont = new Font("Monospaced", Font.PLAIN, 48);

        AttributedString as = new AttributedString(s);

        ImageGraphicAttribute imageGraphicAttribute = new ImageGraphicAttribute(icon.getImage(),
                GraphicAttribute.CENTER_BASELINE,0,10);

        var shapeAttribute = new ShapeGraphicAttribute(shape,GraphicAttribute.CENTER_BASELINE,
                ShapeGraphicAttribute.FILL);

        as.addAttribute(TextAttribute.FONT,serifFont);
        as.addAttribute(TextAttribute.FONT,sansSerifFont,2,5);
        as.addAttribute(TextAttribute.FOREGROUND,Color.RED,2,5);
        as.addAttribute(TextAttribute.UNDERLINE,2,2,5);
        as.addAttribute(TextAttribute.CHAR_REPLACEMENT,imageGraphicAttribute,2,3);
        as.addAttribute(TextAttribute.CHAR_REPLACEMENT,shapeAttribute,1,2);
        as.addAttribute(TextAttribute.FOREGROUND,Color.BLUE,1,2);
        g2.drawString(as.getIterator(),0,40);
        g2.dispose();
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new IteratorTest();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IteratorTest::createUi);
    }
}
