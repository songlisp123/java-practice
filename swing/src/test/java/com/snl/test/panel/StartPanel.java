package com.snl.test.panel;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.LinkedHashSet;

public class StartPanel extends JPanel {

    private float mX,mY;
    private Font font;
    private final String aString = "1. 写一个自己的文本编辑器，可以存放图像，图标和音乐（后期添加视频）;快完成，我是废物"+
            "2. 创建一个音乐播放器，带有声形（我还是没有搞懂绘制机制）" +
              "2025年12月12日12:22:14 搞定了绘制机制，开始制造音乐播放器" +
              "(2025年12月16日11:14:29 已完成，但是很多逻辑不通顺的地方，需要优化)"+
            "3. 创建一个视频播放器(目前还不太会)"+
              "（2025年12月16日12:10:13 这个我是真不会，😭）"+
              "2026年1月3日17:21:16 新年伊始，我还是不会😭"+
              "2026年1月11日18:04:05 我真的不会😭"+
            "5. 创建一个简单的贪吃蛇小游戏（三颗心）"+
            "6. 创建一个雷霆战机似的游戏（四颗星）"+
            "7. 创建一个植物大战僵尸似的游戏（五颗星）";
    private long start;
    private Timer timer;
    private java.util.Set<Shape> shapes = new LinkedHashSet<>();
    private FontRenderContext frc;
    private int visualCount;
    private JButton button;

    public StartPanel() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        font = new Font("隶书",Font.PLAIN,50);
        mX = 40;
        mY = 40f;
        start = System.currentTimeMillis();
        button = new CustomButton("你好");
        button.addActionListener(System.out::println);
        add(button,BorderLayout.PAGE_END);
        timer = new Timer(16,e -> {
            update();
            repaint();
        });
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        frc = g2.getFontRenderContext();
        g2.setPaint(Color.MAGENTA);
        //古老的做法
//        int numGlyphs = gv.getNumGlyphs();
//        for (int i=0;i<numGlyphs;i++) {
//            Shape shape = gv.getGlyphOutline(i);
//            shape = af.createTransformedShape(shape);
//            g2.draw(shape);
//        }
        for (Shape shape : shapes)
            g2.draw(shape);
        g2.dispose();
    }

    private void  update() {
        long now = System.currentTimeMillis();
        if (now - start >= 80L && visualCount < aString.length()) {
            start = now;
            visualCount++;
//            Music.shoot();
            rebulid();
        }
    }

    private void rebulid() {
        shapes.clear();
        mY = 40f;
        String slice = aString.substring(0,visualCount).strip();
        AttributedString string = new AttributedString(slice);
        string.addAttribute(TextAttribute.FONT,font);
        var it = string.getIterator();
        LineBreakMeasurer lbm = new LineBreakMeasurer(it,frc);
        while (lbm.getPosition() < it.getEndIndex()) {
            TextLayout textLayout = lbm.nextLayout(getWidth() - 40);

            AffineTransform af = AffineTransform.getTranslateInstance(mX,mY);

            Shape shape = textLayout.getOutline(af);
            shapes.add(shape);

            mY += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }
}
