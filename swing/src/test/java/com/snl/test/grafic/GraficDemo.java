package com.snl.test.grafic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class GraficDemo extends JFrame  {

    protected int xPos;
    protected int yPos;
    protected final int WEIGHT = 40;
    protected final List<Rectangle> rectangles = new ArrayList<>();
    protected final List<Ellipse2D> ellipses = new ArrayList<>();
    protected Rectangle current;
    protected Ellipse2D ellipse2D;
    protected enum Model {
        REC,CIRCLE
    }

    protected Model drawModel = Model.REC;

    public GraficDemo() throws HeadlessException {
        super("测试");
        init();
    }

    public GraficDemo(String title) throws HeadlessException {
        super(title);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new MyPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class MyPanel extends JPanel {

        protected MouseListener listener;

        public MyPanel() {
            setBorder(BorderFactory.createLineBorder(Color.black,2));
            listener = new MouseListenerImplements();
            addMouseListener(listener);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();

                    for (Rectangle rectangle : rectangles) {
                        if (rectangle.contains(x,y)) {
                            rectangle.setLocation((int) (x - rectangle.getWidth() /2), (int) (y - rectangle.getHeight() / 2));
                            repaint();
                        }

                    }

                    for (Ellipse2D ex : ellipses) {
                        if (ex.contains(x,y)) {
                            ex.setFrame(x-ex.getWidth()/2,y- ex.getHeight() / 2,ex.getWidth(),ex.getHeight());
                            repaint();
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            var g2 = (Graphics2D) g.create();
            super.paintComponent(g2);
            g2.setColor(Color.red);
            for (Rectangle rectangle : rectangles) {
                g2.fill(rectangle);
            }
            g2.setColor(Color.green);
            for (Ellipse2D e : ellipses) {
                g2.fill(e);
            }
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200,50);
        }
    }

    class MouseListenerImplements implements MouseListener {

        public MouseListenerImplements() {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("按键点击……");
            draw(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("鼠标按压……");
            draw(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("鼠标是放……");
            draw(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("鼠标进入区域……");
            draw(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("鼠标退出区域……");
            draw(e);
        }

        private void draw(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();
            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                System.out.println("开始绘制矩形……");
                if (drawModel == Model.REC) {
                    current = new Rectangle(xPos, yPos, WEIGHT, WEIGHT);
                    rectangles.add(current);
                    drawModel = Model.CIRCLE;
                }else {
                    ellipse2D = new Ellipse2D.Float(xPos,yPos,WEIGHT,WEIGHT);
                    ellipses.add(ellipse2D);
                    drawModel = Model.REC;
                }
                repaint();
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GraficDemo::new);
    }
}
