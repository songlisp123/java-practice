package com.snl.test.java2D.font;

import com.snl.test.java2D.coords.DiKaErPlus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class TextMetricsExample extends DiKaErPlus implements ActionListener {

    Font f = new Font("隶书",Font.PLAIN,20);
    boolean kangjuchi;
    JTextField jt;
    List<String> strings = new ArrayList<>();
    int index;

    public TextMetricsExample() throws HeadlessException {
        drawAxis = false;
        JPanel cp = (JPanel) getContentPane();
        cp.setLayout(new BorderLayout());
        jt = new JTextField(15);
        jt.addActionListener(this);
        cp.add(jt,BorderLayout.PAGE_END);
        setContentPane(cp);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B))
            kangjuchi = !kangjuchi;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(f);
        g2.setColor(Color.WHITE);
        drawString(g2,strings);
//        handle(g2);
        g2.dispose();
    }

    private void drawString(Graphics2D g2, List<String> s) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        float ascent,advance;
        float h = 0;
        float leftX;
        int width = c.getWidth();
        int height = c.getHeight();
        float top,bottom;
        top = 20f;
        bottom = 40F;
        ArrayList<String> temp = new ArrayList<>(strings);
        for (int i = index;i<temp.size();i++) {
            String string = temp.get(i);
            TextLayout tl = new TextLayout(string, f, frc);
            //获取上行
            ascent = tl.getAscent();
            //获取全部长
            advance = tl.getAdvance();
            //获取高
            h = (i == 0) ? top + ascent : h + tl.getAscent() + tl.getDescent() + tl.getLeading();
            leftX = (width - advance) / 2.0F;
            tl.draw(g2,leftX,h);
            if (h > height - bottom) {
                strings.remove(index++);
            }
        }
    }

    private void handle(Graphics2D g2) {
        String s = "草长莺飞二月天";
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout(s, f, frc);
        //居中心文本
        int w = c.getWidth();
        int h = c.getHeight();
        float advance = tl.getAdvance();
        float ascent = tl.getAscent();
        float leftX = (w - advance) / 2.0F;
        double height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        tl.draw(g2, leftX, 20 +ascent);
        Rectangle2D r = new Rectangle2D.Double(leftX,20,advance,height);
        g2.draw(r);
        //绘制基线
        g2.setColor(Color.MAGENTA);
        Line2D l = new Line2D.Double(leftX,20 + ascent,leftX + advance,20+ascent);
        g2.draw(l);
        //绘制下极限
        g2.setColor(Color.green);
        l = new Line2D.Double(leftX,20 + ascent + tl.getDescent(),
                leftX + advance,20 +ascent + tl.getDescent());
        g2.draw(l);
        Font f1 = f.deriveFont(32.5F);
        tl = new TextLayout("我草你麻痹",f1,frc);
        advance = tl.getAdvance();
        leftX = (w - advance) / 2.0F;
        height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        ascent = tl.getAscent();
        float fy = (float) ((h -  height) / 2.0F);
        tl.draw(g2,leftX,fy + tl.getAscent());
        l = new Line2D.Double(leftX,fy + ascent,leftX + advance,fy+ascent);
        g2.draw(l);
        l = new Line2D.Double(leftX,fy + ascent + tl.getDescent(),
                leftX + advance,fy +ascent + tl.getDescent());
        g2.draw(l);
        l = new Line2D.Double(leftX,fy,leftX + advance,fy);
        g2.draw(l);
        l = new Line2D.Double(leftX,fy + ascent + tl.getDescent()+ tl.getLeading(),
                leftX + advance,fy + ascent + tl.getDescent()+ tl.getLeading());
        g2.draw(l);
        float[] baselineOffsets = tl.getBaselineOffsets();
        //绘制悬挂基线
        g2.setColor(Color.WHITE);
        l = new Line2D.Double(leftX,fy  - baselineOffsets[0],
                leftX + advance,fy  - baselineOffsets[0]);
        g2.draw(l);
        //绘制中心基线
        g2.setColor(Color.CYAN);
        l = new Line2D.Double(leftX,fy  - baselineOffsets[1],
                leftX + advance,fy  - baselineOffsets[1]);
        g2.draw(l);
        //绘制罗马基线
        g2.setColor(Color.MAGENTA);
        l = new Line2D.Double(leftX,fy - baselineOffsets[2],
                leftX + advance,fy  - baselineOffsets[2]);
        g2.draw(l);

        AffineTransform temp = g2.getTransform();
        g2.translate(30,h /  2);
        Font f2 = f.deriveFont(Font.ITALIC,15);
        tl = new TextLayout("当前警告:【WARNING】",f2,frc);
        height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        ascent = tl.getAscent();
        tl.draw(g2,0,ascent);

        tl = new TextLayout("温度过高:180℃",f2,frc);
        tl.draw(g2,0, (float) (height + tl.getAscent()));
        height += tl.getAscent() + tl.getDescent() + tl.getLeading();

        tl = new TextLayout("我草你麻痹",f2,frc);
        tl.draw(g2,0, (float) (height + tl.getAscent()));
        height += tl.getAscent() + tl.getDescent() + tl.getLeading();

        tl = new TextLayout("我好喜欢草碧",f2,frc);
        tl.draw(g2,0, (float) (height + tl.getAscent()));
        height += tl.getAscent() + tl.getDescent() + tl.getLeading();


        tl = new TextLayout("你喜欢吗？？",f2,frc);
        tl.draw(g2,0, (float) (height + tl.getAscent()));
        height += tl.getAscent() + tl.getDescent() + tl.getLeading();

        tl = new TextLayout("我也喜欢？",f2,frc);
        tl.draw(g2,0, (float) (height + tl.getAscent()));
        height += tl.getAscent() + tl.getDescent() + tl.getLeading();

    }

    public static void main(String[] args) {
        launchGame(new TextMetricsExample());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == jt) {
            strings.add(jt.getText());
        }
    }
}
