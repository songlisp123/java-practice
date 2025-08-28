package com.apress.bgn.char006.frame.grafics;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class DrawComponent extends JComponent {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    public void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g;
        //画一个矩形
        double leftX = 100;
        double topY = 100;
        double width = 200;
        double height = 200;

        var rect = new Rectangle2D.Double(leftX,topY,width,height);
        g2.draw(rect);

        //绘制封闭的椭圆
        var ellipse = new Ellipse2D.Double();
        ellipse.setFrame(rect);

        g2.draw(ellipse);

        //绘制一条对角线
        g2.draw(new Line2D.Double(leftX,topY,leftX+width,topY+height));
        g2.draw(new Line2D.Double(leftX+width,topY,leftX,topY+height));

        //绘制同心圆
        double radius = 150;
        double centerX = rect.getCenterX();
        double centerY  = rect.getCenterY();
        var circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(centerX,centerY,centerX+radius,centerY+radius);
        g2.draw(circle);

        g2.setPaint(Color.blue);
        g2.drawString("警告！",200,200);
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();


        var font = new Font("Bitstream Charter",Font.BOLD,50);
        g2.setFont(font);
        var message = "，你好世界！";
        g2.drawString(message,100,100);
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(message,context);

    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
