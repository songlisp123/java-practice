package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class RollingText extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        String s = "我是一个大傻逼";
        Font font  =new Font("隶书",Font.BOLD,21);
        FontRenderContext fr = g2.getFontRenderContext();
        g2.translate(40,40);

        var gv = font.createGlyphVector(fr,s);
        int numGlyphs = gv.getNumGlyphs();
        for (int i=0;i<numGlyphs;i++) {
            //获取当前字形的位置
            Point2D point2D = gv.getGlyphPosition(i);
            //随时间旋转
            double theta = (double) i / (double) (numGlyphs -1) * Math.PI / 2;
            AffineTransform at = AffineTransform.getTranslateInstance(
                    point2D.getX(),point2D.getY()
            );
            at.rotate(theta);
            Shape glyph = gv.getGlyphOutline(i);
            Shape transForm = at.createTransformedShape(glyph);
            g2.fill(transForm);
        }

        g2.dispose();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new RollingText();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RollingText::createUi);
    }
}
