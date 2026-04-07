package com.snl.swing.practice01.entity;

import com.snl.swing.practice01.entity.enmry.Enemy;
import com.snl.swing.practice01.entity.goods.AbstractGoods;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GroupImplement<T extends Sprite> implements Group<T> {

    private List<T> data;

    public GroupImplement() {
        data = new ArrayList<>();
    }

    @Override
    public void render(Graphics g) {
        if (isEmpty())
            return;
//        for (T t : data)
//            t.paint(g);
        for (T datum : data) {
            datum.paint(g);
        }

    }

    @Override
    public void update(double detta) {
        if (isEmpty())
            return;
        //当使用烟雾效果的时候，会发生并发修改异常？
        Iterator<T> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            T next = iterator.next();
            next.update(detta,null);
            if(next.isDead()) iterator.remove();
        }
    }

    /**
     * 更新子弹
     * @param delta 时间间隔
     * @param walls 墙壁组
     */
    public void update(double delta,Group walls) {
        if (isEmpty())
            return;
        if (walls == null || walls.isEmpty())
            return;
        Iterator<T> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            //与墙发生碰撞,如果是敌人
            var next = iterator.next();
            if (next instanceof Enemy e)
                e.update(delta,null,walls);
            else if (next instanceof AbstractGoods g)
                g.update(delta,walls);
            if(next.isDead()) iterator.remove();
        }
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public void add(T t) {
        data.add(t);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void reset() {
        if (isEmpty())
            return;
        for (T t : data)
            t.reset();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public void addAll(Group otherGroup) {
        if(otherGroup == null)
            return;
        if (otherGroup.isEmpty())
            return;
        var groupData = otherGroup.getData();
        if (groupData == null)
            return;
        data.addAll(groupData);
        otherGroup.clear();
    }

    @Override
    public Collection getData() {
        return data;
    }

    public void remove(T t) {
        data.remove(t);
    }

}
