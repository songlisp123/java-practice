package com.snl.test.frame;

import com.snl.test.java2D.UTIL.Axis;
import com.snl.test.frame.util.Utils;
import com.snl.test.java2D.input.CheckInputEvent;
import com.snl.test.java2D.input.MouseInputEvent;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class SimpleGameFrame extends JFrame implements Runnable {

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

    protected Point2D originPoint;
    protected Axis axis;

    Vector2D[] testShape,copy;
    double rot,thetaDelta;

    List<Point2D> points = new ArrayList<>(); //世界坐标点
    protected Point2D currentPoint;


    public SimpleGameFrame() throws HeadlessException {
        super("游戏框架");
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

        //创建轴线
        axis = new Axis();
        axis.createAxis(c,WIDTH / wordWidth);
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

        axis.createAxis(c,wordWidth);
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
    protected static void launchGame(SimpleGameFrame frame) {
        SwingUtilities.invokeLater(frame::createAndShowUi);
    }

    //**********************************************************************//
    /* ******************          矩阵变换          *********************** */
    //**********************************************************************//

    public Matrix3x3f getViewportTransform() {
        return Utils.getViewportTransform(c,wordWidth,wordHeight);
    }

    public Matrix3x3f getReverseWorldTransForm() {
        return Utils.getReverseWorldTransForm(c,wordWidth,wordHeight);
    }

    public Vector2D getMousePointInWorldPosition() {
        Matrix3x3f mat = getReverseWorldTransForm();
        Vector2D v = Utils.pointConvertToVector(
                mouseInputEvent.getCurrentPoint()
        );
        return mat.mul(v);
    }

    public Point2D convertWorldPointToScreenPoint(Point2D p) {
        Matrix3x3f view = getViewportTransform();
        Vector2D v = Utils.pointConvertToVector(p);
        v = view.mul(v);
        return Utils.vectorCovertToPoint(v);
    }

    public Point2D convertScreenPointToWorldPoint(Point2D p) {
        Matrix3x3f worldView = getReverseWorldTransForm();
        Vector2D v = Utils.pointConvertToVector(p);
        v = worldView.mul(v);
        return Utils.vectorCovertToPoint(v);
    }

    public Matrix3x3f getScaleViewPortMat() {
        return Utils.getScaleViewPortMat(c,wordWidth,wordHeight);
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
        //TODO
        //一个菱形形状
        testShape = new Vector2D[] {
                new Vector2D(-1,.0),new Vector2D(.0,1.0),
                new Vector2D(1.0,.0),new Vector2D(.0,-1.0),
        };
        copy = new Vector2D[testShape.length];

        rot = 0;
        thetaDelta = Math.PI / 4;
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
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A)) {
            rot -= thetaDelta;
        }
        if(keyBoardEvent.keyDownOnce(KeyEvent.VK_D)){
            rot += thetaDelta;
        }

//        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
//        {
//            wordWidth--;
//            wordHeight--;
//            axis.createAxis(c,wordWidth);
//        }
//        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN)) {
//            wordWidth++;
//            wordHeight++;
//            axis.createAxis(c,wordWidth);
//        }

        if (mouseInputEvent.mouseClickedTwo(MouseEvent.BUTTON1))
        {
            //点击左键,将当前屏幕点转换成世界点
            points.add(Utils.vectorCovertToPoint(
                    getMousePointInWorldPosition()
            ));
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_C))
        {
            reset();
        }

    }

    protected void reset() {
        wordWidth = wordHeight = 12;
        rot = 0;
        appSleep = 16;
        appMaintainRatio = true;
        axis.createAxis(c,wordWidth);
        points.clear();
        currentPoint = null;
    }

    /**
     * 更新精灵
     * @param delta 时间间隔
     */
    protected void updateSprite(double delta) {
        v2.calculateFrameRate();
        axis.updateAxis(delta);
        checkPoint();

        //TODO
        System.arraycopy(testShape,0,copy,0,copy.length);
    }

    private void checkPoint() {
        if (points.isEmpty())
            return;
        Point2D mP = Utils.vectorCovertToPoint(getMousePointInWorldPosition());
        for (Point2D p : points)
        {
            //判断
            double x = p.getX();
            double y = p.getY();
            if (x - .1 <= mP.getX() && x + 0.1 >= mP.getX() &&
                    y -0.1 <= mP.getY() && y + .1 >= mP.getY()) {
                currentPoint = p;
                break;
            }
            currentPoint = null;
        }

        if (currentPoint != null)
        {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else {
            c.setCursor(null);
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

    protected void draw(Graphics g) {
        var g2 = (Graphics2D)g.create();
        g2.setFont(appFont);
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
        g2.drawString("当前鼠标世界坐标：[%.2f,%.2f]".formatted(
                getMousePointInWorldPosition().getX(),
                getMousePointInWorldPosition().getY()
        ),30,90);
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,110);
        g2.drawString("按下 a 左旋转",30,130);
        g2.drawString("按下 d 右旋转",30,150);
        g2.drawString("按下 c 重置",30,170);
        g2.drawString("按下 鼠标左键 添加点",30,190);
        g2.drawString("[%d px : 1 单位]".formatted(c.getWidth() / wordWidth),30,c.getHeight() - 20);
        drawOriginalPoint(g2);
        axis.draw(g2);
        //TODO
        g2.setColor(Color.PINK);
        g2.drawString("笛卡尔坐标系",c.getWidth() - 100,30);
        drawTestShape(g2);
        drawPoint(g2);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.dispose();
    }

    private void drawOriginalPoint(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        Matrix3x3f mat = getViewportTransform();
        Vector2D v = mat.mul(new Vector2D());
        originPoint = Utils.vectorCovertToPoint(v);
        Shape o = new Ellipse2D.Double(
                originPoint.getX() - 4,originPoint.getY() - 4,
                8,8
        );
        g2.fill(o);
    }

    private void drawTestShape(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        double width = c.getBounds().getWidth();
        double height = c.getBounds().getHeight();
        double tx = width / 2.0;
        double ty = height / 2.0;
        double sx = width / wordWidth;
        double sy = height / wordHeight;
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.translate(tx,ty));
        mat = mat.mul(Matrix3x3f.scale(sx,-sy));
        mat = mat.mul(Matrix3x3f.rotate(rot));
        for (int i = 0;i<copy.length;i++) {
            copy[i] = mat.mul(copy[i]);
        }
        //绘制
        Utils.drawPolygon(g2,copy);
    }

    private void drawPoint(Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        Point2D sP;
        for (Point2D p : points) {
            sP = convertWorldPointToScreenPoint(p);
            Shape s = new Ellipse2D.Double(sP.getX() - 1, sP.getY() - 1, 2, 2);
            g2.draw(s);
            g2.drawString("[%.2f,%.2f]".formatted(p.getX(), p.getY()),
                    (int) sP.getX(), (int) (sP.getY() - 3));
        }
        if (currentPoint != null)
        {
            g2.setColor(Color.MAGENTA);
            Point2D p2 = convertWorldPointToScreenPoint(currentPoint);
            var c = new Ellipse2D.Double(p2.getX() - 4, p2.getY() - 4, 8, 8);
            g2.fill(c);
        }
    }

}
