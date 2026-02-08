package com.snl.test.java2D.coords;

import com.snl.test.java2D.UTIL.Axis;
import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class DiKaEr extends JFrame implements Runnable {

    Thread gameThread;
    transient boolean running;
    BufferStrategy bs;
    Canvas c;
    final int WIDTH = 900;
    final int HEIGHT = 900;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;
    FrameV2 v2;
    Axis axis;
    CameraCoords camera;

    int GAP = 50;
    int step = 5 , scale =  1;
    List<Point2D> points = new ArrayList<>(); //世界坐标点
    Point2D currentPoint;

    public DiKaEr() throws HeadlessException {
        super("测试框架");
        //生成事件
        createEvent();
        //创建Ui
        createUi();
        //游戏线程
        startGame();
    }

    private void createEvent() {
        mouseInputEvent = new MouseInputEvent();
        keyBoardEvent = new CheckInputEvent();

        v2 = new FrameV2();
    }

    private void createUi() {
        c = new Canvas();

        c.setSize(WIDTH,HEIGHT);
        c.setIgnoreRepaint(true);
        c.addKeyListener(keyBoardEvent);
        c.addMouseListener(mouseInputEvent);
        c.addMouseMotionListener(mouseInputEvent);

        getContentPane().add(c);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utils.centerContainer(this);
        setVisible(true);
        addKeyListener(keyBoardEvent);
        setResizable(false);
        c.requestFocus();
        //创建缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
    }

    private void startGame() {
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    @Override
    public void run() {
        gameInitial();
        long currentTime = System.nanoTime();
        long lastTime = currentTime;
        double frame , delta;
        while (running)
        {
            currentTime = System.nanoTime();
            frame = currentTime - lastTime; //计算每帧过去的时间
            delta = frame / 1.0E9; //时间间隔
            processInput(delta); //获取输入
            updateSprite(delta); //暂时不实现
            render(); //不实现
            Utils.sleep(14);
            lastTime = currentTime;
        }
    }

    private void gameInitial() {
        running = true;
        //坐标
        axis = new Axis();
        axis.createAxis(c,GAP);
        //移动形状？？
//        moveShape = new Ellipse2D.Double(c.getWidth() / 2.0 - 4,
//                c.getHeight() / 2.0 - 4,8,8);
//        copy = moveShape;
        //重置相机
        resetCamera();
    }

    public void resetCamera() {
        camera = new CameraCoords(
                (double) GAP / scale,
                new Point2D.Double(c.getWidth() / 2.0,c.getHeight() / 2.0)
        );
    }

    public void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (camera.scale <= 10 )
        {
            camera.scale = 10;
        }
        if (GAP >= c.getWidth() / 2)
        {
            camera.scale = c.getWidth() / 2.0;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            //如果按上上箭头
            camera.scale += step;
            axis.createAxis(c, (int) camera.scale);
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
        {
            //下箭头
            camera.scale -= step;
            axis.createAxis(c, (int) camera.scale);
        }

        if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1))
        {
            //点击左键,将当前屏幕点转换成世界点
            points.add(
                    camera.ScreenToWorld(mouseInputEvent.getCurrentPoint())
            );
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            reset();
        }
        //……

    }

    public void reset() {
        points.clear();
        currentPoint = null;
    }

    public void updateSprite(double delta) {
        v2.calculateFrameRate();
        axis.updateAxis(delta);
        checkPoint();
        //TODO
    }

    private void checkPoint() {
        if (points.isEmpty())
            return;
        Point2D mP = camera.ScreenToWorld(mouseInputEvent.getCurrentPoint());
        for (Point2D p : points)
        {
            //判断
            double x = p.getX();
            double y = p.getY();
            if (x - .1 <= mP.getX() && x + 0.1 >= mP.getX() &&
                    y -0.1 <= mP.getY() && y + .1 >= mP.getY()) {
                currentPoint = p;
                break;
            }
            currentPoint = null;
        }

        if (currentPoint != null)
        {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else {
            c.setCursor(null);
        }
    }

    private void render() {
        do {
            do {
                Graphics drawGraphics = bs.getDrawGraphics();
                drawGraphics.setColor(Color.BLACK);
                drawGraphics.fillRect(0,0,getWidth(),getHeight());
                draw(drawGraphics);
                drawGraphics.dispose();
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    public void draw(Graphics g) {
        var g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        g2.setStroke(new BasicStroke(2));
        g2.drawString(v2.getFrameRate(),30,30);
        g2.drawString("当前鼠标相对坐标：[%.1f,%.1f]".formatted(
                mouseInputEvent.getCurrentPoint().getX(),
                mouseInputEvent.getCurrentPoint().getY()
        ),30,50);
        g2.drawString("当前鼠标绝对坐标：[%.1f,%.1f]".formatted(
                mouseInputEvent.getAbsPoint().getX(),
                mouseInputEvent.getAbsPoint().getY()
        ),30,70);
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,90);
        g2.draw(mouseInputEvent.getMouseShape());
        axis.draw(g2);
        //TODO
        g2.setColor(Color.PINK);
        g2.drawString("按下 上箭头 增加间距",30,110);
        g2.drawString("按下 下箭头 减少间距",30,130);
        g2.drawString("按下 C 重绘",30,150);
        g2.drawString("[%.0f px : 1 m]".formatted(camera.scale),30,c.getHeight() - 20);
        g2.drawString("笛卡尔坐标系",c.getWidth() - 100,30);
        drawPoint(g2);
        g2.dispose();
    }

    private void drawPoint(Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        Point2D sP;
        for (Point2D p : points) {
            sP = camera.worldToScreen(p);
            Shape s = new Ellipse2D.Double(sP.getX() - 1, sP.getY() - 1, 2, 2);
            g2.draw(s);
            g2.drawString("[%.2f,%.2f]".formatted(p.getX(), p.getY()),
                    (int) sP.getX(), (int) (sP.getY() - 3));
        }
        if (currentPoint != null)
        {
            g2.setColor(Color.MAGENTA);
            Point2D p2 = camera.worldToScreen(currentPoint);
            var c = new Ellipse2D.Double(p2.getX() - 4, p2.getY() - 4, 8, 8);
            g2.fill(c);
        }
    }
}
