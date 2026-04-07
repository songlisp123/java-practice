package com.snl.swing.practice01.entity.goods;

import com.snl.swing.practice01.CONSTANTS.GameConstants;
import com.snl.swing.practice01.entity.Group;
import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

//敌人死亡后掉落的物品类
public abstract class AbstractGoods extends Sprite {
    //物品的抽象形状
    Shape shape;
    double ySpeed = GameConstants.GRAVITY * 0.25;

    public AbstractGoods(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void update(double delta, Group aGroup) {
        //判断是否与墙壁元素相互触碰
        if (aGroup == null || aGroup.isEmpty())
            return;
        Iterator<Sprite> iterator;
        Collection<Sprite> data = aGroup.getData();
        for (iterator = data.iterator();iterator.hasNext();) {
            Sprite next = iterator.next();
            if (this.isCrash(next))
            {
                //如果发生碰撞,y轴速度为零
                ySpeed = 0;
            }
        }
    }

    @Override
    public void update(double delta, InputState state) {
        throw new UnsupportedOperationException("不受支持的操作");
    }

}
