package com.snl.swing.game.gameFrame;

import com.snl.swing.game.input.CheckInputEvent;
import com.snl.swing.game.input.MouseInputEvent;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class SimpleGameFramePlus extends JFrame implements Runnable {

    //游戏线程
    protected Thread gameThread;
    //游戏运行
    protected transient boolean running;
    //双缓冲区
    protected BufferStrategy bs;
    //画布
    protected Canvas c;
    //画布宽
    protected  int WIDTH = 600;
    //画布高
    protected  int HEIGHT = 600;
    //鼠标输入事件
    protected CheckInputEvent keyBoardEvent;
    //获取安全键盘输入
//    protected SafeKeyboardInput keyBoardEvent;
    //鼠标输入事件
    protected MouseInputEvent mouseInputEvent;
    //帧率类
    protected FrameV2 v2;

    //世界高
    protected int wordWidth = 12;
    //世界宽
    protected int wordHeight = 12;
    //游戏字体
    protected Font appFont = new Font("隶书", Font.PLAIN, 15);
    //游戏线程休眠时间
    protected long appSleep = 16L;
    //是否需要画布维持比率
    protected boolean appMaintainRatio  = true;
    //视图矩阵
    protected Matrix3x3f viewMat;

    //非必要字段
    protected int scaleX,scaleY;

    public SimpleGameFramePlus() throws HeadlessException {
        super("游戏框架进阶版");
    }

    private void createAndShowUi() {
        //生成事件
        createEvent();
        //创建Ui
        createUi();
        scaleX = WIDTH / wordWidth;
        scaleY = HEIGHT / wordHeight;
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
        getContentPane().add(c,BorderLayout.CENTER);
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
    protected void handleResizeEvent(ComponentEvent e) {
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

        scaleX = WIDTH / wordWidth;
        scaleY = HEIGHT / wordHeight;
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

    public Matrix3x3f getScaleViewPortMat() {
        return Utils.getScaleViewPortMat(c,wordWidth,wordHeight);
    }

    public Matrix3x3f getReverseScaleViewPortMat() {
        return Utils.getReverseScaleViewPortMat(c,wordWidth,wordHeight);
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
            animation(delta);
            Utils.sleep(appSleep);
            lastTime = currentTime;
        }
        teminate();
    }

    protected void animation(double delta) {
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
    }

    /**
     * 更新精灵
     * @param delta 时间间隔
     */
    protected void updateSprite(double delta) {
        v2.calculateFrameRate();
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
        g2.drawString("鼠标按下:[%s]".formatted(mouseInputEvent.checkButton()),30,50);
        g2.drawString("使用滑动滚轮缩放地图",30,70);
        g2.drawString("按下 h 键居中地图",30,90);
        g2.drawString("按下 c 重置",30,110);
        g2.drawString("[%d px : 1 单位]".formatted(c.getWidth() / wordWidth),30,c.getHeight() - 20);
        //TODO
        g2.setColor(Color.PINK);
        g2.drawString("笛卡尔坐标系",c.getWidth() - 100,30);
        g2.draw(mouseInputEvent.getMouseShape());
        g2.dispose();
    }
}
