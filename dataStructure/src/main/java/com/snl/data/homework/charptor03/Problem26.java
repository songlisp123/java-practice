package com.snl.data.homework.charptor03;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;
import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Problem26 extends JPanel implements MouseMotionListener , MouseListener {

    private JButton runButton;
    private JButton showShapeButton;
    private final int WEIGHT = 20;
    private final int HEIGHT = 20;
    private final double GRAVITY = 9.8;
    private Timer timer;
    private Shape currentrShape;
    /**
     * 绘制图形样式：
     * 1-默认原型
     * 2-方形
     * ……其他
     */
    private int drawShape;

    private final List<Shape> shapes = new ArrayList<>();

    public Problem26() {
        initDate();
    }

    public Problem26(LayoutManager layout) {
        super(layout);
        initDate();
    }

    private void initDate() {

        timer = new Timer(12,new calShape());
        timer.start();

        setBackground(Color.black);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        runButton = new CustomButton("运行");
        runButton.setToolTipText("运行此程序将");

        showShapeButton = new CustomButton("绘制图形");
        showShapeButton.setToolTipText("更改绘制图形");
        alignSpace();
    }

    private void repaint(ActionEvent event) {
        repaint();
    }

    private void alignSpace() {
        //分配空间
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.insets = new Insets(10,5,0,5);
        c.fill = GridBagConstraints.NONE;
        add(showShapeButton,c);

        c.gridx = 1;
        c.weightx = 0.0f;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(runButton,c);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.CYAN);
        g2.drawString("这是一个简单的绘制机制，用来模拟简单图形的绘制",20,20);

        if (!shapes.isEmpty()) {
            g2.setColor(Color.green);
            for (Shape shape : shapes)
            {
                g2.draw(shape);
            }
        }

        if (currentrShape != null){
            double centerX = currentrShape.getBounds().getCenterX();
            double centerY = currentrShape.getBounds().getCenterY();
            Point2D center = new Point2D.Double(centerX,centerY);
            float radius = 20;
            float[] dist = {0.0f,1.0f};
            Color[] colors = {
                    Color.CYAN,Color.BLACK
            };
            RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,0.6f
            ));
            g2.setPaint(paint);
            g2.fill(currentrShape);
            g2.draw(currentrShape);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,400);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        //TODO 暂时不实现
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //TODO 移动时间
        Point point = e.getPoint();
        for (Shape shape : shapes) {
            if (shape.contains(point)) {
                currentrShape = shape;
                repaint(shape.getBounds());
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }else {
                currentrShape = null;
                setCursor(null);
                repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickCount = e.getClickCount();
        if (clickCount > 1 &&
                currentrShape != null)
        {
            shapes.remove(currentrShape);
            currentrShape = null;
            repaint();
            return;
        } else if (clickCount == 1 && currentrShape == null) {
            Point point = e.getPoint();
            int x = e.getX();
            int y = e.getY();
            var shaper = new Ellipse2D.Double(x - WEIGHT / 2, y - HEIGHT / 2, WEIGHT, HEIGHT);
            shapes.add(shaper);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //没有具体的实现
        System.out.println("鼠标按下");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //没有具体的实现
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //没有具体的实现
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //没有具体的实现
    }

    class calShape implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Shape shape : shapes) {
                Rectangle bounds = shape.getBounds();
                double x = bounds.getX();
                double y = bounds.getY();
                y += GRAVITY * 0.25;
                if (y+HEIGHT >= getHeight()) {
                    y = -y ;
                }
                var rec = new Rectangle2D.Double(x, y, WEIGHT, HEIGHT);
                if (shape instanceof Rectangle2D) {
                    ((Rectangle2D) shape).setFrame(rec);
                }
                else {
                    ((Ellipse2D) shape).setFrame(rec);
                }
            }
            repaint();
        }
    }


}
