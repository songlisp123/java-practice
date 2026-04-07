package com.snl.swing.game.practice;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.*;
import com.snl.swing.game.math.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BallInAABB extends DiKaErPlus {

    List<Ball> balls = new ArrayList<>();
    AABB aabb;
    Polygon polygon;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        Ball ball = new Ball(new Vector2D(),0.15);
        balls.add(ball);
        ball = new Ball(new Vector2D(2,3),0.15);
        balls.add(ball);
        ball = new Ball(new Vector2D(-2,4.5),0.15);
        balls.add(ball);
        aabb = new AABB(
                new Vector2D(-1,-1),new Vector2D(1,1)
        );

        aabb.extend(5);

        polygon = new Polygon(
                new Vector2D[]{
                        new Vector2D(-0.5,0.5),new Vector2D(0.5,.5),
                        new Vector2D(0.5,-0.5),new Vector2D(-0.5,-0.5)
                }
        );


        polygon.scale(10);


    }


    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.ORANGE);

//        drawAAbb(g2,aabb,false);

        drawPolyGon(g2,polygon,false);

        for (Ball ball : balls) {
            drawCircle(g2,ball.pos,ball.r,true);
        }
    }

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (balls.isEmpty())
            return;
        for (Ball ball : balls) {
            if (ball.isShowMoving())
                ball.animation(delta);
            SegMent[] edge = polygon.getEdge();
            for (SegMent segMent : edge) {
                if (segMent.collideCircleInBoolean(ball.pos,ball.r)) {
                    //TODO
                    ball.setShowMoving(false);
                    ball.setSpeed(new Vector2D());
                    double collideDistance = segMent.getCollideDepth(ball.pos, ball.r);
                    ball.pos.y += collideDistance;
//                    //逐像素上移
//                    boolean collision = true;
//                    while (collision) {
//                        //碰撞发生时
//                        ball.pos.y += (double) wordHeight / (double) HEIGHT;
//                        if (!segMent.collideCircleInBoolean(ball.pos,ball.r))
//                            collision = false;
//                    }

                }
            }
        }

    }

    public static void main(String[] args) {
        launchGame(new BallInAABB());
    }
}
