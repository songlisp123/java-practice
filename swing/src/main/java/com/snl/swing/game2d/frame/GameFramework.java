package com.snl.swing.game2d.frame;

import com.snl.swing.game2d.input.RelativeMouseInput;
import com.snl.swing.game2d.input.KeyBoardEvent;
import com.snl.swing.game2d.tool.FrameV2;
import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public abstract class GameFramework extends JFrame implements Runnable {

    //双缓冲
    private BufferStrategy bs;
    //游戏线程运行状态
    private volatile boolean running;
    //游戏线程
    private Thread gameThread;

    //视口左边位置
    protected int vx;
    protected int vy;
    //视口
    protected int vw;
    protected int vh;

    //帧率类
    protected FrameV2 frameV2;
    //鼠标类
    protected RelativeMouseInput mouse;
    //键盘输入类
    protected KeyBoardEvent keyboard;

    /* 游戏属性  */
    protected Color appBackground = Color.BLACK;
    protected Color appBorder = Color.LIGHT_GRAY;
    protected Color appFPSColor = Color.GREEN;
    protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
    protected String appTitle = "TBD-Title";

    protected float appBorderScale = 0.8f;
    //屏幕款
    protected int appWidth = 640;
    //屏幕高
    protected int appHeight = 640;
    //世界款
    protected float appWorldWidth = 2.0f;
    //世界高
    protected float appWorldHeight = 2.0f;
    //游戏线程休息时间
    protected long appSleep = 16L;
    //是否维持比率缩放
    protected boolean appMaintainRatio = false;
    //是否禁用鼠标
    protected boolean appDisableCursor = false;
    //文本位置
    protected int textPos = 0;

    public GameFramework() throws HeadlessException {
    }

    protected abstract void createFramework();

    protected abstract void renderFrame(Graphics g);

    public abstract int getScreenWidth();

    public abstract int getScreenHeight();

    protected void createAndShowGUI() {
        createFramework();
        if (appDisableCursor) {
            disableCursor();
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    protected void setupInput(Component component) {
        keyboard = new KeyBoardEvent();
        component.addKeyListener(keyboard);
        mouse = new RelativeMouseInput(component);
        component.addMouseListener(mouse);
        component.addMouseMotionListener(mouse);
        component.addMouseWheelListener(mouse);
    }

    protected void createBufferStrategy(Canvas component) {
        component.createBufferStrategy(2);
        bs = component.getBufferStrategy();
    }

    protected void createBufferStrategy(Window window) {
        window.createBufferStrategy(2);
        bs = window.getBufferStrategy();
    }

    protected void setupViewport(int sw, int sh) {
        int w = (int) (sw * appBorderScale);
        int h = (int) (sh * appBorderScale);
        int x = (sw - w) / 2;
        int y = (sh - h) / 2;
        vw = w;
        vh = (int) (w * appWorldHeight / appWorldWidth);
        if (vh > h) {
            vw = (int) (h * appWorldWidth / appWorldHeight);
            vh = h;
        }
        vx = x + (w - vw) / 2;
        vy = y + (h - vh) / 2;
    }

    protected Matrix3x3f getViewportTransform() {
        return Utility.createViewport(appWorldWidth, appWorldHeight,
                getScreenWidth(), getScreenHeight());
    }

    protected Matrix3x3f getReverseViewportTransform() {
        return Utility.createReverseViewport(appWorldWidth, appWorldHeight,
                getScreenWidth(), getScreenHeight());
    }

    protected Vector2D getWorldMousePosition() {
        Matrix3x3f screenToWorld = getReverseViewportTransform();
        Point mousePos = mouse.getPosition();
        Vector2D screenPos = new Vector2D(mousePos.x, mousePos.y);
        return screenToWorld.mul(screenPos);
    }

    protected Vector2D getRelativeWorldMousePosition() {
        float sx = appWorldWidth / (getScreenWidth() - 1);
        float sy = appWorldHeight / (getScreenHeight() - 1);
        Matrix3x3f viewport = Matrix3x3f.scale(sx, -sy);
        Point p = mouse.getPosition();
        return viewport.mul(new Vector2D(p.x, p.y));
    }

    public void run() {
        running = true;
        initialize();
        long curTime = System.nanoTime();
        long lastTime = curTime;
        double nsPerFrame;
        while (running) {
            curTime = System.nanoTime();
            nsPerFrame = curTime - lastTime;
            gameLoop((float) (nsPerFrame / 1.0E9));
            lastTime = curTime;
        }
        terminate();
    }

    protected void initialize() {
        frameV2 = new FrameV2();
        frameV2.initial();
    }

    protected void terminate() {
    }

    /*
    游戏循环
     */
    private void gameLoop(double delta) {
        processInput(delta);
        updateObjects(delta);
        renderFrame();
        sleep(appSleep);
    }

    private void renderFrame() {
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    renderFrame(g);
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                }
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
        }
    }

    protected void processInput(double delta) {
        keyboard.poll();
        mouse.poll();
    }

    protected void updateObjects(double delta) {
    }

    protected void render(Graphics g) {
        g.setFont(appFont);
        g.setColor(appFPSColor);
        frameV2.calculateFrameRate();
        textPos = Utility.drawString(g, 20, 0, frameV2.getFrameRate());
    }

    private void disableCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "CanBeAnything";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    protected void shutDown() {
        if (Thread.currentThread() != gameThread) {
            try {
                running = false;
                gameThread.join();
                onShutDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    shutDown();
                }
            });
        }
    }

    protected void onShutDown() {
    }

    protected static void launchApp(final GameFramework app) {
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                app.shutDown();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
