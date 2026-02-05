package com.snl.test.frame;

import com.snl.test.frame.util.Utils;
import com.snl.test.vwctor.Matrix3x3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class GameFrame extends SimpleGameFrame {

    private static final int Max_number = 1000;
    private List<Point2D> poly,polyCopy;
    private List<Star> inside,outside;
    //环绕规则，0为奇偶规则，1为非零规则
    private int winding;
    static final RandomGenerator generator = RandomGenerator.getDefault();
    Star[] stars;
    boolean selected;
    Color color;
    final Color DEFAULTColor = Color.lightGray;
    final Color SelectColor = Color.RED;

    public GameFrame() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        //初始化
        poly = new ArrayList<>(); //存放视图坐标
        polyCopy = new ArrayList<>(); //存放世界坐标
        inside = new ArrayList<>();
        outside = new ArrayList<>();
        winding = 0;//，默认奇偶规则
        //填充星星
        stars = new Star[Max_number];
        fillStars();
        color = DEFAULTColor;
        a();
    }

    private void fillStars() {
        for (int  i= 0;i<stars.length;i++) {
            Star star = new Star(
                    generator.nextDouble(WIDTH),
                    generator.nextDouble(HEIGHT),
                    2,
                    2
            );
            stars[i] = star;
        }
    }

    private void a() {
        //判断星星
        inside.clear();
        outside.clear();
        for (Star s : stars)
        {
            Point2D p = s.getLeftPoint();
            boolean b = pointInPolyGon(convertScreenPointToWorldPoint(p),
                    polyCopy,
                    winding);
            if (b)
                inside.add(s);
            else
                outside.add(s);
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //添加鼠标输入
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1))
        {
            polyCopy.add(
                    Utils.vectorCovertToPoint(getMousePointInWorldPosition())
            );
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_1))
        {
            winding = 0;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_2))
        {
            winding = 1;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //转换
        for (Star s : stars)
            s.update();

        selected = pointInPolyGon(
                Utils.vectorCovertToPoint(getMousePointInWorldPosition()),
                polyCopy,
                winding
        );

        if(selected)
        {
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
            ));
        }
        else
            setCursor(null);

        color = selected ? SelectColor : DEFAULTColor;
        a();
    }

    /**
     * 此方法比较在世界坐标系下的点是否在多边形内部
     * @param p 点
     * @param poly 多边形
     * @param winding 环绕规则
     * @return 如果点在多边形内部，返回{@code true},否则返回{@code false}
     */
    private boolean pointInPolyGon(Point2D p,List<Point2D> poly,int winding) {
        int inside = 0;
        if (poly.size() > 2)
        {
            Point2D start = poly.getLast();
            boolean startAbove = start.getY() >= p.getY();
            for (Point2D end : poly) {
                boolean endAbove = end.getY() >= p.getY();
                if (startAbove != endAbove) {
                    //处理逻辑
                    double m = (end.getY() - start.getY()) / (end.getX() - start.getX());
                    double x = start.getX() + (p.getY() - start.getY()) / m;
                    if (x >= p.getX()) {
                        if (winding == 0)
                        {
                            //奇偶规则
                            inside++;
                        } else if (winding == 1) {
                            //非零规则
                            inside += startAbove ? 1 : -1;
                        }
                    }
                }
                //否则，前进在一个点
                startAbove = endAbove;
                start = end;
            }
        }
        return inside % 2 != 0;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //绘制
        g2.setColor(Color.MAGENTA);
        g2.drawString("按下 鼠标左键 绘制多边形",30,250);
        g2.drawString("当前相交规则：%s".formatted(winding == 0?"奇偶规则":"非零规则"),30,270);
        g2.setColor(color);
        //TODO 绘制多边形
        drawPolyGon(g2);
        drawStars(g2,false);
        drawStars(g2,true);
        g2.dispose();
    }

    private void drawPolyGon(Graphics2D g2) {
        poly.clear();
        Matrix3x3f mat = getViewportTransform();
        for (Point2D p : polyCopy)
        {
            Point2D transfromed = mat.mul(p);
            poly.add(transfromed);
        }
        Utils.drawPolygonForPoint(g2,poly);
    }

    private void drawStars(Graphics2D g2,boolean in) {
        List<Star> a;
        Color c;
        if (in)
            a = inside;
        else
            a = outside;
        for (Star s : a)
        {
            if (in)
                c = new Color(255,0,0,s.alpha);
            else
                c = new Color(255,255,255,s.alpha);
            g2.setColor(c);
            g2.fill(s.shape);
        }
    }

    @Override
    protected void reset() {
        super.reset();
        polyCopy.clear();
        poly.clear();
    }

    class Star  {
        int alpha;
        Shape shape;
        Color color = new Color(255,255,255,255);
        boolean shinning;

        public Star(double x,double y,int w,int h) {
            this(x,y,w,h,generator.nextInt(180));
        }

        public Star(double x,double y,int w,int h,int alpha) {
            shape = new Ellipse2D.Double(x,y,w,h);
            this.alpha = alpha;
        }

        public void update() {
            if (shinning) {
                alpha += generator.nextInt(5);
                if (alpha >= 180) {
                    shinning = false;
                    alpha = 180;
                }
            }else {
                alpha -= generator.nextInt(5);
                if (alpha <= 50)
                {
                    shinning = true;
                    alpha = 50;
                }
            }
            color = new Color(255,255,255,alpha);
        }

        public Point2D getLeftPoint() {
            return shape.getBounds().getLocation();
        }
    }

    public static void main(String[] args) {
        launchGame(new GameFrame());
    }
}
