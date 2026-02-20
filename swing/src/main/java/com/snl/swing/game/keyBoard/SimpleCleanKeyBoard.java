package com.snl.swing.game.keyBoard;

import com.snl.swing.game.input.CheckInputEvent;
import com.snl.swing.game.input.MouseInputEvent;
import com.snl.swing.game.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SimpleCleanKeyBoard {

    /* 布局参数 */
    //包围矩形
    protected double leftX,leftY;
    protected double totalW,totalH;
    //这个字段是动态修改每个字符的包围矩形的作用
    protected int rw,rh;
    private int padTop,padLeft,padRight,padBottom;
    //步长参数：每行多少个元素
    private int strideLine;

    //字符的包围矩形数组
    protected Shape[] shapes;
    //键盘字符索引
    private String[] keyBoardStrings;
    //字符选择模式
    private final KeyBoardSelectionModel selectionModel;
    //输入框
    private final InputFrame inputFrame;

    /* 绘制模式 */
    //1：表示换行 ，0表示限制
    int mode;
    //以下是运行模式
    //限制模式
    private static final int LIMIT = 0;
    //换行模式
    private static final  int CHANGING_LINE = 1;

    /* 布尔变量  */
    //布尔变量，是否要绘制包围矩形
    private boolean drawBorder;
    //是否要绘制出每个字符的包围矩形
    private boolean drawCharBorder;
    //是否需要绘制输入框
    private boolean showingInputFrame;
    //点击动画
    private boolean clicked,entering;

    Font font = Utils.font;

    public SimpleCleanKeyBoard(double leftX, double leftY, double totalW, double totalH) {
        this.leftX = leftX;
        this.leftY = leftY;
        this.totalW = totalW;
        this.totalH = totalH;
        //TODO
        //total参数
        int total = getTotal();
        shapes = new Shape[total];
        keyBoardStrings = new String[total];
        selectionModel = new KeyBoardSelectionModel(total);
        inputFrame = new InputFrame(leftX,leftY,totalW,30);
        initial();
    }

    //这个方法修改
    public void initial() {
        strideLine = 10;
        padTop = padBottom= 2;
        padLeft = padRight = 2;
        mode = LIMIT;
        rw = (int) (totalW / strideLine - (padLeft + padRight));
        rh = (int) (totalH / (strideLine - 1) - (padBottom + padTop));
        fillShapes();
        fillStringArray();
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
                yy = (i % strideLine == 0) ?  yy+rh+padBottom + padTop : yy;
            r = new Rectangle2D.Double(x,yy,rw,rh);
            shapes[i] = r;
        }
    }

    private void fillStringArray() {
        int i;
        for (i = '!';i<='z';i++) {
            String letter = ""+(char) i;
            keyBoardStrings[i - '!'] = letter;
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
        if ((keyBoardEvent.keyDownOnce(KeyEvent.VK_BACK_SPACE)) && inputFrame.getSize()  >  0)
            inputFrame.deleteChar();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_LEFT))
            inputFrame.addCharIndex(-1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_RIGHT))
            inputFrame.addCharIndex(1);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_ENTER))
            entering = true;
    }

    public void update(double delta,Point2D mousePoint) {
        checkMaskR(mousePoint);
        clicked = clicked && getMaskIndex() != -1;
        if (clicked) {
            //TODO 点击事件
            setClickedIndex(getMaskIndex());
            inputFrame.addString(
                    keyBoardStrings[selectionModel.getClickedIndex()]);
        }
        clicked = false;

        if (entering) {
            setMaskIndex(getClickedIndex());
            inputFrame.addString(
                    keyBoardStrings[selectionModel.getClickedIndex()]);
        }
        entering = false;
    }

    public void draw(Graphics2D g2,Color color) {
        g2.setColor(color);
        if (showingInputFrame)
            inputFrame.draw(g2);
        //绘制虚拟键盘
        drawKeyBorder(g2);
        //绘制输入框和文本
    }

    private void drawKeyBorder(Graphics2D g2) {
        Rectangle2D r;
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = null;
        for (int i = 0; i < shapes.length; i++) {
            Shape s = shapes[i];
            if (drawCharBorder)
                g2.draw(s);
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

        //绘制蒙版
        if (selectionModel.getMaskIndex() != -1)
        {
            Shape s = shapes[selectionModel.getMaskIndex()];
            g2.setColor(new Color(0.5F,0.5F,0.5F,0.5F));
            g2.fill(s);
        }

        //绘制选择框
        if (selectionModel.getClickedIndex() != -1)
        {
            Shape s = shapes[selectionModel.getClickedIndex()];
            g2.setColor(Color.CYAN);
            g2.draw(s);
        }
    }

    public void checkMaskR(Point2D mouseP) {
        for (int i = 0;i<shapes.length;i++) {
            var s = shapes[i];
            if (s == null) {
                selectionModel.resetMaskIndex();
                return;
            }
            if (s.contains(mouseP)) {
                selectionModel.setMaskIndex(i);
                return;
            }
        }
        selectionModel.resetMaskIndex();
    }

    public int getTotal() {
        return 'z' - '!' + 1;
    }

    private int getMaskIndex() {
        return selectionModel.getMaskIndex();
    }

    private int getClickedIndex() {
        return selectionModel.getClickedIndex();
    }

    private void setClickedIndex(int index) {
        selectionModel.setClickedIndex(index);
    }

    private void setMaskIndex(int maskIndex) {
        selectionModel.setMaskIndex(maskIndex);
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public void setDrawCharBorder(boolean drawCharBorder) {
        this.drawCharBorder = drawCharBorder;
    }

    public void setShowingInputFrame(boolean showingInputFrame) {
        this.showingInputFrame = showingInputFrame;
    }

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public boolean isDrawCharBorder() {
        return drawCharBorder;
    }

    public boolean isShowingInputFrame() {
        return showingInputFrame;
    }

    public String getInputString() {
        return inputFrame.builder.toString();
    }
}
