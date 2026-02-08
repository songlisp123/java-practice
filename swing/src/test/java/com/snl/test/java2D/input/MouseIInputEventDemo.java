package com.snl.test.java2D.input;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class MouseIInputEventDemo extends JFrame implements Runnable {

    Thread gameThread;
    private transient boolean running;
    private BufferStrategy bs;
    FrameV2 v2;
    final Color DEFAULT_COLOR = Color.BLACK;

    private MouseInputEvent mouseInputEvent;
    private CheckInputEvent keyBoardEvent;

    final List<Point2D> line = new ArrayList<>();
    boolean drawLine;

    public MouseIInputEventDemo() throws HeadlessException {
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
        Canvas c = new Canvas();

        c.setSize(600,500);
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
        running = true;
        while (running)
        {
            processInput();
            updateSprite(); //暂时不实现
            render(); //不实现
            Utils.sleep(16);
        }
    }

    private void processInput() {
        keyBoardEvent.poll();
        mouseInputEvent.poll();


        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            drawLine = !drawLine;
        if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1) && drawLine)
        {
            line.add(mouseInputEvent.getCurrentPoint());
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            line.clear();
        }
    }

    private void updateSprite() {
        v2.calculateFrameRate();
    }

    private void render() {
        do {
            do {
                Graphics drawGraphics = bs.getDrawGraphics();
                drawGraphics.setColor(DEFAULT_COLOR);
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
        g2.drawString("绘制模式:[%s]".formatted(drawLine),30,130);
        g2.drawString("按下 c 键清空画布",30,150);
        g2.draw(mouseInputEvent.getMouseShape());
        drawLine(g2);
        g2.dispose();
    }

    private void drawLine(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.cyan);
        for (int i=0;i<line.size() - 1;i++) {
            Point2D p1 = line.get(i);
            drawPoint(g2,p1);
            Point2D p2 = line.get(i + 1);
            drawPoint(g2,p2);
            Line2D l = new Line2D.Double(p1,p2);
            g2.draw(l);
        }
        g2.dispose();
    }

    private void drawPoint(Graphics2D g2, Point2D p) {
        RectangularShape r = new Ellipse2D.Double(p.getX() - 2,p.getY() - 2,4,4);
        g2.draw(r);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MouseIInputEventDemo::new);
    }


}
