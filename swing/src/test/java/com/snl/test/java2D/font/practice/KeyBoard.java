package com.snl.test.java2D.font.practice;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class KeyBoard {

    //布局参数
    protected StringBuilder builder;
    protected int rw,rh;
    int padTop,padLeft,padRight,padBottom;
    int vgap;

    KeyBoardSelectionModel selectionModel;

    protected Font font = new Font("Chiller",Font.PLAIN,15);
    protected Font font02 = new Font("GENISO",Font.PLAIN,25);
    protected Font font03 = new Font("SHOWCARD GOTHIC",Font.PLAIN,15);

    //选择模式
    Shape[] shapes;
    String[] keyBoardStrings;
    int clickedIndex = -1;
    int maskIndex = -1;
    int total; //键盘数组长度
    int size; //这个字段目前用不上
    int charIndex;
    //步长参数：每行多少个元素
    int strideLine;
    int maxLength,minLength;

    public KeyBoard() {
        builder = new StringBuilder();
        total = getTotal();
        shapes = new Shape[total]; // TODO
        keyBoardStrings = new String[total];
        fillStringArray();
    }

    public void initial(Component c) {
        strideLine = 10;
        padTop = padBottom= 0;
        padLeft = padRight = 10;
        vgap = 20;
        maxLength = 15;
        minLength = 6;
        adjustedWAndH(c);
        fillShapes(c);
    }

    public void addClickedIndex(int delta) {
        clickedIndex += delta;
        clickedIndex = Math.max(0,Math.min(clickedIndex,total-1));
    }

    public void addCharIndex(int delta) {
        charIndex += delta;
        charIndex = Math.max(0,Math.min(charIndex,size-1));
    }

    private void fillShapes(Component c) {
        int w = c.getWidth();
        int h = c.getHeight();

        int x = padLeft;
        int yy = h / 2;
        Rectangle2D r;
        for (int i = 0;i<shapes.length;i++) {
            x = (i % strideLine == 0)? padLeft : x + rw + padLeft + padRight;
            if (i == 0)
                yy = yy + padBottom + vgap;
            else
                yy = (i % strideLine == 0) ?  yy+rh+vgap : yy;
            r = new Rectangle2D.Double(x,yy,rw,rh);
            shapes[i] = r;
        }
    }

    private void adjustedWAndH(Component c) {
        int w = c.getWidth();
        int h = c.getHeight()/ 2;
        rw = w / strideLine - (padLeft + padRight);
        rh = h / 16 - (padTop + padBottom);
    }

    private void fillStringArray() {
        int i;
        for (i = '!';i<='z';i++) {
            String letter = ""+(char) i;
            keyBoardStrings[i - '!'] = letter;
        }
    }

    public void checkMaskR(Point2D mouseP) {
        for (int i = 0;i<shapes.length;i++) {
            var s = shapes[i];
            if (s == null) {
                maskIndex = -1;
                break;
            }
            if (s.contains(mouseP)) {
                maskIndex = i;
                break;
            }else {
                maskIndex = -1;
            }
        }
    }

    public void deleteChar() {
        builder.deleteCharAt(charIndex);
        size--;
        charIndex--;
    }

    public void addString() {
        boolean inside = size <= maxLength;
        if (inside) {
            builder.append(
                    keyBoardStrings[clickedIndex]
            );
            charIndex = size++;
        }
    }

    public int getTotal() {
        return 'z' - '!' + 1;
    }

    public int getMaskIndex() {
        return maskIndex;
    }

    public void setMaskIndex(int maskIndex) {
        this.maskIndex = maskIndex;
    }

    public int getClickedIndex() {
        return clickedIndex;
    }

    public void setClickedIndex(int clickedIndex) {
        this.clickedIndex = clickedIndex;
    }

    public int getStrideLine() {
        return strideLine;
    }

    public int getSize() {
        return size;
    }

    //重点
    public void draw(Graphics2D g2,Component c) {
        int w = c.getWidth();
        int h = c.getHeight();

        int x = padLeft;
        int yy = h / 2;
        int middleH = yy;
        int middleX = w / 2;
        Rectangle2D r;
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl;
        for (int i = 0;i<shapes.length;i++) {
            x = (i % strideLine == 0)? padLeft : x + rw + padLeft + padRight;
            if (i == 0)
                yy = yy + padBottom + vgap;
            else
                yy = (i % strideLine == 0) ?  yy+rh+vgap : yy;
            r = new Rectangle2D.Double(x,yy,rw,rh);
            shapes[i] = r;
//            g2.draw(r);
            Font f = font.deriveFont((float) rh);
            tl = new TextLayout(keyBoardStrings[i], f,frc);
            float ascent = tl.getAscent();
            float advance = tl.getAdvance();
            float cx = x + (rw - advance) / 2.0F;
            tl.draw(g2, cx, (float) yy + ascent);
        }

        //绘制字母
        r = new Rectangle2D.Double(0,h/2.0,w,yy - h / 2.0F + rh + padBottom);
        g2.draw(r);

        g2.setFont(new Font("隶书",Font.PLAIN,15));
        tl = new TextLayout("虚拟键盘",g2.getFont(),frc);
        //获取基线上升位置
        float ascent = tl.getAscent();
        //获取前进距离
        float advance = tl.getAdvance();
        //计算中心小
        float centerX = middleX - advance / 2.0F;
        tl.draw(g2,centerX,middleH + ascent + 10.0f);

        if (maskIndex != -1)
        {
            Shape s = shapes[maskIndex];
            g2.setColor(new Color(0.5F,0.5F,0.5F,0.5F));
            g2.fill(s);
        }

        if (clickedIndex != -1)
        {
            Shape s = shapes[clickedIndex];
            g2.setColor(Color.CYAN);
            g2.draw(s);
        }

        //绘制输入字体
        //注意对于不同的字体：可能不会显示中文效果
        g2.setColor(Color.green);
        Font f1 = font02.deriveFont(25.0F);
        tl = new TextLayout("PLEASE ENTER YOUR NAME:",f1,frc);
        advance = tl.getAdvance();
        ascent = tl.getAscent();
        float cy = h / 4.0F - ascent;
        float cx = (w - advance) / 2.0F;
        tl.draw(g2,cx,cy);
        if (builder.length() <= 0)
            return;
        //昵称名字
        g2.setColor(Color.WHITE);
        tl = new TextLayout(builder.toString(),f1,frc);
        //中心位置
        advance = tl.getAdvance();
        /*
        这个判断是根据屏幕大小
         */
//        if (advance >= w - (padLeft + padRight))
//            showWarningMessage = true;
//        else
//            showWarningMessage = false;
        ascent = tl.getAscent();
        cy = h / 4.0F;
        cx = (w - advance) / 2.0F;
        cy += ascent;
        tl.draw(g2,cx,cy);
        //光标
        Shape caretShape = tl.getCaretShape(TextHitInfo.trailing(charIndex));
        AffineTransform af = AffineTransform.getTranslateInstance(cx,cy);
        caretShape = af.createTransformedShape(caretShape);
        g2.draw(caretShape);

        //下划线
        Line2D l = new Line2D.Double(cx,cy + 2.0f,cx + advance,cy+2.0f);
        g2.draw(l);

        //报警信息
        g2.setColor(Color.red);
        Font f2 = font03.deriveFont(30F);
        if (isBeyond()) {
            tl = new TextLayout("NOT REQUIRED . REQUEST FOR LENGTH IN [%d-%d]".formatted(minLength,maxLength),f2,frc);
            advance = tl.getAdvance();
            ascent = tl.getAscent();
            cx = (w - advance) / 2.0F;
            cy += 2 *  ascent;
            tl.draw(g2,cx,cy);
        }
    }

    public void  reset() {
        builder = new StringBuilder();
        charIndex = size = 0 ;
    }

    public boolean isBeyond() {
        return size < minLength || size > maxLength;
    }
}
