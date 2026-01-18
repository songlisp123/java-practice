package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class Highlights extends JPanel implements MouseListener, MouseMotionListener {

    private TextLayout layout;
    private TextHitInfo first,second;
    private int mX = 40,mY = 80;

    public Highlights() {
        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        String s = "你好，世界";
        Font font = new Font("隶书",Font.PLAIN,32);
        if (second == null) {
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            layout = new TextLayout(s,font,fontRenderContext);
        }

        //绘制高亮条
        if (first != null && second != null) {
            Shape shape = layout.getLogicalHighlightShape(
                    first.getInsertionIndex(), second.getInsertionIndex()
            );
            AffineTransform at = AffineTransform.getTranslateInstance(mX, mY);
            Shape hightLight = at.createTransformedShape(shape);
            g2.setPaint(Color.green);
            g2.fill(hightLight);
        }

        g2.setPaint(Color.black);
        layout.draw(g2,mX,mY);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new Highlights();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Highlights::createUi);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        first = layout.hitTestChar(
                e.getX() - mX,
                e.getY() - mY
        );
        second = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        second = layout.hitTestChar(
                e.getX() - mX,
                e.getY() - mY
        );
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        second = layout.hitTestChar(
                e.getX() - mX,
                e.getY() - mY
        );
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
