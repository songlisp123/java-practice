package com.snl.test.java2D.font.practice;

import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SimpleCleanKeyBoard {

    //包围矩形
    protected double leftX,leftY;
    protected double totalW,totalH;

    protected int rw,rh;
    int padTop,padLeft,padRight,padBottom;
    int vgap;

    protected StringBuilder builder;
    int size; //这个字段目前用不上
    int charIndex;

    protected Font font = new Font("Chiller",Font.PLAIN,15);
    protected Font font02 = new Font("GENISO",Font.PLAIN,25);
    protected Font font03 = new Font("SHOWCARD GOTHIC",Font.PLAIN,15);

    //选择模式
    Shape[] shapes;
    String[] keyBoardStrings;
    //步长参数：每行多少个元素
    int strideLine;
    int total;
    boolean drawBorder;
    KeyBoardSelectionModel selectionModel;
    //1：表示换行 ，0表示限制
    int mode;
    private boolean filling;
    //点击动画
    boolean clicked,entering;
    int gap;

    public SimpleCleanKeyBoard(double leftX, double leftY, double totalW, double totalH) {
        this.leftX = leftX;
        this.leftY = leftY;
        this.totalW = totalW;
        this.totalH = totalH;
        //TODO
        builder = new StringBuilder();
        total = getTotal();
        shapes = new Shape[total];
        keyBoardStrings = new String[total];
        selectionModel = new KeyBoardSelectionModel(total);
        initial();
    }

    public int getTotal() {
        return 'z' - '!' + 1;
    }

    //这个方法修改
    public void initial() {
        strideLine = 10;
        padTop = padBottom= 2;
        padLeft = padRight = 2;
        vgap = padBottom + padTop;
        drawBorder = true;
        gap = 50;
        adjustedWAndH((int) totalW, (int) totalH);
        fillShapes();
        fillStringArray();
    }

    private void fillStringArray() {
        int i;
        for (i = '!';i<='z';i++) {
            String letter = ""+(char) i;
            keyBoardStrings[i - '!'] = letter;
        }
    }

    private void adjustedWAndH(int w, int h) {
        rw = w / strideLine - (padLeft + padRight);
        rh = h / (strideLine - 1) - (padBottom + padTop);
    }

    private void fillShapes() {
        int x = (int) leftX;
        int yy= (int) leftY;
        Rectangle2D r;
        for (int i = 0;i<shapes.length;i++) {
            x = (i % strideLine == 0)? (int) (padLeft + leftX) : x + rw + padLeft + padRight;
            if (i == 0)
                yy = yy + padTop;
            else
                yy = (i % strideLine == 0) ?  yy+rh+vgap : yy;
            r = new Rectangle2D.Double(x,yy,rw,rh);
            shapes[i] = r;
        }
    }


    public void processInput(MouseInputEvent mouseInputEvent, CheckInputEvent keyBoardEvent) {
        if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1))
            clicked = true;
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_S))
            selectionModel.addClickedIndex(strideLine);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A))
            selectionModel.addClickedIndex(-1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_D))
            selectionModel.addClickedIndex(1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_W))
            selectionModel.addClickedIndex(-strideLine);
        if ((keyBoardEvent.keyDownOnce(KeyEvent.VK_BACK_SPACE)) && getSize()  > 0)
            deleteChar();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_LEFT))
            addCharIndex(-1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_RIGHT))
            addCharIndex(1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_ENTER))
            entering = true;
    }

    public void  update(double delta,Point2D mousePoint) {
        checkMaskR(mousePoint);
        clicked = clicked && getMaskIndex() != -1;
        if (clicked) {
            //TODO 点击事件
            setClickedIndex(getMaskIndex());
            addString();
        }
        clicked = false;

        if (entering) {
            setMaskIndex(getClickedIndex());
            addString();
        }
        entering = false;
    }

    //重点
    public void draw(Graphics2D g2,Color color) {
        g2.setColor(color);
        Rectangle2D r;
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = null;
        for (int i = 0; i < shapes.length; i++) {
            Shape s = shapes[i];
            double cx = s.getBounds().getX();
            double cy = s.getBounds().getY();
            Font f = font.deriveFont((float) rh);
            tl = new TextLayout(keyBoardStrings[i], f, frc);
            float ascent = tl.getAscent();
            float advance = tl.getAdvance();
            cx = cx + (rw - advance) / 2.0F;
            tl.draw(g2, (float) cx, (float) cy + ascent);
        }

        //绘制外边框边框
        if (drawBorder) {
            r = new Rectangle2D.Double(leftX ,leftY ,totalW,totalH);
            g2.draw(r);
        }

        g2.setFont(new Font("隶书", Font.PLAIN, 15));
        tl = new TextLayout("虚拟键盘", g2.getFont(), frc);
        //获取基线上升位置
        float ascent;
        //获取前进距离
        float advance = tl.getAdvance();
        //计算中心小
        float centerX = (float) (leftX + (totalW - advance) / 2.0F);
        tl.draw(g2, centerX, (float) leftY);

        //绘制选择形状
        if (selectionModel.getMaskIndex() != -1)
        {
            Shape s = shapes[selectionModel.getMaskIndex()];
            g2.setColor(new Color(0.5F,0.5F,0.5F,0.5F));
            g2.fill(s);
        }

        if (selectionModel.getClickedIndex() != -1)
        {
            Shape s = shapes[selectionModel.getClickedIndex()];
            g2.setColor(Color.CYAN);
            g2.draw(s);
        }

        //绘制输入框和文本
        if (builder.length() <= 0)
            return;
        if (mode == 0){
            tl = new TextLayout(builder.toString(),font02,frc);
            ascent = tl.getAscent();
            float descent = tl.getDescent();
            advance = tl.getAdvance();
            float hight = tl.getDescent() + ascent + tl.getLeading();
            //边框矩形
            r = new Rectangle2D.Double(leftX,leftY - gap -  hight,totalW,hight);
            g2.draw(r);
            //绘制文本
            tl.draw(g2, (float) leftX + 10, (float) (leftY - descent - gap));
            filling =  advance >= (totalW - 20);
            if (filling) {
                g2.setColor(Color.red);
                //输入框矩形
                g2.draw(r);
            }
            //绘制光标
            //获取字体度量
            Shape caretShape = tl.getCaretShape(TextHitInfo.trailing(charIndex));
            AffineTransform af = AffineTransform.getTranslateInstance(leftX + 10,leftY - descent - gap);
            caretShape = af.createTransformedShape(caretShape);
            g2.draw(caretShape);
        }
    }

    public void addString() {
        if (filling) return;
        builder.append(
                keyBoardStrings[selectionModel.getClickedIndex()]
        );
        charIndex = size++;
    }

    public void deleteChar() {
        if (charIndex < 0)
            return;
        builder.deleteCharAt(charIndex);
        size--;
        charIndex--;
        if (size == 0)
            charIndex = size;
    }

    public void checkMaskR(Point2D mouseP) {
        for (int i = 0;i<shapes.length;i++) {
            var s = shapes[i];
            if (s == null) {
                selectionModel.resetMaskIndex();
                break;
            }
            if (s.contains(mouseP)) {
                selectionModel.setMaskIndex(i);
                break;
            }else {
                selectionModel.resetMaskIndex();
            }
        }
    }

    public int getMaskIndex() {
        return selectionModel.getMaskIndex();
    }

    public void setClickedIndex(int index) {
        selectionModel.setClickedIndex(index);
    }

    public void setMaskIndex(int index) {
        selectionModel.setMaskIndex(index);
    }

    public int getClickedIndex() {
        return selectionModel.getClickedIndex();
    }

    public int getSize() {
        return size;
    }

    public void addCharIndex(int delta) {
        charIndex += delta;
        charIndex = Math.max(0,Math.min(charIndex,size-1));
    }

    public double getLeftX() {
        return leftX;
    }

    public double getLeftY() {
        return leftY;
    }

    public double getTotalW() {
        return totalW;
    }

    public double getTotalH() {
        return totalH;
    }
}
