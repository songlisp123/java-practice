package com.apress.bgn.char006.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MyComponent extends JComponent {
    public static final int MESSAGE_X = 75;
    public static final int MESSAGE_Y = 100;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    public MyComponent() {
        super();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        var r2 = new Rectangle2D.Double(50,50,100,50);
        var e1 = new Ellipse2D.Double(100,100,100,50);
        var e2 = new Ellipse2D.Double();
        var p1 = new Point2D.Double();
        var p2 = new Point2D.Double(12,23);
        var l1 = new Line2D.Double(p1,p2);
        System.out.println(p1);
        g2.draw(r2);
        g2.draw(e1);
        g2.setPaint(Color.CYAN);
        g2.draw(l1);
        g2.fill(r2);
        g2.drawString("不是合理咯！",MESSAGE_X,MESSAGE_Y);
        var afont = new Font("SansSerif",Font.BOLD,20);
        g2.setPaint(Color.red);
        g2.setFont(afont);
        String s = "尼玛的比";
        g2.drawString(s,50,50);

        //获取字体的大小
        FontRenderContext f  = g2.getFontRenderContext();
        Rectangle2D rec = afont.getStringBounds(s,f);

        //设置组件相对于内容面板的位置
        this.setLocation(50,50);

    }


}
