package com.snl.test.java2D.coords;

import com.snl.test.java2D.UTIL.Axis;
import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

//保证画布宽高比率一比一
public class DiKaErByViewPort extends JFrame implements Runnable {

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

    final int VIEW_WIDTH = 450;
    final int VIEW_HEIGHT = 450;

    Point2D viewPortCenter;
    Vector2D[] shape,shapeCopy;

    double gameWorldWidth = 12;
    double gameWorldHeight = 12;

    Vector2D worldPoint;

    public DiKaErByViewPort() throws HeadlessException {
        super("笛卡尔视图坐标");
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
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResizeEvent(e);
            }
        });
        //创建缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
    }

    private void handleResizeEvent(ComponentEvent e) {
        Dimension size = getContentPane().getSize();
        int vw = size.width ;
        int vh = size.height;
        int vx = 0;
        int vy = 0;
        int newWidth = vw;
        int newHeight = (int) (vw * gameWorldHeight / gameWorldWidth);
        if (newHeight > vh)
        {
            newWidth = (int) (vh * gameWorldWidth / gameWorldHeight);
            newHeight = vh;
        }
        vx += (vw - newWidth) / 2;
        vy += (vh - newHeight) / 2;
        c.setLocation(vx,vy);
        c.setSize(newWidth,newHeight);

        viewPortCenter = new Point2D.Double(
                newWidth / 2.0,newHeight / 2.0
        );
        int sx = (int) (newWidth / gameWorldWidth);
        if (axis != null)
            axis.createAxis(c,sx);
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
        //TODO
        shape = new Vector2D[]{
                new Vector2D(0,1),
                new Vector2D(1,1),
                new Vector2D(1,0),
                new Vector2D(0,0),
        };
        shapeCopy = new Vector2D[shape.length];
        worldPoint = new Vector2D();
        axis = new Axis();
        axis.createAxis(c, (int) (WIDTH/gameWorldWidth));
    }


    public void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
    }

    public void updateSprite(double delta) {
        v2.calculateFrameRate();
        axis.updateAxis(delta);
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

    public void draw(Graphics g) {
        var g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        g2.setStroke(new BasicStroke(2));
        g2.drawString(v2.getFrameRate(),30,30);
        g2.drawString("当前鼠标视图坐标：[%.1f,%.1f]".formatted(
                mouseInputEvent.getCurrentPoint().getX(),
                mouseInputEvent.getCurrentPoint().getY()
        ),30,50);
        g2.drawString("当前鼠标世界坐标：[%.2f,%.2f]".formatted(
                worldPoint.getX(),
                worldPoint.getY()
        ),30,70);
        g2.drawString("当前鼠标绝对坐标：[%.1f,%.1f]".formatted(
                mouseInputEvent.getAbsPoint().getX(),
                mouseInputEvent.getAbsPoint().getY()
        ),30,90);
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,110);
        g2.draw(mouseInputEvent.getMouseShape());
        axis.draw(g2);
        //TODO
        g2.setColor(Color.PINK);
        g2.drawString("按下 C 重绘",30,130);
        g2.drawString("笛卡尔坐标系",c.getWidth() - 100,30);
        drawCenter(g2);
        g2.dispose();
    }

    private void drawCenter(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.MAGENTA);
        Shape center = new Ellipse2D.Double(
                viewPortCenter.getX() - 4,  viewPortCenter.getY() - 4,
                8,8
        );
        g2.fill(center);
        //处理缩放
        double sx = c.getWidth() / gameWorldWidth;
        double sy = c.getHeight() / gameWorldHeight;
        //矩形
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.translate(viewPortCenter.getX(), viewPortCenter.getY()));
        mat = mat.mul(Matrix3x3f.scale(sx,-sy));
        for (int i = 0;i<shapeCopy.length;i++) {
            shapeCopy[i] = mat.mul(shape[i]);
        }
        Vector2D s;
        Vector2D p = shapeCopy[shapeCopy.length - 1];
        for (Vector2D vector2D : shapeCopy) {
            s = vector2D;
            Line2D l = new Line2D.Double(
                    p.getX(), p.getY(),
                    s.getX(), s.getY()
            );
            g2.draw(l);
            p = s;
        }
        Matrix3x3f m2 = Matrix3x3f.identity();
        m2 = m2.mul(Matrix3x3f.scale(1.0 / sx,1.0 / sy));
        worldPoint =m2.mul(new Vector2D(
               mouseInputEvent.getCurrentPoint().getX() - viewPortCenter.getX(),
               viewPortCenter.getY() - mouseInputEvent.getCurrentPoint().getY()
        ));
        g2.dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiKaErByViewPort::new);
    }
}
