package com.snl.data.homework.charptor03.practice01.entity.booms;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BoomGroup implements Group {

    private List<Sprite> boomQueue;

    /**
     * 空构造函数
     */
    public BoomGroup(int size) {
        boomQueue = new ArrayList<>(size);
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

    public void update(double delta,Group aGroup,Group destory,Group wall) {
        if(isEmpty())
            return;
        Iterator<Sprite> iterator;
        for (iterator = boomQueue.iterator();iterator.hasNext();)
        {
            Boom next = (Boom) iterator.next();
            next.update(delta,null,aGroup,destory,wall);
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
    }

    @Override
    public int size() {
        return boomQueue.size();
    }

    @Override
    public void reset() {
        clear();
    }

    @Override
    public boolean isEmpty() {
        return boomQueue.isEmpty();
    }

    @Override
    public void addAll(Group otherGroup) {
        //无实现
    }

    @Override
    public Collection getData() {
        return null;
    }
}
