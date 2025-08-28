package com.apress.bgn.char006.frame.mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MouseComponent extends JComponent {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int SIDE_ELEMENT  = 10;

    private ArrayList<Rectangle2D> squares;
    private Rectangle2D current;
//    private AbstractAction action;

    public MouseComponent() {
        //获取屏幕大小
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = d.width;
        int screenHeight = d.height;
        //在当前窗口中央显示框架
        int x = (screenHeight-DEFAULT_WIDTH)/2;
        int y = (screenHeight-DEFAULT_HEIGHT)/2;
        this.setBounds(x,y,DEFAULT_WIDTH,DEFAULT_HEIGHT);

        //处理正方形
        this.squares = new ArrayList<>();
        current = null;

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Rectangle2D r:squares) {
            g2.draw(r);
        }
     }

    public Rectangle2D find(Point2D p) {
        for (Rectangle2D r:squares) {
            if (r.contains(p)) return r;
        }
        return null;
    }

    public void remove(Rectangle2D r) {
        if (r ==  null) return;
        if (r == current) current = null;
        squares.remove(r);
        repaint();
    }

    public void add(Point2D p) {
        double x = p.getX();
        double y = p.getY();
        current = new Rectangle2D.Double(x-SIDE_ELEMENT/2,y-SIDE_ELEMENT/2,
                SIDE_ELEMENT,SIDE_ELEMENT);
        squares.add(current);
        repaint();
    }

    private class MouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent event) {
            //添加一个新对象,如果图表不在方框内
            Point2D mousePoint = event.getPoint();
            current = find(mousePoint);
            if (current == null) add(mousePoint);
        }

        public void mouseClicked(MouseEvent event) {
            //如果双击，则移除当前正方形
            Point2D mousePoint = event.getPoint();
            current = find(mousePoint);
            if (current != null && event.getClickCount()>=2)
                remove(current);
        }
    }

    private class MouseMotionHandler implements MouseMotionListener {
        public void mouseMoved(MouseEvent event) {
            //如果在组件的其他部分，则将鼠标改为默认
            Point2D mousePoint = event.getPoint();
            if (find(mousePoint) == null)
                setCursor(Cursor.getDefaultCursor());
            else
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void mouseDragged(MouseEvent event) {
            if (current !=  null) {
                int x = event.getX();
                int y = event.getY();

                //拖动矩形
                current.setFrame(x-SIDE_ELEMENT/2,y-SIDE_ELEMENT/2,
                        SIDE_ELEMENT,SIDE_ELEMENT);

                repaint();
            }
        }
    }
}
