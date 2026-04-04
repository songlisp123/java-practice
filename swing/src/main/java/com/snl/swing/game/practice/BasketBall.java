package com.snl.swing.game.practice;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasketBall extends DiKaErPlus {

    AABB base;
    Vector2D spos,epos,copy;
    double r;
    double rot,theta;
    boolean shot;
    AABB player;
    boolean leftMoving,rightMoving;
    Vector2D speed;


    public final double F = 10;
    public final double GRAVITY = 9.98;

    AABB basketBase,basketUp;
    List<Ball> balls = new ArrayList<>();

    public BasketBall() throws HeadlessException {
        drawAxis = false;
        wordWidth = 20;
        wordHeight = 20;
        WIDTH = 750;
        HEIGHT = 750;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        viewMat = Matrix3x3f.translate(0,-wordHeight / 2.0);
        base = new AABB(
                new Vector2D(-wordWidth/2.0,0),new Vector2D(wordWidth/2.0,0.25)
        );
        basketBase = new AABB(
                new Vector2D(wordWidth/2.0-0.25,0),new Vector2D(wordWidth/2.0,4)
        );
        basketUp = new AABB(
                new Vector2D(wordWidth/2.0-0.5,4),new Vector2D(wordWidth/2.0-0.25,7)
        );
        rot = 0;
        theta = Math.PI / 4;
        r = 1;
        copy = new Vector2D();
        player = new AABB(
                new Vector2D(0,0.25),new Vector2D(0.5,0.75)
        );
        speed = new Vector2D(2,0);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_UP)) {
            rot += theta * delta;
        }
        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN))
            rot -= theta * delta;
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            shot = true;
        leftMoving = keyBoardEvent.keyDown(KeyEvent.VK_A);
        rightMoving = keyBoardEvent.keyDown(KeyEvent.VK_D);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        spos = getMousePointInVector();
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        rotate = rotate.mul(Matrix3x3f.translate(r,0));
        epos = rotate.mul(copy).add(spos);
        if (shot) {
            Ball ball = new Ball(spos, 0.125);
            double x = F * Math.cos(rot);
            double y = F * Math.sin(rot);
            ball.setSpeed(new Vector2D(x,y));
            ball.setOffset(new Vector2D());
            balls.add(ball);
        }

        updatePlayer(delta);

        shot = false;
    }

    private void updatePlayer(double delta) {
        //更新玩家
        if (leftMoving) {
            player.translate(speed.mul(-delta));
        }
        if (rightMoving)
            player.translate(speed.mul(delta));

        for (Ball ball : balls) {
            if (player.collisionCircle(ball.pos,ball.r))
            {
                System.out.println("碰撞");
            }
        }
    }

    @Override
    protected void animation(double delta) {
        if (balls.isEmpty())
            return;
        Iterator<Ball> iterator = balls.iterator();
        while (iterator.hasNext())
        {
            Ball b = iterator.next();
            Vector2D start = b.pos;
            b.animation(delta);
            Vector2D end = b.pos;
            Vector2D totalMove = end.sub(start);
            //TODO
            Circle circle = new Circle(b.r,end);
            if (moving_circle_rectangle_collide(circle,totalMove,base)) {
                b.speed.setY(-b.speed.getY());
            }
            if (moving_circle_rectangle_collide(circle,totalMove,basketBase) ||
                moving_circle_rectangle_collide(circle,totalMove,basketUp))
            {
                b.speed.setX(-b.speed.getX());
            }
            if (b.pos.getY() <= 0)
                iterator.remove();
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.ORANGE);
        drawAAbb(g2,base,true);

        drawCircle(g2,spos,.1,true);
        drawCircle(g2,epos,.1,true);

        Stroke stroke = g2.getStroke();
        g2.setStroke(
                new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                        new float[]{3,5,3},2)
        );
        drawCircle(g2,spos,r,false);
        drawLine(g2,spos,epos);
        g2.setStroke(stroke);
        g2.setColor(Color.WHITE);
        drawAAbb(g2,basketBase,true);
        drawAAbb(g2,basketUp,true);
        drawAAbb(g2,player,false);
        for (Ball ball : balls) {
            drawCircle(g2, ball.pos,0.02, false);
            if (ball.olds.size() < 2)
                continue;
            Vector2D first = ball.olds.getFirst();
            for (int i=0;i<ball.olds.size();i++)
            {
                Vector2D v2d = ball.olds.get(i);
                drawLine(g2,first,v2d);
                first = v2d;
            }
        }
        g2.dispose();
    }

    /**
     * 这是一个二分算法，来自2d测试第8章，这个算法不算优雅，但是能行
     * @param ball 包络圆
     * @param move 前进向量
     * @param aabb aabb矩形
     * @return 如果相撞，返回{@code true}，否则返回{@code false}
     * @implNote 这个方法采用递归调用的方法，每次将问题分解为一小步，
     * 注意，这个方法也可以使用包裹矩形，目前使用前进的距离加上当前的距离采取包裹圆
     * 来源自：2d碰撞测试 书籍
     */
    public boolean moving_circle_rectangle_collide(Circle ball, Vector2D move, AABB aabb) {
        Circle envelope = new Circle(ball.r,ball.center.clone());
        Vector2D halfMove = move.div(2);
        double distance = move.len();
        envelope.center = ball.center.add(halfMove);
        envelope.r = ball.r + distance / 2.0;
        if (aabb.collisionCircle(envelope.center,envelope.r)) {
            double epsilon = 1.0 / 32.0;
            double minim = Math.max(ball.r/4.0,epsilon);
            if (distance < minim)
                return aabb.collisionCircle(ball.center,ball.r);
            envelope.r = ball.r;
            return moving_circle_rectangle_collide(ball,halfMove,aabb) ||
                    moving_circle_rectangle_collide(envelope,halfMove,aabb);
        }else {
            return false;
        }
    }

    @Override
    protected void reset() {
        super.reset();
    }

    public static void main(String[] args) {
        launchGame(new BasketBall());
    }
}
