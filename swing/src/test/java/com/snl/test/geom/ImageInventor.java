package com.snl.test.geom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageInventor extends JPanel {

    private Thread gameThread;
    private boolean isRunning = true;
    private BufferedImage bi;

    public ImageInventor() {
        setBackground(Color.black);
        createBufferedImage();
        createTextual();
        gameThread = new Thread(gameLoop(),"游戏线程");
        gameThread.start();
    }

    private void createBufferedImage() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screenDevice.getDefaultConfiguration();
        bi = configuration.createCompatibleImage(50, 50,
                BufferedImage.TYPE_INT_RGB);
    }


    private void createTextual() {
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0, bi.getWidth(),bi.getHeight());
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //todo
        Graphics2D g2 = (Graphics2D) g.create();
        AffineTransform oldTransform = g2.getTransform();
        oldTransform.translate(2 *bi.getWidth(),2 * bi.getHeight());
        g2.drawImage(bi,oldTransform,null);
        oldTransform.scale(-1,1);
        oldTransform.translate(-bi.getWidth(),0);
        g2.drawImage(bi,oldTransform,null);
        g2.setTransform(oldTransform);
        g2.dispose();
    }

    private Runnable gameLoop() {
        //游戏循环
        long ms = 16L;
        return () -> {
            while (isRunning)
            {
                //游戏运行
                //第一步更新状态
                updateSprites();
                //第二步调用组件的repaint方法
                repaint();
                //让线程睡眠16毫秒
                while (!Thread.currentThread().isInterrupted())
                {
                    try {
                        Thread.sleep(ms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void updateSprites() {

    }


}
