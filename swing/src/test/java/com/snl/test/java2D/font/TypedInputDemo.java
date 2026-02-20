package com.snl.test.java2D.font;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.font.practice.KeyBoard;
import com.snl.test.java2D.font.practice.SimpleCleanKeyBoard;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TypedInputDemo extends DiKaErPlus {

    boolean kangjuchi;
    //选择模式
    boolean showingKeyBoard;

    KeyBoard keyBoard;
    SimpleCleanKeyBoard cleanKeyBoard;

    public TypedInputDemo() throws HeadlessException {
        drawAxis =  false;
        WIDTH = HEIGHT = 900;
        //获取字体度量
        keyBoard = new KeyBoard();
        cleanKeyBoard = new SimpleCleanKeyBoard(100,500,300,300);
        showingKeyBoard = true;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        keyBoard.initial(c);
//        cleanKeyBoard.initial(c);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B))
            kangjuchi = !kangjuchi;
        cleanKeyBoard.processInput(mouseInputEvent,keyBoardEvent);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_T))
            showingKeyBoard = !showingKeyBoard;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (showingKeyBoard) {
            cleanKeyBoard.update(delta,mouseInputEvent.getCurrentPoint());
        }
    }


    @Override
    protected void reset() {
        super.reset();
        keyBoard.reset();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.drawString("按下 B 键开启或者关闭抗锯齿",30,130);
        g2.drawString("按下 T 键显示键盘",30,150);
        g2.drawString("按下 0/1 键切换模式",30,170);
        if (showingKeyBoard)
            cleanKeyBoard.draw(g2,Color.RED);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TypedInputDemo());
    }
}

//还有一些其他的东西，比如怎么获取文字，现在我还不会，以后再学
