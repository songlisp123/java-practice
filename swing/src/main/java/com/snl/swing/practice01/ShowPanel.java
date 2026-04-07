package com.snl.swing.practice01;

import com.snl.swing.practice01.button.CustomButton;
import com.snl.swing.practice01.state.InputState;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.LinkedHashSet;

public class ShowPanel extends JPanel {
    private float mX,mY;
    private Font font;
    private final String aString = "1. 写一个自己的文本编辑器，可以存放图像，图标和音乐（后期添加视频）;快完成，我是废物"+
            "望着测验魔石碑上面闪亮得甚至有些刺眼的五个大字，少年面无表情，唇角有着一抹自嘲，紧握的手掌，因为大力，而导致略微尖锐的指甲深深的刺进了掌心之中，带来一阵阵钻心的疼痛……"+
            "斗破苍穹，尽请期待……";
    private long start;
    private Timer timer;
    private java.util.Set<Shape> shapes = new LinkedHashSet<>();
    private FontRenderContext frc;
    private int visualCount;
    private JButton button;
    private InputState state;
    private JFrame frame;

    public ShowPanel(InputState state, JFrame frame) {
        this.state = state;
        this.frame = frame;
        init();
    }

    public ShowPanel() {
        init();
    }

    private void init() {
        Music.back01();
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        button = new CustomButton("开始游戏>>");
        button.addActionListener(e -> {
            BootPanel bootPanel = new BootPanel(state,frame);
            frame.remove(this);
            frame.getContentPane().add(bootPanel);
            timer = null;
            frame.revalidate();
            frame.repaint();
            setVisible(false);
        });
        add(button,BorderLayout.PAGE_END);
        button.setEnabled(false);
        font = new Font("隶书",Font.PLAIN,25);
        mX = 40;
        start = System.currentTimeMillis();
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
        float radius = 200.F;
        float[] fraction = {0.0f,0.5f,1.0f};
        Color[] colors = {
                Color.LIGHT_GRAY,Color.ORANGE,Color.WHITE
        };
        RadialGradientPaint paint = new RadialGradientPaint(mX,mY,radius,fraction,colors,
                MultipleGradientPaint.CycleMethod.REPEAT);
        g2.setPaint(paint);
        for (Shape shape : shapes)
            g2.fill(shape);
        g2.dispose();
    }

    private void  update() {
        long now = System.currentTimeMillis();
        if (now - start >= 80L && visualCount < aString.length()) {
            start = now;
            visualCount++;
            if (visualCount == aString.length())
                button.setEnabled(true);
            rebulid();
        }
    }

    private void rebulid() {
        shapes.clear(); //清空
        mY = 40f; //初始化字形渲染位置
        String slice = aString.substring(0,visualCount).strip(); //字符串切片
        AttributedString string = new AttributedString(slice);
        string.addAttribute(TextAttribute.FONT,font);
        var it = string.getIterator(); //获取迭代器，非常重要的一步
        LineBreakMeasurer lbm = new LineBreakMeasurer(it,frc); //获取文本布局
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
        return new Dimension(800,600);
    }
}
