package com.snl.test.java2D.time;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;

public class TimeDeltaDemo extends JFrame implements Runnable {

    Thread gameThread;
    transient boolean running;
    BufferStrategy bs;
    Canvas c;

    final int WIDTH = 600;
    final int HEIGHT = 600;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;

    FrameV2 v2;

    Line2D xAix;
    Line2D yAix;
    Point2D screenMiddlePoint;
    Shape originPointShape;

    double step;
    long sleep;
    double angle;

    public TimeDeltaDemo() throws HeadlessException {
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
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utils.centerContainer(this);
        setVisible(true);
        addKeyListener(keyBoardEvent);
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
            delta = frame / 1.0E9;
            processInput(delta);
            updateSprite(delta); //暂时不实现
            render(); //不实现
            Utils.sleep(sleep);
            lastTime = currentTime;
        }
    }


    private void gameInitial() {
        running = true;
        sleep = 16;
        angle = 0.0;
        step = Math.PI / 2;
        xAix = new Line2D.Double(
                0,HEIGHT / 2.0,WIDTH,HEIGHT / 2.0
        );
        yAix = new Line2D.Double(
                WIDTH / 2.0,0,WIDTH / 2.0,HEIGHT
        );
        screenMiddlePoint = new Point2D.Double(WIDTH  /2.0,HEIGHT / 2.0);
        originPointShape = new Ellipse2D.Double(screenMiddlePoint.getX() - 10,
                screenMiddlePoint.getY() - 10,20,20);
        var p = new Point2D.Double(screenMiddlePoint.getX() + WIDTH / 2.0,
                screenMiddlePoint.getY());
        var p2 = new Point2D.Double(screenMiddlePoint.getX() - WIDTH / 2.0,
                screenMiddlePoint.getY());
    }

    private void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            sleep += 5;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
        {
            sleep -= 5;
        }

        if (sleep > 1000)
        {
            sleep = 16;
        }
        if (sleep < 0)
            sleep = 16;
    }

    private void updateSprite(double delta) {
        v2.calculateFrameRate();
        //TODO
        angle += step * delta;
//        angle += step * v2.getmFrameRate() / 1000;
        if (angle > 2 * Math.PI) {
            angle -= 2 * Math.PI;
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

    private void draw(Graphics g) {
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
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,110);
        g2.drawString("当前休眠时间:[%d]".formatted(sleep),30,130);
        drawHandle(g2);
        g2.setColor(Color.cyan);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.dispose();
    }

    private void drawHandle(Graphics2D g2) {
        Matrix3x3f m = Matrix3x3f.identity();
        int w = WIDTH / 2;
        int h = HEIGHT / 2;
        m = m.mul(Matrix3x3f.translate(w,h));
        Vector2D v = m .mul(new Vector2D());
        RectangularShape s = new Ellipse2D.Double(v.getX() - w / 2.0,v.getY() - h / 2.0,w,h);
        g2.draw(s);

        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.rotate(angle));
        mat = m.mul(mat);
        mat = mat.mul(Matrix3x3f.translate(w  /2.0,0));
        Vector2D v2  = mat.mul(new Vector2D());

        double cx = v2.getX();
        double cy = v2.getY();
        Line2D l = new Line2D.Double(new Point2D.Double(cx,cy),screenMiddlePoint);
        g2.draw(l);

        s = new Rectangle2D.Double(cx - 2,cy - 2,4,4);
        g2.draw(s);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimeDeltaDemo::new);
    }
}
