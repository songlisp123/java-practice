package com.apress.bgn.char006.frame.font;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class FontCompent extends JComponent {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        String message = "你好世界！";
        Font f = new Font("Serif", Font.BOLD, 36);
        g2.setFont(f);

        //测试消息的大小
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(message,context);
        //设置（x，y）为文本的左上角
        double x = (getWidth()-bounds.getWidth()) /2;
        double y = (getHeight()-bounds.getHeight()) /2;

        //在y上添加上升高度以到达基线
        double ascent = -bounds.getY();
        double baseY = y+ascent;

        //绘制信息
        g2.drawString(message,(int) x,(int) baseY);
        g2.setPaint(Color.LIGHT_GRAY);

        //绘制基线
        g2.draw(new Line2D.Double(x,baseY,x+bounds.getWidth(),baseY));

        //绘制包围矩形

        var rect = new Rectangle2D.Double(x,y,bounds.getWidth(),bounds.getHeight());

        g2.draw(rect);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
