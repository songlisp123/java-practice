package com.snl.data.homework.charptor03.practice01.entity.booms;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 小型弹药架
 */
public class BoomGroup implements Group {

    //弹药架
    private List<Sprite> boomQueue;

    //当前弹药夹数量
    private int boomSize;

    //弹药总量
    private int boomMaxSize;


    /**
     * 空构造函数
     */
    public BoomGroup(int size) {
        boomQueue = new ArrayList<>(size);
        this.boomSize = size;
        boomMaxSize = boomSize;
    }

    @Override
    public void render(Graphics g) {
        for (Sprite sprite : boomQueue)
            sprite.paint(g);
    }

    @Override
    public void update(double delta) {
        //空实现
    }

    /**
     * 更新子弹袋
     * @param delta 时间间隔
     * @param aGroup 敌人组
     * @param destory 摧毁组
     * @param wall 墙壁组
     * @param damage 武器伤害
     */
    public void update(double delta,Group aGroup,Group destory,Group wall,double damage) {
        if (isEmpty())
            return;
        Iterator<Sprite> iterator;
        for (iterator = boomQueue.iterator();iterator.hasNext();)
        {
            //遍历迭代器，获取子弹
            Boom next = (Boom) iterator.next();
            //更新每一个子弹
            next.update(delta,null,aGroup,destory,wall,damage);
            //如果子弹生命周期已过，移除
            if (next.isDead()) iterator.remove();
        }
    }

    @Override
    public void clear() {
        boomQueue.clear();
    }

    @Override
    public void add(Sprite sprite) {
        boomQueue.add(sprite);
        boomSize--;
    }

    @Override
    public int size() {
       return boomSize;
    }

    //重新
    @Override
    public void reset() {
        //空实现
    }

    @Override
    public boolean isEmpty() {
        return boomSize == 0;
    }

    @Override
    public void addAll(Group otherGroup) {
        //无实现
    }

    @Override
    public Collection<Sprite> getData() {
        return boomQueue;
    }

    public int getBoomMaxSize() {
        return boomMaxSize;
    }
}
