package com.snl.test.display;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;

public class DisplayModelActiveRender extends JFrame implements Runnable {

    Thread gameThread;
    transient boolean running;
    GraphicsDevice gd;
    BufferStrategy bs;
    FrameV2 v2;
    DisplayMode currentDisplayModel;
    final double G = 9.98;
    Rectangle2D r = new Rectangle2D.Double(50,50,50,50);

    public DisplayModelActiveRender() {
        v2 = new FrameV2();
        setResizable(false);
        //获取图像设备
        getGraphicsDevice();
        createUi();
        //添加键盘监听器
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int eID = e.getID();
                if (eID == KeyEvent.KEY_PRESSED)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.VK_ESCAPE:
                            if (gd.isDisplayChangeSupported())
                            {
                                gd.setDisplayMode(currentDisplayModel);
                                gd.setFullScreenWindow(null);
                            }
                            break;
                        case KeyEvent.VK_BACK_SPACE:
                            Utils.showClosingDialog(null);
                            break;
                        case KeyEvent.VK_SPACE :
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void createUi() {
        Canvas c = new Canvas();
        c.setSize(new Dimension(600,500));

        getContentPane().add(c);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utils.centerContainer(this);

        //设置全屏
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
            gd.setDisplayMode(getSelectModel());
        }

        //创建缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();

        //游戏线程
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    private void getGraphicsDevice() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = environment.getDefaultScreenDevice();
        currentDisplayModel = gd.getDisplayMode();
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
                //获取绘图上下文
                Graphics drawGraphics = bs.getDrawGraphics();
                //清空缓冲
                drawGraphics.clearRect(0,0,getWidth(),getHeight());
                //更新
                update();
                //渲染
                render(drawGraphics);
                //清空引用
                drawGraphics.dispose();
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    private void update() {
        double y = r.getY() + G * 0.2;
        r.setFrame(r.getX(),y,r.getWidth(),r.getHeight());
        //更新帧率
        v2.calculateFrameRate();
    }

    private DisplayMode getSelectModel() {
//        var d = Utils.getCurrentDisplayMode();
        var d = Utils.listAllDisplayModes()[6];
        currentDisplayModel = d;
        int width = d.getWidth();
        int height = d.getHeight();
        int bitDepth = d.getBitDepth();
        int refresh = DisplayMode.REFRESH_RATE_UNKNOWN;
        return new DisplayMode(width,height,bitDepth,refresh);
    }

    private void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.red);
        g2.fill(r);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,BasicStroke.JOIN_BEVEL));
        g2.drawString(v2.getFrameRate(),30,30);
        g2.dispose();
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(DisplayModelActiveRender::new);
    }

}
