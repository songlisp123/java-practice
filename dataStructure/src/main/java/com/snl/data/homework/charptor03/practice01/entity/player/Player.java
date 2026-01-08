package com.snl.data.homework.charptor03.practice01.entity.player;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;

public class Player extends Sprite implements PlayerAction {

    private Color color;
    private Point2D leftUpConor;
    private Point2D rightUpConor;
    private Point2D leftDownConor;
    private Point2D rightDownConor;
    private final double SPEED = 3.5;

    private final int boomCounts = 5;
    private BoomGroup group;

    public Player() {
        super();
        initData();
    }

    public Player(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        initData();
    }

    private void initData() {
        color = Color.CYAN;
        group = new BoomGroup(boomCounts);
        resetPoint();
    }

    @Override
    public void update(double delta, InputState state) {
        if (state.up)
            move(0,-SPEED);
        if (state.down)
            move(0,SPEED);
        if (state.left)
            move(-SPEED,0);
        if (state.right)
            move(SPEED,0);
        if (state.attackPressed) {
            shoot(group,state);
        }
    }

    public void update(double delta,InputState state,Group aGroup,Group destory) {
        this.update(delta,state);
        changePosition(state);
        group.update(delta,aGroup,destory);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        float radius = 150;
        float[] dist = {0.0f,1.0f};
        Color[] colors = {
                Color.WHITE,Color.BLACK
        };
        RadialGradientPaint paint = new RadialGradientPaint(leftUpConor, radius, dist, colors);
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,0.65f
        ));
        drawLine(g2);
        g2.setPaint(paint);
        g2.fillOval(0,0,200,200);
        g2.fillOval(300,250,200,200);
        g2.fillOval(125,400,60,80);
        g2.fillOval(400,50,100,100);
        //绘制子弹
        group.render(g);
        g2.dispose();
    }

    private void drawLine(Graphics2D g2) {
        //硬编码
        //顺时针画图
        g2.setColor(Color.green);
        g2.drawLine((int) leftUpConor.getX(), (int) leftUpConor.getY(),
                (int) rightUpConor.getX(), (int) rightUpConor.getY());
        g2.drawLine((int) rightUpConor.getX(), (int) rightUpConor.getY(),
                (int) rightDownConor.getX(), (int) rightDownConor.getY());
        g2.drawLine((int) rightDownConor.getX(), (int) rightDownConor.getY(),
                (int) leftDownConor.getX(), (int) leftDownConor.getY());
        g2.drawLine((int) leftDownConor.getX(), (int) leftDownConor.getY(),
                (int) leftUpConor.getX(), (int) leftUpConor.getY());
    }

    private void changePosition(InputState state) {
        double x = getxPos();
        double y = getyPos();
        if (state.left) {
            //左按键
//            point = new Point2D.Double()
            leftUpConor = new Point2D.Double(x - 2.5,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() - 2.5,y);
        }else {
            leftUpConor = new Point2D.Double(x,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() ,y);
        }
        if (state.right) {
            leftUpConor = new Point2D.Double(x + 2.5,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() + 2.5,y);
        }
    }

    @Override
    public void reset() {
        super.reset();
        resetPoint();
        //重置弹药
        group.reset();
    }

    @Override
    public void move(double xPos, double yPos) {
        double x = getxPos() + xPos;
        double y = getyPos() + yPos;
        super.setxPos(x);
        super.setyPos(y);
        resetPoint(x,y);
    }

    @Override
    public void handleTouchWall(int weight, int height) {
        if (touchLeftBounder())
            setxPos(Math.max(0,getxPos()));
        if (touchUpBounder())
            setyPos(Math.max(0,getyPos()));
        if (touchRightBounder(weight))
            setxPos(Math.min(weight - getWEIGHT(),getxPos()));
        if (touchBottomBounder(height))
            setyPos(Math.min(height - getHEIGHT(),getyPos()));
        resetPoint();
    }

    public boolean isCrash(Collection<? extends Sprite> collection) {
        for (Sprite sprite : collection)
        {
            if (super.isCrash(sprite)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Sprite> T eat(Collection<T> collection) {
        for (T sprite : collection)
        {
            if (super.isCrash(sprite)) {
                sprite.setDead(true);
                return sprite;
            }
        }
        return null;
    }

    private boolean touchLeftBounder() {
        return getxPos() <= 0;
    }

    private boolean touchRightBounder(int weight) {
        return (getxPos()+getWEIGHT()) >= weight;
    }

    private boolean touchUpBounder() {
        return getyPos() <= 0;
    }

    private boolean touchBottomBounder(int height) {
        return (getyPos()+getHEIGHT()) >= height;
    }

    private void resetPoint() {
        double x = getxPos();
        double y = getyPos();
        resetPoint(x,y);
    }

    private void resetPoint(double x,double y) {
        leftUpConor = new Point2D.Double(x,y);
        leftDownConor = new Point2D.Double(x,y + getHEIGHT()); //左下角
        rightUpConor = new Point2D.Double(x + getWEIGHT(),y); //右上角
        rightDownConor = new Point2D.Double(x+getWEIGHT() , y + getHEIGHT()); //右下角
    }

    @Override
    public void shoot(BoomGroup group,InputState state) {
        if (group.size() >= boomCounts)
            return;
        group.add(createBoom(state));
//        Music.shoot();
    }

    private Sprite createBoom(InputState state) {
        Boom boom;
        switch (state.direction) {
            case NORTH -> boom = new Boom(getxPos() + getWEIGHT() / 2.0 - 5
                    ,getyPos() + 10,
                    10,10, state.direction, BoomShape.CIRCLE);
            case SOUTH -> boom = new Boom(getxPos() + getWEIGHT() / 2.0 - 3
                    ,getyPos() + getHEIGHT(),
                    6,10, state.direction);
            case WEST -> boom = new Boom(getxPos()
                    ,getyPos() + getHEIGHT() / 2.0 - 3,
                    10,6, state.direction);
            default -> boom = new Boom(getxPos() + getWEIGHT()
                    ,getyPos() + getHEIGHT() / 2.0 - 5,
                    10,10, state.direction,BoomShape.CIRCLE);
        }
        return boom;
    }
}
