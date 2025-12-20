package com.snl.test.grafic;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GraficDemo02 extends JPanel {


    public GraficDemo02() {
        super(new BorderLayout());
        init();
    }

    public GraficDemo02(LayoutManager layout) {
        super(layout);
        init();
    }

    private void init() {
        add(new MyCustomProcessBar(0,150),BorderLayout.PAGE_END);
    }

    class MyCustomProcessBar extends JProgressBar {
        public MyCustomProcessBar(int min, int max) {
            super(min, max);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.red);
            g2.fill(getBounds());

            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            super.paintBorder(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.green);
            g2.draw(new Rectangle2D.Double(0,0,getWidth(),getHeight()));
            g2.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    private static void CreateUi() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new GraficDemo02());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GraficDemo02::CreateUi);
    }
}
