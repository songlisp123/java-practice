package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class ParagraphLayout extends JPanel {


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //设置渲染提示
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        String s = """
                  1. 写一个自己的文本编辑器，可以存放图像，图标和音乐（后期添加视频）;快完成，我是废物
                  2. 创建一个音乐播放器，带有声形（我还是没有搞懂绘制机制）
                    2025年12月12日12:22:14 搞定了绘制机制，开始制造音乐播放器
                    (2025年12月16日11:14:29 已完成，但是很多逻辑不通顺的地方，需要优化)
                  3. 创建一个视频播放器(目前还不太会)
                    （2025年12月16日12:10:13 这个我是真不会，😭）
                    2026年1月3日17:21:16 新年伊始，我还是不会😭
                    2026年1月11日18:04:05 我真的不会😭
                  4. 创建一个自己的文件管理器，带有各种文件的预览（已经有模版，但是需要修整）
                    （2025年12月16日11:15:17 需要学习文件浏览器模型，哎，又是一个重磅头戏！）
                  5. 创建一个简单的贪吃蛇小游戏（三颗心）
                  6. 创建一个雷霆战机似的游戏（四颗星）
                  7. 创建一个植物大战僵尸似的游戏（五颗星）
                """;
        Font font= new Font("隶书",Font.BOLD,20);
        AttributedString as = new AttributedString(s);
        as.addAttribute(TextAttribute.FONT,font);
        AttributedCharacterIterator iterator = as.getIterator();

        //获取文本哦渲染上下文类
        FontRenderContext fontRenderContext =
                g2.getFontRenderContext();
        //获取分割线类
        LineBreakMeasurer lineBreakMeasurer =
                new LineBreakMeasurer(iterator, fontRenderContext);
        //获取边距
        Insets insets = getInsets();
        float warppingWidth = getSize().width - insets.left - insets.right;
        float x = insets.left;
        float y = insets.top;

        while (lineBreakMeasurer.getPosition() < iterator.getEndIndex()) {
            int position = lineBreakMeasurer.getPosition();
            System.out.println("position = " + position);
            TextLayout layout = lineBreakMeasurer.nextLayout(warppingWidth);
            y += layout.getAscent();
            layout.draw(g2,x,y);
            y += layout.getDescent() + layout.getLeading();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        var p = new ParagraphLayout();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParagraphLayout::createUi);
    }
}
