package com.snl.test.java2D.space;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

public class ViewPortDiKaEr extends JFrame implements Runnable, MouseWheelListener {
    Thread gameThread;
    transient boolean running;
    BufferStrategy bs;
    Canvas c;

    final int WIDTH = 600;
    final int HEIGHT = 600;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;

    FrameV2 v2;
    Point2D originPoint,originPointCopy;

    boolean clicked,dragging;
    Vector2D mousePos,mouseDelta,mouseD;

    int worldWidth,worldHeight;
    int sx,sy;
    Vector2D c0,c0Pos;
    double r0;

    public ViewPortDiKaEr() throws HeadlessException {
        super("测试框架");
        //生成事件
        createEvent();
        //创建Ui
        createUi();
        //游戏线程
        startGame();
        addMouseWheelListener(this);
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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Utils.centerContainer(this);
        setVisible(true);
        addKeyListener(keyBoardEvent);
        c.requestFocus();
        //创建缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Utils.showClosingDialog(c);
            }
        });
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
        mousePos = new Vector2D();
        mouseD = new Vector2D();
        worldWidth = worldHeight = 4;
        sx = sy = WIDTH / worldWidth;
        originPoint = new Point2D.Double();
        c0Pos = new Vector2D(1,1);
        r0 = 1;
    }

    private void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON2);
        if (clicked)
            mousePos =  Utils.pointConvertToVector(mouseInputEvent.getCurrentPoint());
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON2);

        if(keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            reset();
        }
    }

    private void reset() {
        mouseD = new Vector2D();
    }

    private void updateSprite(double delta) {
        v2.calculateFrameRate();
        changePos();
        //TODO
        if (dragging) {
            Vector2D pos =
                    Utils.pointConvertToVector(mouseInputEvent.getCurrentPoint());
            //获取dx,dy
            mouseDelta = pos.sub(mousePos);
            double dx = mouseDelta.getX() / sx;
            double dy = mouseDelta.getY() / sy;
            mouseD = mouseD.add(new Vector2D(dx,dy));
            mousePos = pos;
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
            ));
        }else
            setCursor(null);
        double x = mouseD.getX();
        double y = mouseD.getY();
        c0 = c0Pos.add(new Vector2D(-x,y));
        originPoint = new Point2D.Double(-x,y);
    }

    private void changePos() {
        //TODO

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
        g2.drawString("按下 c 键重置",30,130);
        g2.drawString("[%d px : 1 单位]".formatted(sx),30,c.getHeight() - 20);
        g2.setColor(Color.cyan);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.setColor(Color.cyan);
        drawOriginPoint(g2,originPoint);
        g2.dispose();
    }

    private void drawOriginPoint(Graphics2D g2, Point2D p) {
        Shape s= new Ellipse2D.Double(
                p.getX() - 4,p.getY()-4,8,8
        );
        //鼠标拖动的距离
        double width = c.getBounds().getWidth();
        double height = c.getBounds().getHeight();
        double tx = width  / 2.0;
        double ty = height / 2.0;
        double viewportCenterX = p.getX() * sx +  tx;
        double viewportCenterY = ty - p.getY() * sy;
        g2.drawString("[0,0]", (int) viewportCenterX, (int) viewportCenterY);
        Line2D l = new Line2D.Double(viewportCenterX,viewportCenterY,width,viewportCenterY);
        g2.draw(l);
        l = new Line2D.Double(viewportCenterX,viewportCenterY,viewportCenterX,-height);
        g2.draw(l);
        g2.fill(s);
        //获取圆的坐标
        double c0X = c0.getX() * sx + tx;
        double c0Y = ty - c0.getY() * sy;
        double r = r0 * sx;
        Shape circle = new Ellipse2D.Double(c0X - r ,c0Y - r,r * 2,r *2);
        g2.draw(circle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewPortDiKaEr ::new);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            worldWidth--;
            worldHeight--;
        }

        if (wheelRotation == 1) {
            worldWidth++;
            worldHeight++;
        }

        if (worldWidth <= 2)
            worldWidth = worldHeight = 2;

        if (worldWidth == WIDTH || worldHeight == HEIGHT)
        {
            worldWidth = WIDTH;
            worldHeight = HEIGHT;
        }

        sx = WIDTH / worldWidth;
        sy = HEIGHT / worldHeight;
    }
}
