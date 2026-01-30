package com.snl.test.frame;

import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class TestBufferStrategy extends JFrame implements Runnable {

    FrameV2 v2;
    Thread gameThread;
    boolean running;
    BufferStrategy bf;

    public TestBufferStrategy() {
        v2 = new FrameV2();
        createAndShowUi();
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    private void createAndShowUi() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = Utils.showClosingDialog();
                if (i == JOptionPane.YES_OPTION)
                {
                    if (gameThread != null)
                        gameThread = null;
                    running = false;
                    System.exit(0);
                }
            }
        });
        Canvas c = new Canvas();
        c.setPreferredSize(new Dimension(300,300));
        this.getContentPane().add(c);
        Utils.centerContainer(this);
        this.pack();
        this.setVisible(true);
        c.createBufferStrategy(2);
        bf = c.getBufferStrategy();
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

    private void gameLoop() {
        do {
            do {
                //获取回执上下文
                Graphics drawGraphics = bf.getDrawGraphics();
                //清除离屏图像
                drawGraphics.setColor(getBackground());
                drawGraphics.fillRect(0,0,getWidth(),getHeight());
//                drawGraphics.clearRect(0,0,getWidth(),getHeight());
                //渲染图形
                render(drawGraphics);
                //去除引用
                drawGraphics.dispose();

            }while (bf.contentsRestored());
            //显示缓冲区
            bf.show();
        }while (bf.contentsLost());
    }

    private void render(Graphics g) {
        //更新帧率
        v2.calculateFrameRate();
        //绘制帧率
        g.setColor(Color.BLACK);
        g.drawString(v2.getFrameRate(),30,30);
    }

    public static void main(String[] args) {
        Utils.listAllDisplayModes();
        EventQueue.invokeLater(TestBufferStrategy::new);
    }

}
