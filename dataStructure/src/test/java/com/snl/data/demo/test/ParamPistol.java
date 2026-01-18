package com.snl.data.demo.test;

import javax.swing.*;
import java.awt.*;

// 带完整可调参数的极简2D手枪 - Swing绘制版
public class ParamPistol extends JPanel {
    // ====================== 所有可修改的【手枪参数】，改这里的数值即可缩放/调整手枪 ======================
    // 手枪整体位置（中心点坐标，改这个直接移动手枪）
    private final int centerX = 300;
    private final int centerY = 200;
    // 手枪核心尺寸参数（所有尺寸都是相对值，按比例修改不变形）
    private final int gripW = 50;    // 握把宽度
    private final int gripH = 80;    // 握把高度
    private final int bodyW = 40;    // 枪身主体宽度
    private final int bodyH = 35;    // 枪身主体高度
    private final int barrelL = 100; // 枪管长度
    private final int barrelW = 20;  // 枪管宽度
    private final int triggerR = 10; // 扳机圆的半径
    private final int sightW = 8;    // 准星宽度
    private final int sightH = 8;    // 准星高度

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(46, 46, 46)); // 手枪颜色（深灰色，可改Color.BLACK/Color.GRAY）
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 抗锯齿，线条更顺滑

        // 1 绘制握把（带圆角，握把是手枪的重心，圆角更贴合真实手枪）
        int gripX = centerX - gripW/2;
        int gripY = centerY;
        g2d.fillRoundRect(gripX, gripY, gripW, gripH, 10, 10); // 最后两个值是圆角弧度

        // 2 绘制枪身主体（连接握把和枪管，矩形即可）
        int bodyX = centerX - bodyW/2;
        int bodyY = centerY - bodyH;
        g2d.fillRect(bodyX, bodyY, bodyW, bodyH);

        // 3 绘制枪管（手枪最前端，细长矩形，标准直线）
        int barrelX = centerX + bodyW/2;
        int barrelY = centerY - barrelW/2;
        g2d.fillRect(barrelX, barrelY, barrelL, barrelW);

        // 4 绘制扳机（握把上方的小椭圆，极简经典）
        int triggerX = centerX - triggerR/2;
        int triggerY = centerY + triggerR*2;
        g2d.fillOval(triggerX, triggerY, triggerR, triggerR);

        // 5 绘制准星（枪管最前端顶部，小方块，精准瞄准）
        int sightX = barrelX + barrelL - sightW/2;
        int sightY = barrelY - sightH/2;
        g2d.fillRect(sightX, sightY, sightW, sightH);

        // 可选：绘制手枪中心线（辅助对齐，调试用，删了不影响）
        g2d.setColor(Color.RED);
        g2d.drawLine(centerX-100, centerY, centerX+200, centerY);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("带参数的极简2D手枪 - Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ParamPistol());
        frame.setSize(650, 450); // 窗体大小
        frame.setLocationRelativeTo(null); // 居中显示
        frame.setVisible(true);
    }
}