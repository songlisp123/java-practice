package com.snl.test.java2D.game;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.font.practice.SimpleCleanKeyBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GameBootPanel extends DiKaErPlus {

    SimpleCleanKeyBoard keyBoard,keyBoardCopy;
    boolean showKeyBoard;
    Shape[] shapes;
    BufferedImage[] images;
    int imageIndex;
    int padTop,padBottom,padLeft,padRight,vGap;
    int sl;
    int index;
    final  String[] strings = {
           "a", "2","3","4","5","6","7","8","9","10","j","q","k"
    };

    final  char[] chars = {'c','d','h','s'};
    int total;

    boolean clicked;
    int selectedIndex = -1;
    boolean leftFocus,rightFocus;

    public GameBootPanel() throws HeadlessException {
        drawAxis= false;
        WIDTH = 600;
        HEIGHT = 600;
        total = 52;
        shapes = new Shape[total];
        images = new BufferedImage[shapes.length];
        imageIndex = -1;
        padTop = padBottom = 75;
        padLeft = padRight = 5;
        vGap = 10;
        sl = 7;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        showKeyBoard = true;
        double totalH = 200;
        double v = (c.getBounds().getHeight() - totalH) / 2.0;
        keyBoard = new SimpleCleanKeyBoard(50,v,200,200);
        keyBoardCopy = new SimpleCleanKeyBoard(300,v,200,200);
        fillImages();
        fillShapes();
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

    private int fills(String path,int index) {
        //获取缩放倍数
        int w = c.getWidth();
        int h = c.getHeight();

        int sw = (int) (w / (2.0 * sl) - (padLeft + padRight));
        ImageIcon icon = new ImageIcon(path);
        int sh = sw * icon.getIconHeight() / icon.getIconWidth();
        BufferedImage bi = new BufferedImage(sw,sh,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.clearRect(0,0,bi.getWidth(), bi.getHeight());
        g2.drawImage(icon.getImage(),0,0,bi.getWidth(),bi.getHeight(),null);
        g2.dispose();
        images[index++] = bi;
        return index;
    }

    private void fillShapes() {
        int x = (int) (c.getWidth() / 2.0 + padLeft);
        int xCopy = x;
        int y = padTop;
        for (int i = 0;i<shapes.length;i++) {
            //TODO
            BufferedImage image = images[i];
            if (image == null)
                break;
            x = (i % sl == 0) ? xCopy : x + image.getWidth() + padLeft +padRight;
            if (i != 0)
                y = (i%sl == 0) ? y+image.getHeight() + vGap : y;
            Shape r = new Rectangle2D.Double(x,y,image.getWidth(),image.getHeight());
            shapes[i] = r;
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        checkFocus();
        if (leftFocus)
            if (showKeyBoard)
                keyBoard.processInput(mouseInputEvent,keyBoardEvent);
        if (rightFocus) {
                //TODO
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A))
                selectedIndex--;
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_D))
                selectedIndex++;
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_W) && selectedIndex > sl)
                    selectedIndex -= sl;
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_S) && selectedIndex < total - sl)
                selectedIndex += sl;
            selectedIndex = Math.max(0,Math.min(selectedIndex,total-1));
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            showKeyBoard = !showKeyBoard;
    }

    private void checkFocus() {
        if (clicked && (mousePos.getX() < c.getWidth() / 2.0)) {
            leftFocus = true;
            rightFocus = false;
        }

        if (clicked && (mousePos.getX() >= c.getWidth() / 2.0))
        {
            leftFocus = false;
            rightFocus = true;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D currentPoint = mouseInputEvent.getCurrentPoint();
        //更新键盘
        //是否是左焦点
        if (leftFocus)
            if (showKeyBoard)
                keyBoard.update(delta,currentPoint);
        else if (rightFocus) {
            //TODO
        }
        checkClicked(currentPoint);

    }

    private void checkClicked(Point2D currentPoint) {
        for (int i = 0;i<shapes.length;i++) {
            Shape s = shapes[i];
            if (s == null) {
                return;
            }
            if (clicked && s.contains(currentPoint)) {
                selectedIndex = i;
                break;
            }
        }
        clicked = false;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做

        if (showKeyBoard) {
            g2.setClip(0,0, (int) mousePos.getX(),c.getHeight());
            keyBoard.draw(g2, Color.WHITE);
            keyBoardCopy.draw(g2,Color.CYAN);
        }
        g2.drawString("按下 SPACE 点火",30,130);
        //绘制中心线
        g2.setColor(Color.WHITE);
        int w = c.getWidth();
        int h = c.getHeight();
        var l = new Line2D.Double(mousePos.getX(),0,mousePos.getX(),h);
        g2.draw(l);
        g2.setClip((int) mousePos.getX(),0,c.getWidth(),c.getHeight());
        for (int i = 0;i<shapes.length;i++) {
            //TODO
            Shape s = shapes[i];
            if (s == null)
                continue;
            g2.draw(s);
            int x = s.getBounds().x;
            int y = s.getBounds().y;

            g2.translate(x,y);
            g2.drawImage(images[i],0,0,null);
            g2.translate(-x,-y);
        }
        if (selectedIndex != -1)
        {
            Shape s = shapes[selectedIndex];
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                    new float[]{3,5,3},2));
            g2.draw(s);
        }
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new GameBootPanel());
    }

    //选择
}
