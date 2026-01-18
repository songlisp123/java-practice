package com.snl.data.homework.charptor03.practice01.article;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.random.RandomGenerator;

public abstract class Smoke extends Sprite {

    //对于烟雾类，需要x，y轴的速度
    private double xSpeed;

    private final double MAX_XSPEED = 3;
    //Y轴速度
    private double ySpeed;
    private final double MAX_YSPEED = 5.0;
    private final double MIN_YSPEED = 1.0;
    //当前烟雾的颜色
    private Color color;
    //初始化颜色
    private final Color DEFAUL_COLOR = Color.RED;
    //生命周期,这是一个随机数
    private long lifeSpan;
    //烟雾粒子最大生命周期
    private final long MAX_LIFE_COUNT = 1000L;
    //随机化烟雾器
    private final RandomGenerator generator =
            RandomGenerator.getDefault();
    //烟雾粒子alpha值
    private float alpha;

    private RectangularShape rectangularShape;

    /**
     * 创建时间
     */
    private long createTime;

    private int originHeight;
    private int originWeight;

    private BoomShape shape;

    private final double DAMPING = 0.96;

    public Smoke() {
        initData();
    }

    public Smoke(double xPos, double yPos, int WEIGHT, int HEIGHT,Color color) {
        this(xPos,yPos,WEIGHT,HEIGHT,color,BoomShape.CIRCLE);
    }

    public Smoke(double xPos, double yPos, int WEIGHT, int HEIGHT, Color color, BoomShape shape) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.color = color;
        this.shape = shape;
        this.originHeight = HEIGHT;
        this.originWeight = WEIGHT;
        initData();
    }

    private void initData() {
        //初始化烟雾粒子生命周期
        lifeSpan = generator.nextLong(MAX_LIFE_COUNT / 2,MAX_LIFE_COUNT);
        //初始化alpha管道
        alpha = 1.0f;
        //更新创建时间
        createTime = System.currentTimeMillis();
        //判断颜色
        if (color == null)
            color = DEFAUL_COLOR;
        //初始化x轴速度
        xSpeed = generator.nextDouble(-MAX_XSPEED,MAX_XSPEED);
        //初始化y轴速度
        ySpeed = generator.nextDouble(MIN_YSPEED,MAX_YSPEED);
        color = (color == null) ? Color.lightGray : color;
        //判断形状
        switch (shape) {
            case CIRCLE -> rectangularShape = new Ellipse2D.Double(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
            case RECT -> rectangularShape = new Rectangle2D.Double(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
        }
    }

    @Override
    public void update(double delta, InputState state) {
        long now = System.currentTimeMillis();
        //判断是否死亡
        if (now - createTime >= lifeSpan || alpha <= 0)
            //如果烟雾超过给定的生命周期那么就失败
            //获取烟雾的alpha值小于等于0，那么也死亡
        {
            setDead(true);
        }
        else {
            //如果都没有
            alpha -= (float)Math.pow(1.0 - alpha, 2.0);
            if (alpha < 0)
                alpha = 0;
            /**
             * 以下代码为 ai 创作
             */
            //空气阻尼优化
            xSpeed *= DAMPING;
            ySpeed *= DAMPING;
        }
        //x轴移动
        moveX();
        //移动y轴
        moveY();
        //改变形状
        rectangularShape.setFrame(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
    }

    private void moveY() {
        double y = getyPos();
        setyPos(y - ySpeed);
//        ySpeed -= Math.min(Math.random(),0.1) * ySpeed;
    }

    private void moveX() {
        double x = getxPos();
        setxPos(x + xSpeed);
//        xSpeed -= Math.min(Math.random() + 0.25,0.5) * xSpeed;
    }

    @Override
    public void paint(Graphics g) {
        this.paint(g,null);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        //空实现
        //为什么没有更新？
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(
                color.getRed(),color.getGreen(),
                color.getBlue(),(int)(alpha * 255)));
        g2.fill(rectangularShape);
        g2.dispose();
    }

    public long getMAX_LIFE_COUNT() {
        return MAX_LIFE_COUNT;
    }

    public Color getColor() {
        return color;
    }
}
