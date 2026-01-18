package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.text.AttributedString;

public class LineMetricsIllustrated extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //设置渲染提示
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        String s = "popular";
        Font font = new Font("Serif", Font.PLAIN, 72);
        g2.setFont(font);
        float x = 50, y = 150;

        //绘制文本基线
        var context = g2.getFontRenderContext();
        double width = font.getStringBounds(s, context).getWidth();
        var line1 = new Line2D.Double(x,y,x + width,y);
        g2.setPaint(Color.lightGray);
        g2.draw(line1);

        //绘制上升线
        LineMetrics lineMetrics = font.getLineMetrics(s, context);
        double ascent = lineMetrics.getAscent();
        var ascentLine = new Line2D.Double(x,y - ascent,x + width ,y - ascent);
        g2.draw(ascentLine);

        //绘制下降线
        double descent = lineMetrics.getDescent();
        var descentLine = new Line2D.Double(x,y + descent,x + width ,y + descent);
        g2.draw(descentLine);

        //绘制间距线
        double leading = lineMetrics.getLeading();
        var leadingLine = new Line2D.Double(x,y+descent + leading,x + width,y + descent  +leading);
        g2.draw(leadingLine);
        //获取基线
        int baselineIndex = lineMetrics.getBaselineIndex();//默认是罗马基线
        //获取基线的偏析
        AttributedString string = new AttributedString(s);
        string.addAttribute(TextAttribute.FOREGROUND,Color.BLUE);
        string.addAttribute(TextAttribute.FONT,font);
        string.addAttribute(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON,2,4);
        g2.drawString(string.getIterator(),x,y);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new LineMetricsIllustrated();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LineMetricsIllustrated::createUi);
    }
}
