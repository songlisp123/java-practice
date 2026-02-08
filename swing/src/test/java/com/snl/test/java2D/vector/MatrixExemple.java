package com.snl.test.java2D.vector;

import com.snl.test.frame.FrameV2;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferStrategy;
import java.util.random.RandomGenerator;

public class MatrixExemple extends JFrame implements Runnable {

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
    Line2D x_45du;

    boolean showStars;
    Star[] stars;
    final RandomGenerator generator = RandomGenerator.getDefault();

    private float earthRot, earthDelta;
    private float moonRot, moonDelta;

    public MatrixExemple() throws HeadlessException {
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
        showStars = true;
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
        x_45du = new Line2D.Double(p,p2);
        //添加星星
        stars = new Star[350];
        for (int  i= 0;i<stars.length;i++) {
            Star star = new Star(
                    generator.nextDouble(WIDTH),
                    generator.nextDouble(HEIGHT),
                    2,
                    2
            );
            stars[i] = star;
        }

        earthDelta = (float) Math.toRadians(0.5);
        moonDelta = (float) Math.toRadians(2.5);
    }

    private void processInput() {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            showStars = !showStars;
        }
    }

    private void updateSprite() {
        v2.calculateFrameRate();
        //TODO
        for (Star s : stars)
        {
            s.update();
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
        g2.drawString("星空绘制：[%s](可按 空格键 关闭和开启)".formatted(showStars),30,130);

//        AffineTransform transform = g2.getTransform();
//        Stroke stroke = g2.getStroke();
//        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,
//                1.0f,new float[]{4,2,4},2.0f));
//        g2.rotate(-Math.PI / 4,screenMiddlePoint.getX(),screenMiddlePoint.getY());
//        g2.draw(x_45du);
//        g2.rotate(Math.PI / 2,screenMiddlePoint.getX(),screenMiddlePoint.getY());
//        g2.draw(x_45du);
//        g2.setStroke(stroke);
//        g2.setTransform(transform);
//        g2.draw(xAix);
//        g2.draw(yAix);
//        g2.draw(originPointShape);
        //绘制星星
        drawStars(g2);
        //绘制太阳
        drawSun(g2);
        g2.setColor(Color.cyan);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.dispose();
    }

    private void drawSun(Graphics2D g2) {
        //绘制太阳
        Matrix3x3f sunMat = Matrix3x3f.identity();
        sunMat = sunMat.mul(Matrix3x3f.translate(WIDTH / 2.0,HEIGHT / 2.0));
        Vector2D sun = sunMat.mul(new Vector2D());
        Point2D center  = new Point2D.Double(sun.x,sun.y);
        float radius = 50;
        float[] fractions = new float[]{
                0.0f,0.3f,0.6f,1.0f
        };
        Color[] colors = new Color[]{
                Color.YELLOW,Color.ORANGE,new Color(255,200,0,180),
                new Color(255,200,0,120)
        };
        RadialGradientPaint paint = new RadialGradientPaint(center,radius,fractions,colors);
        g2.setPaint(paint);
        RectangularShape sunShape = new Ellipse2D.Double(sun.x - 50,sun.y - 50,100,100);
        g2.fill(sunShape);
        //绘制太阳轨道
        g2.setColor(Color.WHITE);
        RectangularShape sunOrbit =
                new Ellipse2D.Double(sun.x - WIDTH / 4.0,sun.y - HEIGHT/4.0,WIDTH/2.0,HEIGHT/2.0);
        g2.draw(sunOrbit);
        //绘制地球
        Matrix3x3f earthMatTemp = Matrix3x3f.identity();
        earthMatTemp = earthMatTemp.mul(Matrix3x3f.rotate(earthRot));
        earthRot += earthDelta;
        //列向量
        earthMatTemp = sunMat.mul(earthMatTemp);
        earthMatTemp = earthMatTemp.mul(Matrix3x3f.translate(WIDTH / 4.0, 0));
        Vector2D earth = earthMatTemp.mul(new Vector2D());
        g2.setColor(Color.BLUE);
        RectangularShape earthShape =  new Ellipse2D.Double(earth.x - 10,earth.y - 10,20,20);
        g2.fill(earthShape);
        //绘制月球

        Matrix3x3f moonMat = Matrix3x3f.identity();
        moonMat = moonMat.mul(Matrix3x3f.rotate(moonRot));
        moonRot += moonDelta;
        moonMat = earthMatTemp.mul(moonMat);
        moonMat = moonMat.mul(Matrix3x3f.translate(WIDTH/16.0,0));
        Vector2D moon = moonMat.mul(new Vector2D());
        g2.setColor(Color.WHITE);
        earthShape = new Ellipse2D.Double(moon.x - 5,moon.y - 5,10,10);
        g2.fill(earthShape);
    }

    private void drawStars(Graphics2D g2) {
        if (!showStars)
            return;
        for (Star s : stars)
        {
            g2.setColor(s.color);
            g2.fill(s.shape);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MatrixExemple::new);
    }

    class Star  {
        int alpha;
        Shape shape;
        Color color = new Color(255,255,255,255);
        boolean shinning;

        public Star(double x,double y,int w,int h) {
            this(x,y,w,h,generator.nextInt(255));
        }

        public Star(double x,double y,int w,int h,int alpha) {
            shape = new Ellipse2D.Double(x,y,w,h);
            this.alpha = alpha;
        }

        public void update() {
            if (shinning) {
                alpha += generator.nextInt(10);
                if (alpha >= 255) {
                    shinning = false;
                    alpha = 255;
                }
            }else {
                alpha -= generator.nextInt(10);
                if (alpha <= 0)
                {
                    shinning = true;
                    alpha = 0;
                }
            }
            color = new Color(255,255,255,alpha);
        }
    }
}
