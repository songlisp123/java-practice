package com.snl.test.TIMEANDSPACE.space;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.input.CheckInputEvent;
import com.snl.test.input.MouseInputEvent;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

public class ScreenMappingDemo extends JFrame implements Runnable {

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

    Vector2D[] tri;
    Vector2D[] triWord;

    Vector2D[] rec;
    Vector2D[] recWorld;

    public ScreenMappingDemo() throws HeadlessException {
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
            Utils.sleep(16);
            lastTime = currentTime;
        }
    }

    private void gameInitial() {
        running = true;
        tri = new Vector2D[]{
                new Vector2D(0.0f,0.5f),new Vector2D(-0.5f,-0.5f),new Vector2D(0.5f,-0.5f)
        };
        triWord = new Vector2D[tri.length];
        rec = new Vector2D[] {
                new Vector2D(-1.0f,1.0f),new Vector2D(1.0f,1.0f),
                new Vector2D(1.0f,-1.0f),new Vector2D(-1.0f,-1.0f)
        };
        recWorld = new Vector2D[rec.length];
    }

    private void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
    }

    private void updateSprite(double delta) {
        v2.calculateFrameRate();
        //TODO
        changePos();
    }

    private void changePos() {
        xAix = new Line2D.Double(
                0,c.getHeight() / 2.0,c.getWidth(),c.getHeight() / 2.0
        );
        yAix = new Line2D.Double(
                c.getWidth() / 2.0,0,c.getWidth() / 2.0,c.getHeight()
        );
        screenMiddlePoint = new Point2D.Double(c.getWidth()  /2.0,c.getHeight() / 2.0);
        originPointShape = new Ellipse2D.Double(screenMiddlePoint.getX() - 10,
                screenMiddlePoint.getY() - 10,20,20);
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
        g2.draw(xAix);
        g2.draw(yAix);
        g2.draw(originPointShape);
        g2.setColor(Color.cyan);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.setColor(Color.cyan);
        drawTri(g2);
        g2.dispose();
    }



    private void drawTri(Graphics2D g2) {
        double w = 2.0;
        double h = 2.0;

        int cw = c.getWidth() - 1;
        int ch = c.getHeight() - 1;

        double sx = cw / w;
        double sy = ch / h;

        double tx = cw / 2.0;
        double ty = ch / 2.0;

        Matrix3x3f view = Matrix3x3f.identity();
        view = view.mul(Matrix3x3f.translate(tx,ty));
        view = view.mul(Matrix3x3f.scale(sx,sy));

        int i;
        for (i = 0;i<tri.length;i++) {
            triWord[i] = view.mul(tri[i]);
        }

        drawPolygon(g2,triWord);

        for (i=0;i< rec.length;i++) {
            recWorld[i] = view.mul(rec[i]);
        }
        drawPolygon(g2,recWorld);
    }

    private void drawPolygon(Graphics2D g2, Vector2D[] polyGon) {
        Vector2D f;
        int i;
        Vector2D p = polyGon[polyGon.length -1];
        for (i = 0;i<polyGon.length;i++) {
            f = polyGon[i];
            Line2D l = new Line2D.Double(f.getX(),f.getY(),p.getX(),p.getY());
            g2.draw(l);
            p = f;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScreenMappingDemo::new);
    }
}
