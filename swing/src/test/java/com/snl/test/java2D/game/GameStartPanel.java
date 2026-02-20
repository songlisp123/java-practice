package com.snl.test.java2D.game;

import com.snl.test.java2D.SlideDataChangeListener;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.font.practice.SimpleCleanKeyBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

public class GameStartPanel extends DiKaErPlus implements SlideDataChangeListener {

    SimpleCleanKeyBoard keyBoard;
    Font font = new Font("隶书",Font.BOLD|Font.ITALIC,25);
    Font font02 = new Font("Chiller",Font.BOLD|Font.ITALIC,25);
    int padLeft,padRight,padTop,padBottom;

    int pictureW,pictureH;
    BufferedImage image;

    int pictureTop,pictureLeft;
    Shape[] shapes;
    Image[] images;

    int clickedIndex;
    boolean clicked,mouseButton01Dragging;

    int total;
    final  String[] strings = {
            "a", "2","3","4","5","6","7","8","9","10","j","q","k"
    };

    final  char[] chars = {'c','d','h','s'};

    int index;
    int start;
    GameSlide slide;
    GeneralPath[] paths;
    int rowLeft,rowRight,pathIndex = -1;
    float alpha;

    BufferedImage[] bufferedImages;
    private boolean showDetail = false;
    boolean hanging;
    Shape r;


    public GameStartPanel() throws HeadlessException {
        WIDTH = 900;
        HEIGHT = 900;
        drawAxis = false;
        padLeft = padRight = 50;
        padTop = 50;
        pictureW = pictureH = 150;
        pictureTop = 20;
        pictureLeft = 20;
        total = 56;
        start = 0;
        rowLeft = rowRight = 150;
        shapes = new Shape[3];
        images = new Image[total];
        paths = new GeneralPath[2];
        bufferedImages = new BufferedImage[2];
        keyBoard = new SimpleCleanKeyBoard(padLeft,padTop,400,400);
        image = createBufferedImage(pictureW,pictureH,BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        fillImages();
        fill();
        fillShapes();
        fillBuffers();
    }

    private void fillBuffers() {
        var b1 = createBufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_ARGB);
        fillBufferImaged(b1);
        bufferedImages[0] = b1;
        var b2 = createBufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_ARGB);
        bufferedImages[1] = b2;
    }
    
    private void fillBufferImaged(BufferedImage bi) {
        Graphics2D g2 = bi.createGraphics();
        g2.clearRect(0,0,bi.getWidth(),bi.getHeight());
        g2.setComposite(AlphaComposite.Src);
        g2.dispose();
    }

    private void fillImages() {
        String path = "images/cards/deck/FINAL/";
        for (String s :strings) {
            for (char c :chars)
            {
                String relativePath = path + s + c + ".png";
                index = fills(relativePath,index);
            }
        }
    }

    private void fill() {
        //获取中心
        double rw = 30,rh = 30;
        int w = c.getWidth();
        int h = c.getHeight();

        double leftX = rowLeft;
        double leftY = h * 8 / 9.0 - rh / 2.0;
        var s = new GeneralPath();
        s.moveTo(leftX,leftY + rh / 2.0);
        s.lineTo(leftX + rw,leftY);
        s.lineTo(leftX + rw,leftY + rh);
        s.closePath();
        paths[0] = s;
        //绘制第二个
        leftX = w - rowRight;
        s= new GeneralPath();
        s.moveTo(leftX,leftY + rh / 2.0);
        s.lineTo(leftX - rw,leftY);
        s.lineTo(leftX - rw,leftY + rh);
        s.closePath();
        paths[1] = s;

        leftX = (w - 400) / 2.0;
        slide = new GameSlide(leftX,leftY + 10,400,10, total-shapes.length);
        slide.addListener(this);
    }

    private void fillShapes() {
        int left = 20;
        int right = 20;
        int top = 50;
        int y = (int) (padTop + keyBoard.getTotalH() + top);
        int x  = padLeft;
        int rw = c.getWidth() / shapes.length - (left + right);
        for (int  i = 0;i<shapes.length;i++) {
            x = (i == 0) ? x : x + rw + left  ;
            Shape r = new Rectangle2D.Double(x,y,rw,rw);
            shapes[i] = r;
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        mouseButton01Dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_ESCAPE) && showDetail)
            showDetail = false;
        keyBoard.processInput(mouseInputEvent,keyBoardEvent);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D mousePoint = mouseInputEvent.getCurrentPoint();

        keyBoard.update(delta,mousePoint);

        slide.update(delta,clicked,mousePoint);
        slide.update(delta,mouseButton01Dragging,mousePoint);

        int c = checkPos(mousePoint,shapes);
        clickedIndex = (c != -1) ? c : clickedIndex;

        c = checkPos(mousePoint,paths);
        if (c!= -1)
        {
            pathIndex = c;
            alpha = 1.0F;
            if (pathIndex == 0)
                //点击左边
                start--;
            else
                //点击右边
                start++;
        }

        start = Math.max(0,Math.min(start,total - 3));
        slide.setValue(start);
        clickedIndex = Math.max(0,Math.min(clickedIndex,shapes.length-1));
        alpha = (float) Math.max(0,alpha-delta);
        clicked = false;
    }

    private <T extends Shape> int checkPos(Point2D mousePoint,T[] data) {
        if (data == null ||data.length == 0)
            return -1;
        for (int i = 0;i<data.length ;i++)
        {
            T s = data[i];
            if (s == null) {
                return -1;
            }
            if (s.contains(mousePoint) && clicked) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        super.draw(g);
        //TODO 待做
        if (!showDetail)
            drawPanel(g2);
        else
            //TODO
            ;
        g2.dispose();
    }

    private void drawPanel(Graphics2D g2) {
        keyBoard.draw(g2,Color.YELLOW);
        //绘制左边输入框
        //获取宽度
        g2.setColor(Color.WHITE);
        double totalW = keyBoard.getTotalW();
        double totalH = keyBoard.getTotalH();
        //绘制边框
        //绘制中心线
//        Line2D l = new Line2D.Double(c.getWidth() / 2.0,0,c.getWidth() / 2.0,c.getHeight());
//        g2.draw(l);R
//        Shape out = new Rectangle2D.Double(c.getWidth() / 2.0,padTop,
//                totalW,totalH);
//        g2.draw(out);
        double tx = c.getWidth() / 2.0;
        double ty = padTop;
        FontRenderContext frc = g2.getFontRenderContext();
        AffineTransform temp = g2.getTransform();

        g2.translate(tx,ty);
        g2.drawImage(images[start + clickedIndex],pictureLeft,pictureTop,null);
        TextLayout tl = new TextLayout("名称：美杜莎",font,frc);
        //获取中心线
        float ascent = tl.getAscent();
        float descent = tl.getDescent();
        float leading = tl.getLeading();
        float advance;
        float height = ascent + descent + leading;
        //获取左上角
        float leftX = (float) (pictureLeft + image.getWidth() + padRight);
        float leftY = padTop;
        tl.draw(g2,leftX,leftY);

        tl = new TextLayout("类型；毒药、折磨",font,frc);
        leftY += height;
        tl.draw(g2,leftX,leftY);

        height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        tl = new TextLayout("元素: 毒/土",font,frc);
        leftY += height;
        tl.draw(g2,leftX,leftY);
        tl = new TextLayout("生命:54",font,frc);
        leftY += height;
        tl.draw(g2,leftX,leftY);


        //绘制说明文本
        leftX = padLeft;
        leftY = pictureTop + image.getHeight() + padBottom;

        AttributedString as = new AttributedString("属性:");
        as.addAttribute(TextAttribute.FOREGROUND,Color.MAGENTA);
        as.addAttribute(TextAttribute.FONT,font.deriveFont(40.0F));

        tl = new TextLayout(as.getIterator(),frc);
        ascent = tl.getAscent();
        advance = tl.getAdvance();
        tl.draw(g2,padLeft,leftY + ascent);

        as = new AttributedString("哀怨挽歌");
        as.addAttribute(TextAttribute.FOREGROUND,Color.LIGHT_GRAY);
        as.addAttribute(TextAttribute.FONT,font.deriveFont(30.0F));
        tl = new TextLayout(as.getIterator(),frc);
        height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        leftY += ascent;
        tl.draw(g2,leftX + advance,leftY);

        as = new AttributedString("反击: 清除自身1个减益效果。每当自身清除4个减益效果，" +
                "触发一次攻击，对所有敌人造成1 x 4点水系 / 土系伤害，并对每个敌人施加4层折磨。");
        as.addAttribute(TextAttribute.FONT,font.deriveFont(28.0F));
        as.addAttribute(TextAttribute.FOREGROUND,new Color(179, 147, 139));
        as.addAttribute(TextAttribute.FOREGROUND,Color.YELLOW,0,2);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,8,9);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,21,22);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,32,34);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,43,44);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,45,46);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,48,55);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,49,52);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,66,67);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,68,70);
        LineBreakMeasurer lbm = new LineBreakMeasurer(as.getIterator(),frc);
        while (lbm.getPosition() < as.getIterator().getEndIndex()) {
            tl = lbm.nextLayout((float) totalW);
            tl.draw(g2,pictureLeft,leftY + height);
            leftY += tl.getDescent() + tl.getLeading() + tl.getAscent();
        }
        //非常重要
        g2.setTransform(temp);
        //绘制图像
        //绘制矩形
        int count = 0;
        for (int  i =start;i<start + 3;i++) {
            Shape s = shapes[count++%shapes.length];
            Image im = images[i];
            g2.draw(s);
            double w = s.getBounds().getWidth();
            double h = s.getBounds().getHeight();
            BufferedImage bi = new BufferedImage(
                    (int) (w * 0.65), (int) (h * 0.65),BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D biG = bi.createGraphics();
            biG.clearRect(0,0,bi.getWidth(),bi.getHeight());
            biG.setComposite(AlphaComposite.Src);
            biG.drawImage(im,0,0,bi.getWidth(),bi.getHeight(),null);
            biG.dispose();
            //确定中心
            float lx = (float) (s.getBounds().x + (w - bi.getWidth()) / 2.0F);
            float ly = s.getBounds().y + 20;
            g2.drawImage(bi, (int) lx, (int) ly,null);

            //绘制说明文本
            AttributedString ast = new AttributedString("美杜莎");
            ast.addAttribute(TextAttribute.FOREGROUND,Color.WHITE);
            ast.addAttribute(TextAttribute.FONT,font.deriveFont(25.0F));
            tl = new TextLayout(ast.getIterator(),frc);
            advance = tl.getAdvance();
            float sx = (float) (s.getBounds().x + (w - advance) / 2.0F);
            float sy = ly + bi.getHeight() ;
            tl.draw(g2,sx,sy + tl.getAscent());

            height = tl.getAscent() + tl.getLeading() + tl.getDescent();
            as = new AttributedString("属性:毒 x");
            as.addAttribute(TextAttribute.FOREGROUND,Color.YELLOW,0,2);
            as.addAttribute(TextAttribute.FONT,font.deriveFont(20F));
            Shape r = new Ellipse2D.Double(0,0,20,20);
            ShapeGraphicAttribute st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.HANGING_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,5,6);
            as.addAttribute(TextAttribute.FOREGROUND,Color.green,3,6);
            tl = new TextLayout(as.getIterator(),frc);
            sy += height;
            tl.draw(g2,sx,sy + tl.getAscent());

            height = tl.getAscent() + tl.getLeading() + tl.getDescent();
            as = new AttributedString("元素:土 x / 水 x");
            r = new Ellipse2D.Double(0,4,10,10);
            st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.HANGING_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,5,6);
            as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,0,2);
            as.addAttribute(TextAttribute.FOREGROUND,Color.orange,5,6);
            st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.HANGING_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,11,12);
            as.addAttribute(TextAttribute.FOREGROUND,Color.cyan,11,12);
            tl = new TextLayout(as.getIterator(),frc);
            sy += height;
            tl.draw(g2,sx,sy + tl.getAscent());
        }

        //绘制箭头
        drawSlide(g2);
        if (clickedIndex != -1) {
            Shape s = shapes[clickedIndex];
            g2.setColor(Color.green);
            g2.draw(s);
        }
        //绘制启动按钮
        drawStartButton(g2);
    }

    private void drawSlide(Graphics2D g2) {
        for (GeneralPath p : paths)
            g2.draw(p);
        //绘制滑动条
        slide.draw(g2);
        //绘制选择形状
        if (pathIndex != -1){
            GeneralPath path = paths[pathIndex];
            g2.setColor(new Color(1.0F,1.0F,0.0F,alpha));
            g2.fill(path);
        }
    }

    private void drawStartButton(Graphics2D g2) {
        //TODO
        int w = c.getWidth();
        int h = c.getHeight();
        int top = 60, right = 150;
        int y = h - top;
        int x = w - right;
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout("START",font02,frc);
        float advance = tl.getAdvance();
        float height = tl.getAscent() + tl.getDescent() + tl.getLeading();
        if (r != null && r.contains(mouseInputEvent.getCurrentPoint()))
            g2.setColor(Color.PINK);
        else
            g2.setColor(Color.green);
        tl.draw(g2,x,y + tl.getAscent());
        if (r == null)
            r = new RoundRectangle2D.Double(x,y,advance,height,3,3);

    }

    private int fills(String path,int index) {
        //获取缩放倍数
        ImageIcon icon = new ImageIcon(path);
        BufferedImage bi = new BufferedImage(pictureW,pictureH,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.clearRect(0,0,bi.getWidth(), bi.getHeight());
        g2.drawImage(icon.getImage(),0,0,bi.getWidth(),bi.getHeight(),null);
        g2.dispose();
        images[index++] = bi;
        return index;
    }

    public static void main(String[] args) {
        launchGame(new GameStartPanel());
    }

    @Override
    public void change(Object source, double oldValue, double newValue) {
        start = (int) newValue;
    }
}
