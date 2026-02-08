package com.snl.test.java2D.coords;

import com.snl.test.java2D.UTIL.Axis;
import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;

public class AixelDemo extends JFrame implements Runnable {

    Thread gameThread;
    transient boolean running;
    BufferStrategy bs;
    Canvas c;

    final int WIDTH = 600;
    final int HEIGHT = 600;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;

    FrameV2 v2;
    Axis axis;

    Shape ball,yBall,ball45,ball135,middleSpeedBall,speedBall;
    Shape copy;
    boolean rolling;
    double rollDelta , rollDistance;

    int GAP = 50;
    int step = 5;

    public AixelDemo() throws HeadlessException {
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

        c.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                axis.createAxis(c,GAP);
                changeBall();
            }
        });
    }

    private void changeBall() {
        ball = new Ellipse2D.Double(c.getWidth() / 2.0 - 5,
                c.getHeight() / 2.0 - 5,10,10);
        copy = ball;
        yBall = ball;
        middleSpeedBall = ball;
        speedBall = ball;
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
            Utils.sleep(16);
            lastTime = currentTime;
        }
    }

    private void gameInitial() {
        running = true;
        rollDelta = GAP;
        rollDistance = 0;
        changeBall();
        axis = new Axis();
        axis.createAxis(c,GAP);
    }

    private void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            //空格键
            rolling = !rolling;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            reset();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            //如果按上上箭头
            GAP += step;
            axis.createAxis(c,GAP);
            rollDelta = GAP;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
        {
            //下箭头
            GAP -= step;
            axis.createAxis(c,GAP);
            rollDelta = GAP;
        }

        if (GAP <= 10 || GAP >= c.getWidth() / 2)
        {
            step = - step;
        }
    }

    private void reset() {
        changeBall();
        rollDistance = 0;
        rollDelta = GAP;
    }

    private void updateSprite(double delta) {
        v2.calculateFrameRate();
        axis.updateAxis(delta);
        //TODO
        if (rolling) {
            rollDistance += rollDelta * delta;
            AffineTransform t = AffineTransform.getTranslateInstance(rollDistance, 0);
            ball = t.createTransformedShape(copy);
            AffineTransform t5 = AffineTransform.getTranslateInstance(2 * rollDistance,0);
            middleSpeedBall = t5.createTransformedShape(copy);
            AffineTransform t6 = AffineTransform.getTranslateInstance(3 *  rollDistance,0);
            speedBall = t6.createTransformedShape(copy);
            AffineTransform t2 = AffineTransform.getTranslateInstance(0, -rollDistance);
            yBall = t2.createTransformedShape(copy);
        }
        checkCollide();
    }

    private void checkCollide() {
        if (ball.getBounds2D().getX() + ball.getBounds2D().getWidth() >= c.getWidth()
            || ball.getBounds2D().getX() <= 0)
        {
            rollDelta = -rollDelta;
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
        g2.drawString("按下 空格键 渲染动画",30,130);
        g2.drawString("这是一个 %d 像素为1m的绘制空间".formatted(GAP),30,170);
        g2.drawString("按下 上箭头 增加间距",30,190);
        g2.drawString("按下 下箭头 减少间距",30,210);
        axis.draw(g2);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.setColor(Color.cyan);
        //绘制球
        g2.fill(ball);
        g2.fill(yBall);
        g2.setColor(Color.red);
        g2.fill(middleSpeedBall);
        g2.setColor(Color.ORANGE);
        g2.fill(speedBall);
        g2.dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AixelDemo::new);
    }
}
