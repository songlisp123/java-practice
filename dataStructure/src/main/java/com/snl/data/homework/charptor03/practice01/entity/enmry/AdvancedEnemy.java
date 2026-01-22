package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;
import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.util.random.RandomGenerator;

public class AdvancedEnemy extends Enemy {
    //TODO 暂时不完成
    //第五章boss

    private final double MAX_Y_SPEED = 5.0;
    private final double MIN_Y_SPEED = 1.0;
    private final double MIN_X_SPEED = 1.0;
    private final double MAX_X_SPEED = 5.0;
    private final RandomGenerator generator =
            RandomGenerator.getDefault();

    private BoomGroup boomGroup;

    private long start;

    public AdvancedEnemy(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        setOriginLifePoints(100);
        setxSpeed(5);
        setySpeed(0);
        boomGroup = new BoomGroup(200);
        start = System.currentTimeMillis();
    }

    @Override
    public void update(double delta, InputState state, Group group) {
        super.update(delta, state, group);
        //发射子弹
        long now = System.currentTimeMillis();
        if (now - start >= 2_000L) {
            //进攻
            fillBooms();
            start = now;
        }
        boomGroup.update(delta,null,null,group,10.0);
    }

    private void fillBooms() {
        //计算下边界
        double right = getRight();
        double left = getLeft();
        double y = getyPos() + getHEIGHT();
        for (double i = left;i<right;i+=2) {
            double x = generator.nextDouble(-3.5, 3.5);
            Boom boom = new Boom(i,y,10,10, Direction.SOUTH,
                    BoomShape.CIRCLE, Color.RED,x,3);
            boomGroup.add(boom);
        }
    }

    @Override
    public void paint(Graphics g, InputState state) {
        super.paint(g, state);
        boomGroup.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        boomGroup.clear();
    }

    public BoomGroup getBoomGroup() {
        return boomGroup;
    }
}
