package com.snl.data.homework.charptor03.practice01.entity.booms;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.InputState;

/**
 * 定时炸弹
 */
public class TimerBoom extends Boom {

    /**
     * 炸弹 的生命周期,默认五秒
     */
    final long lifeSpan = 2_000L;
    final long start = System.currentTimeMillis();

    public TimerBoom(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this(xPos,yPos,WEIGHT,HEIGHT,Direction.EAST);
    }

    public TimerBoom(double xPos, double yPos, int WEIGHT, int HEIGHT, Direction direction) {
        this(xPos,yPos,WEIGHT,HEIGHT,direction,BoomShape.CIRCLE);
    }

    public TimerBoom(double xPos, double yPos, int WEIGHT, int HEIGHT,
                     Direction direction, double xSpeed)
    {
        super(xPos, yPos, WEIGHT, HEIGHT, direction, xSpeed);
    }

    public TimerBoom(double xPos, double yPos, int WEIGHT, int HEIGHT,
                     Direction direction, BoomShape shape)
    {
        super(xPos, yPos, WEIGHT, HEIGHT, direction, shape);
    }

    @Override
    public void update(double delta, InputState state, Group group,
                       Group destory, Group wall,double damage) {
        super.update(delta,state,group,destory,wall,damage);
        //待实现
        if (isDead())
            return;
        long now  = System.currentTimeMillis();
        if (now - start > lifeSpan) {
            //如果超出生命周期，则此精灵死亡
            System.err.println("超过时间死亡");
            setDead(true);
        }
    }
}
