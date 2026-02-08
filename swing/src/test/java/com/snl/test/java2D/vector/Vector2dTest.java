package com.snl.test.java2D.vector;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;

public class Vector2dTest extends JFrame implements Runnable {

    Thread gameThread;
    BufferStrategy bs;
    Canvas c; //画布
    FrameV2 v2;
    CheckInputEvent keyBoardEvent;
    MouseInputEvent mouseInputEvent;
    transient boolean running;
    final Color DEFAULT_COLOR = Color.BLACK;
    final int WIDTH = 600;
    final int HEIGHT = 500;

    Vector2D[] shapes;
    Vector2D[] worlds;

    private double tx,ty;
    private double dx,dy;
    private double rot,rotStep;
    private double scale,scaleStep;
    private double sx,sxStep;
    private double sy,syStep;
    private boolean doTranslate;
    private boolean doScale;
    private boolean doRotate;
    private boolean doXShear;
    private boolean doYShear;

    public Vector2dTest() throws HeadlessException {
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
        shapes = new Vector2D[]{
                new Vector2D(10,0),new Vector2D(-10,8),
                new Vector2D(0,0),new Vector2D(-10,-8)
        };
        worlds = new Vector2D[shapes.length];
        reset();
    }

    private void reset() {
        tx = 0;
        ty = 0;
        dx = dy = 2;
        rot = 0.0f;
        rotStep = Math.toRadians(Math.PI / 4);
        scale = 1.0f;
        scaleStep = 0.1f;
        sx = sy = 0.0f;
        sxStep = syStep = 0.01f;
        doRotate = doScale = doTranslate = doXShear
                = doYShear = false;
    }

    private void processInput() {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_T))
        {
            //平移
            doTranslate = !doTranslate;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R))
        {
            //旋转
            doRotate = !doRotate;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_S))
        {
            //缩放
            doScale = !doScale;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_X))
        {
            //剪切
            doXShear = !doXShear;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_Y))
        {
            //Y轴剪切
            doYShear = !doYShear;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            //重置
            reset();
        }
    }

    private void updateSprite() {
        v2.calculateFrameRate();
        //TODO
        //复制当前形状
        for (int i=0;i<shapes.length;i++)  {
            worlds[i] = new Vector2D(shapes[i]);
        }

        if (doScale)
        {
            scale += scaleStep;
            if (scale < 1.0 || scale  > 5.0)
            {
                scaleStep = - scaleStep;
            }
        }

        if (doRotate)
        {
            rot += rotStep;
            if (rot < 0.0 || rot > Math.PI * 2)
            {
                rot = 0;
            }
        }

        if (doTranslate)
        {
            tx += dx;
            if (tx <  0 || tx >= WIDTH)
            {
                dx = -dx;
            }

            ty += dy;
            if (ty < 0 || ty >= HEIGHT)
            {
                dy = -dy;
            }
        }

        if (doXShear)
        {
            sx += sxStep;
            if (Math.abs(sx) > 1.0)
            {
                sxStep = -sxStep;
            }
        }

        if (doYShear)
        {
            sy += syStep;
            if (Math.abs(sy) > 1.0)
            {
                syStep = -syStep;
            }
        }

        for (Vector2D world : worlds) {
            world.shear(sx, sy);
            world.scale(scale, scale);
            world.rotate(rot);
            world.translation(tx,ty);
        }
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
        AffineTransform af = g2.getTransform();
        g2.translate(WIDTH/2 ,HEIGHT / 2);
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1.0F,
                new float[]{3,5,3},0));
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
        g2.drawString("平移:[%s]".formatted(doTranslate),30,130);
        g2.drawString("旋转:[%s]".formatted(doRotate),30,150);
        g2.drawString("缩放:[%s]".formatted(doScale),30,170);
        g2.drawString("x轴剪切:[%s]".formatted(doXShear),30,190);
        g2.drawString("y轴剪切:[%s]".formatted(doYShear),30,210);
        g2.setTransform(af);
        g2.translate(20,20);
        drawShapes(g2);
        g2.setTransform(af);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.drawString("你好，世界",50,50);
        g2.dispose();
    }

    private void drawShapes(Graphics2D g2) {
        Vector2D f = worlds[worlds.length - 1];
        Vector2D p;
        for (Vector2D world : worlds) {
            p = world;
            Line2D l = new Line2D.Double(f.x, f.y, p.x, p.y);
            g2.draw(l);
            f = p;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Vector2dTest::new);
    }
}
