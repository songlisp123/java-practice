package com.snl.test.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class GunDrawingDemo extends JPanel {

    static final double AXIS_Y = 60;   // 枪管中心轴
    static final double SLIDE_TOP = 20;
    static final double SLIDE_BOTTOM = 90;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.translate(100, 100);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        drawSlide(g2);
        drawFrame(g2);
        drawGrip(g2);
        drawTriggerGuard(g2);
        drawTrigger(g2);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Java2D Gun Outline");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.add(new GunDrawingDemo());
        f.setVisible(true);
    }

    private void drawSlide(Graphics2D g2) {
        Path2D p = new Path2D.Double();

        // 左后端
        p.moveTo(20, SLIDE_TOP);
        p.lineTo(420, SLIDE_TOP);

        // 前端斜切 + 圆角
        p.lineTo(445, 35);
        p.quadTo(455, AXIS_Y, 445, SLIDE_BOTTOM - 5);

        p.lineTo(60, SLIDE_BOTTOM);

        // 后端下圆角
        p.quadTo(20, SLIDE_BOTTOM - 5, 20, SLIDE_BOTTOM - 30);
        p.closePath();

        g2.draw(p);
    }


    private void drawGrip(Graphics2D g2) {
        Path2D p = new Path2D.Double();

        // 起点：护圈后下
        p.moveTo(210, 245);

        // 握把前缘（轻微曲率）
        p.lineTo(230, 290);
        p.lineTo(235, 330);

        // 握把底
        p.lineTo(225, 380);

        // 握把背部：单一连续三次曲线（重点）
        p.curveTo(
                190, 365,   // 控制点1（腰部）
                155, 300,   // 控制点2（虎口）
                170, 215    // 回到机匣
        );

        p.closePath();
        g2.draw(p);
    }


    private void drawTriggerGuard(Graphics2D g2) {
        Path2D p = new Path2D.Double();

        p.moveTo(210, 110);
        p.lineTo(250, 110);

        p.quadTo(290, 120, 290, 160);
        p.quadTo(290, 220, 240, 230);
        p.quadTo(190, 240, 180, 200);
        p.quadTo(170, 160, 210, 110);

        g2.draw(p);
    }

    private void drawTrigger(Graphics2D g2) {
        Path2D p = new Path2D.Double();

        p.moveTo(235, 135);

        p.quadTo(215, 170, 235, 210);
        p.quadTo(250, 225, 260, 210);
        p.quadTo(250, 170, 240, 145);

        g2.draw(p);
    }

    private void drawFrame(Graphics2D g2) {
        Path2D p = new Path2D.Double();

        // 从滑套下缘开始
        p.moveTo(180, SLIDE_BOTTOM);

        // 扳机前上方连接
        p.lineTo(235, 110);

        // 护圈前壁
        p.lineTo(275, 115);

        // 护圈前下圆弧
        p.quadTo(300, 150, 290, 200);

        // 护圈底部
        p.quadTo(270, 245, 225, 245);

        // 护圈后下
        p.quadTo(185, 240, 185, 200);

        // 回到机匣
        p.quadTo(185, 150, 210, 120);

        p.closePath();
        g2.draw(p);
    }



}
