package com.snl.swing.game2026;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.sprite.Sequence;
import com.snl.swing.practice01.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.LinkedList;

public class BootPanel extends DiKaErPlus {

    private Timer timer;
    private Time time;
    private final  String content = "第一个项目在内存使用存在问题时是重要的，无论是因为我们在处理内存受限的设备（如控制台），还是因为我们希望存储大量变换（例如在动画数据中）。在任何情况下，表示大小的减少都意味着我们释放了可用于更多动画、更多动画帧（从而获得更平滑的结果）或其他游戏方面的内存。高效旋转点和向量虽然看似显而易见，但确实值得提及；并非所有表示方法都擅长于此。同样，对于某些表示方法，连接操作是不可行的";
    private String subContent = "";
    private LinkedList<Shape> shapes = new LinkedList<>();
    private int visualCount;
    private int my;
    private  FontRenderContext frc;
    Sequence sequence;
    BufferedImage[] bi,bi02;
    Button button;
    Sequence sequence02;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        drawAxis = false;
        time = new Time(150);
        timer = new Timer(16 , e -> timeCallBack());
        timer.setInitialDelay(1000);
        timer.start();
        bi = new BufferedImage[20];
        bi02 = new BufferedImage[19];
        fillImages();
        sequence = new Sequence(bi);
        sequence.setCellAdvanceInterval(80L);
        sequence02 = new Sequence(bi02);
        sequence02.setCellAdvanceInterval(80L);
        sequence02.start();
        sequence.start();
        button = new Button("跳过",c.getWidth() / 2,500);
        button.addActionListener(e->{
            visualCount = content.length() - 1;
            button.setBs("开始游戏");
        });
        Music.beep();
    }

    private void fillImages() {
        String s = "frame_0";
        for (int i = 0;i<bi.length;i++) {
//            String s = (i < 9) ? "frame_0"  : "frame_";
//            String s = (i < 10) ? "spin0"  : "spin";
            bi[i] = readImage("./images/" + s + (i + 20) + ".png");
        }

        for (int i = 0;i<bi02.length;i++) {
        s = (i < 10) ? "spin0"  : "spin";
            bi02[i] = readImage("./images/spin/" + s + i + ".gif");
        }
    }

    private BufferedImage readImage(String s) {
        try {
            InputStream in = new FileInputStream(s);
            in = new BufferedInputStream(in);
            BufferedImage bi = ImageIO.read(in);
            in.close();
            return bi;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void timeCallBack() {
        if (visualCount >= content.length())
        {
            timer.stop();
            timer = null;
            return;
        }
        if (time.isBeyond()) {
            visualCount++;
            subContent = content.substring(0, visualCount);
            time.setInitialed(time.getCurrent());
            fillShapes();
        }
        time.reCalculate();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        button.processInput(mouseInputEvent);
    }

    private void fillShapes() {
        LinkedList<Shape> newShapes =
                new LinkedList<>();
        my = c.getHeight() / 2;
        AttributedString as = new AttributedString(subContent);
        as.addAttribute(TextAttribute.FONT,new Font("隶书",Font.BOLD,16));
        AttributedCharacterIterator iterator = as.getIterator();
        LineBreakMeasurer lbm = new LineBreakMeasurer(iterator, frc);
        while (lbm.getPosition() < iterator.getEndIndex()) {
            TextLayout tl = lbm.nextLayout(c.getWidth() / 2);
//            float v = c.getWidth() - tl.getAdvance();
            AffineTransform aff = AffineTransform.getTranslateInstance( c.getWidth() / 4, my);
            newShapes.add(tl.getOutline(aff));
            my += (int) (tl.getAscent() + tl.getDescent() + tl.getLeading());
        }
        shapes = newShapes;
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if(frc == null)
            frc = g2.getFontRenderContext();
        g2.setColor(Color.WHITE);
//        g2.drawString(subContent,20,30);
        LinkedList<Shape> localShapes = shapes;
        BufferedImage cBi = sequence.getCurrentImage();
        double x = (c.getWidth() - cBi.getWidth()) / 2.0;
        g2.drawImage(cBi, (int) x,100,null);
        g2.drawLine(10,10,150,150);
        g2.drawImage(sequence02.getCurrentImage(), 50,100,100,100,null);
        for (Shape shape : localShapes)
           g2.fill(shape);
        button.draw(g2);
        g2.dispose();
    }

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (sequence.timeToAdvanceCell())
            sequence.advance();
        if (sequence02.timeToAdvanceCell())
            sequence02.advance();
    }

    public static void main(String[] args) {
        launchGame(new BootPanel());
    }
}
