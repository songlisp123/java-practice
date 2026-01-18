package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

public class TextGravity extends JPanel {

    private final double GRAVITY = 9.96;
    private Timer timer;
    private String s;
    private Font font;
    private float mX,mY,oY;
    private int number;
    private long start;
    private final java.util.List<Shape> shapes = new ArrayList<>();

    private AttributedCharacterIterator iterator;
    private AffineTransform af;
    private Graphics2D g2;

    public TextGravity() {
        setBackground(Color.black);
        s = """
           斗之力，三段！”
           望着测验魔石碑上面闪亮得甚至有些刺眼的五个大字，少年面无表情，唇角有着一抹自嘲，紧握的手掌，因为大力，而导致略微尖锐的指甲深深的刺进了掌心之中，带来一阵阵钻心的疼痛……
           “萧炎，斗之力，三段！级别：低级！”测验魔石碑之旁，一位中年男子，看了一眼碑上所显示出来的信息，语气漠然的将之公布了出来……
           年男子话刚刚脱口，便是不出意外的在人头汹涌的广场上带起了一阵嘲讽的骚动。
           “三段？嘿嘿，果然不出我所料，这个‘天才’这一年又是在原地踏步！”
           “哎，这废物真是把家族的脸都给丢光了。”
           “要不是族长是他的父亲，这种废物，早就被驱赶出家族，任其自生自灭了，哪还有机会待在家族中白吃白喝。”
           “唉，昔年那名闻乌坦城的天才少年，如今怎么落魄成这般模样了啊？”
           “谁知道呢，或许做了什么亏心事，惹得神灵降怒了吧……”
           """;
        font = new Font("隶书",Font.PLAIN,30);
        AttributedString string = new AttributedString(s);
        string.addAttribute(TextAttribute.FONT,font);
        iterator = string.getIterator();
        mX = 40;
        mY = 40;
        oY = mY;
        number = 0;
        start = System.currentTimeMillis();
        timer = new Timer(16,e-> update());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.MAGENTA);
        //创建放射矩形
        for (Shape s : shapes)
            g2.draw(s);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new TextGravity();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void update() {
        float width = getWidth();
        java.util.List<String> strings = new ArrayList<>();
        java.util.List<Float> v = new ArrayList<>();
        FontRenderContext frc = g2.getFontRenderContext();
        LineBreakMeasurer lbm = new LineBreakMeasurer(iterator,frc);
        while (lbm.getPosition() < iterator.getEndIndex()) {
            //获取当前行的开始位置
            int start = lbm.getPosition();
            TextLayout textLayout = lbm.nextLayout(width);
            int end = lbm.getPosition();
            var slice = s.substring(start, end).trim();
            //获取渲染位置
            strings.add(slice);
            v.add(mY);
            mY += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextGravity::createUi);
    }
}
