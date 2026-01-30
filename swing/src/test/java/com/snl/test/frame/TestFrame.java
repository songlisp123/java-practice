package com.snl.test.frame;

import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;

public class TestFrame extends JFrame {

    FrameV1 v1;
    FrameV2 v2;

    public TestFrame(String title) throws HeadlessException {
        super(title);
    }

     void createAndShowUi() {
//        v1 = new FrameV1();
         v2 = new FrameV2();
        GamePanel gamePanel = new GamePanel();
        this.getContentPane().add(gamePanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utils.centerContainer(this);
        this.pack();
//        v1.init();
        this.setVisible(true);
    }


    class GamePanel extends JPanel {

        public GamePanel() {
            setBackground(Color.black);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onPaint(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400,300);
        }
    }

    private void onPaint(Graphics g) {
//        v1.calculate();
        v2.calculateFrameRate();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.cyan);
        g2.drawString(v2.getFrameRate(),30,30);
        g2.dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new TestFrame("测试").createAndShowUi();
        });
    }
}
