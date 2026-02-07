package com.snl.test.frame;

import com.snl.test.TIMEANDSPACE.UTIL.AxisPlus;
import com.snl.test.frame.util.Utils;
import com.snl.test.input.CheckInputEvent;
import com.snl.test.input.MouseInputEvent;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

public class SimpleGameFramePlus extends JFrame implements Runnable {

    protected Thread gameThread;
    protected transient boolean running;
    protected BufferStrategy bs;
    protected Canvas c;
    protected final int WIDTH = 900;
    protected final int HEIGHT = 900;
    protected CheckInputEvent keyBoardEvent;
    protected MouseInputEvent mouseInputEvent;
    protected FrameV2 v2;

    protected int wordWidth = 12;
    protected int wordHeight = 12;

    protected Font appFont = new Font("隶书", Font.PLAIN, 15);
    protected long appSleep = 16L;
    protected boolean appMaintainRatio  = true;

    protected boolean dragging;
    protected Vector2D mousePos,mouseDelta; //(世界坐标)
    protected Vector2D mousePosScreen;
    protected Matrix3x3f viewMat;
    protected AxisPlus axis;
    //原点形状
    Point2D originPoint;

    public SimpleGameFramePlus() throws HeadlessException {
        super("游戏框架进阶版");
    }

    private void createAndShowUi() {
        //生成事件
        createEvent();
        //创建Ui
        createUi();
        //游戏线程
        startGame();
    }

    //**********************************************************************//
    /* ******************          游戏初始化         *********************** */
    //**********************************************************************//

    private void createEvent() {
        mouseInputEvent = new MouseInputEvent();
        keyBoardEvent = new CheckInputEvent();

        v2 = new FrameV2();
    }

    /**
     * 创建ui
     */
    private void createUi() {
        c = new Canvas();

        c.setSize(WIDTH,HEIGHT);
        c.setIgnoreRepaint(true);

        //添加监听器
        c.addKeyListener(keyBoardEvent);
        c.addMouseListener(mouseInputEvent);
        c.addMouseMotionListener(mouseInputEvent);

        addKeyListener(keyBoardEvent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getContentPane().add(c);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResizeEvent(e);
            }
        });
        pack();
        Utils.centerContainer(this);
        setVisible(true);

        c.requestFocus();
        //创建缓冲区
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
        //添加窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Utils.showClosingDialog(c);
            }
        });
    }

    /**
     * 处理拖动窗口逻辑
     * @param e 组件事件
     */
    private void handleResizeEvent(ComponentEvent e) {
        Dimension size = getContentPane().getSize();
        int vw = size.width ;
        int vh = size.height;
        int vx = 0;
        int vy = 0;
        int newWidth = vw;
        int newHeight = vw * wordWidth / wordHeight;
        if (newHeight > vh)
        {
            newWidth = vh * wordHeight / wordWidth;
            newHeight = vh;
        }
        vx += (vw - newWidth) / 2;
        vy += (vh - newHeight) / 2;
        c.setLocation(vx,vy);
        c.setSize(newWidth,newHeight);
    }

    /**
     * 启动游戏线程
     */
    private void startGame() {
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    /**
     * 启动游戏类
     */
    protected static void launchGame(SimpleGameFramePlus frame) {
        SwingUtilities.invokeLater(frame::createAndShowUi);
    }

    //**********************************************************************//
    /* ******************          矩阵变换          *********************** */
    //**********************************************************************//

    public Matrix3x3f getViewportTransform() {
        Matrix3x3f viewportTransform =
                Utils.getViewportTransform(c, wordWidth, wordHeight);
        viewportTransform = viewportTransform.mul(viewMat);
        return viewportTransform;
    }

    public Matrix3x3f getReverseWorldTransForm() {
        Matrix3x3f RmAT =
                Utils.getReverseWorldTransForm(c, wordWidth, wordHeight);
        Matrix3x3f inView = viewMat.inverse();
        return inView.mul(RmAT);
    }

    //需要修改
    public Vector2D getMousePointInWorldPosition() {
        Matrix3x3f mat = getReverseWorldTransForm();
        return mat.mul(mousePosScreen);
    }

    public Point2D convertWorldPointToScreenPoint(Point2D p) {
        Matrix3x3f view = getViewportTransform();
        return view.mul(p);
    }

    public Point2D convertScreenPointToWorldPoint(Point2D p) {
        Matrix3x3f worldView = getReverseWorldTransForm();
        return worldView.mul(p);
    }

    public Matrix3x3f getScaleViewPortMat() {
        return Utils.getScaleViewPortMat(c,wordWidth,wordHeight);
    }

    public Matrix3x3f getReverseScaleViewPortMat() {
        return Utils.getReverseScaleViewPortMat(c,wordWidth,wordHeight);
    }

    public Matrix3x3f getTranslationMat() {
        return Utils.getTranslationMat(c,wordWidth,wordHeight);
    }

    //**********************************************************************//
    /* ******************          游戏线程          *********************** */
    //**********************************************************************//

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
            Utils.sleep(appSleep);
            lastTime = currentTime;
        }
        teminate();
    }

    /**
     * 游戏终止逻辑
     */
    private void teminate() {

    }

    /**
     * 游戏初始化变量
     */
    protected void gameInitial() {
        running = true;
        viewMat = Matrix3x3f.identity();
        mousePos = new Vector2D();
        mousePosScreen = getViewportTransform().mul(mousePos);
        //创建轴
        axis = new AxisPlus();
        axis.createAxis(getViewportTransform(),c);
        originPoint = new Point2D.Double();
        //TODO
    }

    //**********************************************************************//
    /* ******************          游戏循环          *********************** */
    //**********************************************************************//

    /**
     * 处理游戏输入
     * @param delta 时间间隔
     */
    protected void processInput(double delta) {
        keyBoardEvent.poll();
        mouseInputEvent.poll();
        Vector2D pos = new Vector2D(mouseInputEvent.getCurrentPoint());
        mouseDelta = pos.sub(mousePos);
        mousePos = pos;
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON2);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            reset();
        }
        //TODO

    }

    protected void reset() {
        wordWidth = wordHeight = 12;
        appSleep = 16;
        appMaintainRatio = true;
        viewMat = Matrix3x3f.identity();
        mouseDelta = new Vector2D();
        axis.createAxis(getViewportTransform(),c);
    }

    /**
     * 更新精灵
     * @param delta 时间间隔
     */
    protected void updateSprite(double delta) {
        v2.calculateFrameRate();
        if (dragging)
        {
            // 像素 → 世界单位
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D v = re.mul(mouseDelta);
            viewMat = Matrix3x3f.translate(v.getX(),v.getY()).mul(viewMat);
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
            ));
            axis.createAxis(getViewportTransform(),c);
        }
        else
            setCursor(null);
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

    protected void draw(Graphics g) {
        var g2 = (Graphics2D)g.create();
        g2.setFont(appFont);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        g2.setStroke(new BasicStroke(2));
        g2.drawString(v2.getFrameRate(),30,30);
//        g2.drawString("当前鼠标相对坐标：[%.1f,%.1f]".formatted(
//                mouseInputEvent.getCurrentPoint().getX(),
//                mouseInputEvent.getCurrentPoint().getY()
//        ),30,50);
//        g2.drawString("当前鼠标绝对坐标：[%.1f,%.1f]".formatted(
//                mouseInputEvent.getAbsPoint().getX(),
//                mouseInputEvent.getAbsPoint().getY()
//        ),30,70);
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,50);
        g2.drawString("向 上 滑动滚轮放大地图",30,70);
        g2.drawString("向 下 滑动滚轮缩小地图",30,90);
        g2.drawString("按下 c 重置",30,110);
        g2.drawString("[%d px : 1 单位]".formatted(c.getWidth() / wordWidth),30,c.getHeight() - 20);
//        drawOriginalPoint(g2);
        //TODO
        g2.setColor(Color.PINK);
        g2.drawString("笛卡尔坐标系",c.getWidth() - 100,30);
        axis.draw(g2);
        g2.draw(mouseInputEvent.getMouseShape());
        drawPoint(g2,originPoint);
        g2.dispose();
    }

    private void drawPoint(Graphics2D g2, Point2D point) {
        g2.setColor(Color.MAGENTA);
        Matrix3x3f mat = getViewportTransform();
        Point2D p = mat.mul(point);
        Shape o = new Ellipse2D.Double(
                p.getX() - 4,p.getY() - 4,
                8,8
        );
        g2.fill(o);
        g2.drawString("[%.2f,%.2f]".formatted(point.getX(),point.getY()),
                (int) p.getX(), (int) (p.getY() - 10));
    }

//    private void drawOriginalPoint(Graphics2D g2) {
//        g2.setColor(Color.MAGENTA);
//        Matrix3x3f mat = getViewportTransform();
//        Vector2D v = mat.mul(new Vector2D());
//        Point2D p = mat.mul(originPoint);
//        Shape o = new Ellipse2D.Double(
//                p.getX() - 4,p.getY() - 4,
//                8,8
//        );
//        g2.fill(o);
//        g2.drawString("[%.2f,%.2f]".formatted(v.getX(),v.getY()),
//                (int) p.getX(), (int) (p.getY() - 10));
//    }

}
