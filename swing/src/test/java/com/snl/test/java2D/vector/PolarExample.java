package com.snl.test.java2D.vector;

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

public class PolarExample extends JFrame implements Runnable {

    Thread gameThread;
    FrameV2 v2;
    BufferStrategy bs;
    transient boolean running;
    final int WIDTH = 600;
    final int HEIGHT = 600;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;
    Canvas c;
    final Color DEFAULT_COLOR = Color.BLACK;
    Point2D cord;
    Line2D xAix;
    Line2D yAix;
    Point2D screenMiddlePoint;
    Shape originPointShape;
    Line2D x_45du;

    public PolarExample() throws HeadlessException {
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
        while (running)
        {
            processInput();
            updateSprite(); //暂时不实现
            render(); //不实现
            Utils.sleep(16);
        }
    }

    private void gameInitial() {
        running = true;
        xAix = new Line2D.Double(
                0,HEIGHT / 2.0,WIDTH,HEIGHT / 2.0
        );
        yAix = new Line2D.Double(
                WIDTH / 2.0,0,WIDTH / 2.0,HEIGHT
        );
        screenMiddlePoint = new Point2D.Double(WIDTH  /2.0,HEIGHT / 2.0);
        originPointShape = new Ellipse2D.Double(screenMiddlePoint.getX() - 4,
                screenMiddlePoint.getY() - 4,8,8);
        var p = new Point2D.Double(screenMiddlePoint.getX() + WIDTH / 2.0,
                screenMiddlePoint.getY());
        var p2 = new Point2D.Double(screenMiddlePoint.getX() - WIDTH / 2.0,
                screenMiddlePoint.getY());
        x_45du = new Line2D.Double(p,p2);
    }

    private void processInput() {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1))
        {
            cord = mouseInputEvent.getCurrentPoint();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            if (cord != null)
                cord = null;
        }
    }

    private void updateSprite() {
        v2.calculateFrameRate();
        //TODO
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
        g2.draw(mouseInputEvent.getMouseShape());
        AffineTransform transform = g2.getTransform();
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,
                1.0f,new float[]{4,2,4},2.0f));
        g2.rotate(-Math.PI / 4,screenMiddlePoint.getX(),screenMiddlePoint.getY());
        g2.draw(x_45du);
        g2.rotate(Math.PI / 2,screenMiddlePoint.getX(),screenMiddlePoint.getY());
        g2.draw(x_45du);
        g2.setStroke(stroke);
        g2.setTransform(transform);
        g2.draw(xAix);
        g2.draw(yAix);
        g2.draw(originPointShape);
        //绘制
        drawRadius(g2);
        g2.dispose();
    }

    private void drawRadius(Graphics2D g2) {
        if (cord == null)
            return;
        g2.setColor(Color.cyan);
        Line2D l = new Line2D.Double(screenMiddlePoint,cord);
        g2.draw(l);
        double x = cord.getX();
        double y = cord.getY();
        RectangularShape e = new Ellipse2D.Double(x-2,y-2,4,4);
        g2.fill(e);
        x =(float) (x - screenMiddlePoint.getX());
        y =(float) (screenMiddlePoint.getY() - y);
        double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double rad = Math.atan2(y,x);
        double degrees = Math.toDegrees(rad);
        double sx = r * Math.cos(rad);
        double sy = r * Math.sin(rad);
        g2.drawString("笛卡尔坐标:[%.1f,%.1f]".formatted(x,y), (float) cord.getX(), (float) cord.getY());
        g2.drawString("极坐标:[%.1f,%.1f°]".formatted(r,degrees), (float) cord.getX(), (float) cord.getY() + 20);
        g2.drawArc((int) (screenMiddlePoint.getX() - r),
                (int) (screenMiddlePoint.getY() - r),
                (int) (2 * r),
                (int) (2 * r),0, (int) degrees);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PolarExample::new);
    }
}
