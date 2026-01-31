package com.snl.test.input;

import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class SimpleKeyBoardImplement extends JFrame implements Runnable {

    Thread gameThread;
    transient boolean running;
    BufferStrategy bs;
    CheckInputEvent inputEvent;
//    boolean space;
//    boolean left;
//    boolean right;
//    boolean up;
//    boolean down;

    public SimpleKeyBoardImplement() throws HeadlessException {
        super("测试框架");
        init();
    }

    public SimpleKeyBoardImplement(String title) throws HeadlessException {
        super(title);
        init();
    }

    private void init() {
        Canvas c = new Canvas();
        inputEvent = new CheckInputEvent();
        c.addKeyListener(inputEvent);

        c.setSize(new Dimension(600,500));
        getContentPane().add(c);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utils.centerContainer(this);
        setVisible(true);
//        addKeyListener(inputEvent);

        //获取缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();

        //游戏线程
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    @Override
    public void run() {
        running = true;
        while (running)
        {
            gameLoop();
            Utils.sleep(16);
        }
    }

//    private void processInput() {
//        if (inputEvent.keyDown(KeyEvent.VK_SPACE)) {
//            if (!space) {
//                System.out.println("按下 空格键");
//            }
//            space = true;
//        }else {
//            space = false;
//        }
//        if (inputEvent.keyDown(KeyEvent.VK_ENTER)) {
//            System.out.println("输入 enter 键");
//        }
//        if (inputEvent.keyDown(KeyEvent.VK_UP)) {
//            if (!up)
//            {
//                System.out.println("按下 ⬆️ 箭头键");
//            }
//            up = true;
//        }else {
//            up = false;
//        }
//        if (inputEvent.keyDown(KeyEvent.VK_DOWN))
//        {
//            if (!down)
//            {
//                System.out.println("按下 ⬇️ 箭头");
//            }
//            down = true;
//        }else {
//            down = false;
//        }
//        if (inputEvent.keyDown(KeyEvent.VK_LEFT))
//        {
//            if (!left)
//                System.out.println("按下 ⬅️ 箭头");
//            left = true;
//        }else {
//            left = false;
//        }
//        if (inputEvent.keyDown(KeyEvent.VK_RIGHT))
//        {
//            if (!right)
//                System.out.println("按下 ➡️ 箭头");
//            right = true;
//        }else {
//            right = false;
//        }
//    }

    private void gameLoop() {
        do {
            do {
                processInput();
                Graphics drawGraphics = bs.getDrawGraphics();
                drawGraphics.setColor(getBackground());
                drawGraphics.fillRect(0,0,getWidth(),getHeight());
                render(drawGraphics);
                drawGraphics.dispose();
            }while (bs.contentsLost());
            bs.show();
        }while (bs.contentsRestored());
    }

    private void processInput() {
        inputEvent.poll();
        if (inputEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            System.out.println("空格键 按下");
        }

        if (inputEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            System.out.println("上箭头 按下");
        }

    }

    private void render(Graphics g) {

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(SimpleKeyBoardImplement::new);
    }
}
