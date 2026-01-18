package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

public class HitMe extends JPanel implements MouseListener {

    private TextLayout layout;
    private int mX = 40,mY = 40;

    public HitMe() {
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //设置渲染提示
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        String s = "你好，世界";
        Font font = new Font("隶书",Font.PLAIN,32);
        if (layout == null) {
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            layout = new TextLayout(s,font,fontRenderContext);
        }
        layout.draw(g2,mX,mY);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new HitMe();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HitMe::createUi);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TextHitInfo info = layout.hitTestChar(
                e.getX() - mX,
                e.getY() - mY
        );
//        System.out.println("info = " + info);
        System.out.println("info.getInsertionIndex() = " + info.getInsertionIndex());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
