package com.snl.swing.TwoDimensionDemo;

import com.snl.swing.TwoDimensionDemo.layerUi.SpotLightLayerUiDemo;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.random.RandomGenerator;

public class ComBoxDemo extends JPanel implements ListDataListener , MouseListener {

    protected JComboBox<ShapeMaker> comboBox;
    protected ComboBoxModel<ShapeMaker> model;
    protected ShapeMaker maker;
    protected Point2D[] points;
    protected final RandomGenerator generator =
            RandomGenerator.getDefault();
    protected final int WEIGHT = 200;
    protected final int HEIGHT = 200;
    protected int current;


    public ComBoxDemo() {
        super(new BorderLayout());
        init();

    }

    private void init() {
        model = new CustomComboBoxModel();
        model.addListDataListener(this);
        comboBox = new JComboBox<>(model);
        setMaker(new LineMaker());
        addMouseListener(this);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (current == -1) return;
                points[current] = e.getPoint();
                repaint();
            }
        });
        current = -1;
        add(comboBox,BorderLayout.PAGE_START);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        //空方法体
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        //空方法体
    }

    public void setMaker(ShapeMaker maker) {
        this.maker = maker;
        int pointCount = maker.getPointCount();
        points = new Point2D[pointCount];
        for (int i=0;i<pointCount;i++) {
            double x = generator.nextDouble() * WEIGHT;
            double y = generator.nextDouble() * HEIGHT;
            points[i] = new Point2D.Double(x,y);
        }
        repaint();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        //只触发这一个事件
        if (e.getType() == ListDataEvent.CONTENTS_CHANGED) {
            var source = (CustomComboBoxModel)e.getSource();
            var item = source.getSelectedItem();
            if (item != null) {
                setMaker((ShapeMaker) item);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WEIGHT,HEIGHT);
    }

    private static void CreateUi() {
        JFrame frame = new JFrame("测试");
        ComBoxDemo demo = new ComBoxDemo();
        var uiDemo = new SpotLightLayerUiDemo();
        var layer = new JLayer<>(demo,uiDemo);
        frame.add(layer);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(ComBoxDemo::CreateUi);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //先绘制点
        for (Point2D point : points) {
            double x = point.getX() - 5;
            double y = point.getY() - 5;
            g2.fill(new Rectangle2D.Double(x, y, 10, 10));
        }
        Shape shape = maker.setShape(points);
        g2.setColor(Color.BLACK);
        g2.draw(shape);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("鼠标点击……");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("鼠标按下……");
        Point point = e.getPoint();
        for (int i=0;i<points.length;i++) {
            double x = points[i].getX() - 5;
            double y = points[i].getY() - 5;
            var r = new Rectangle2D.Double(x, y, 10, 10);
            if (r.contains(point)) {
                current = i;
                return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("鼠标释放……");
        current = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("进入界面……");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("退出界面……");
    }
}
