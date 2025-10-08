package wormsubject.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

public class CustomButton extends JButton {
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int DEFAULT_WIDTH = 90;
    private static final int DEFAULT_HEIGHT = 75;


    public CustomButton(String name) {
        super(name);
        //不绘制默认背景
        setContentAreaFilled(false);
        //设置无焦点
        setFocusPainted(true);
        //背景透明
        setOpaque(false);
        setForeground(Color.BLACK);
        addMouseMotionListener(new MouseMotion());
        addMouseListener(new MouseHandler());

    }

    @Override
    public void paintComponent(Graphics g) {
//        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0,0,getWidth(),getHeight(),WIDTH,HEIGHT);
        g2.dispose();
        super.paintComponent(g);
    }

    public void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,
                WIDTH,HEIGHT));
        g2.dispose();
    }

    private class MouseMotion implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e){
            Point point = e.getPoint();
            boolean contains = contains(point);
            if (contains) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (contains(e.getPoint())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }


}
