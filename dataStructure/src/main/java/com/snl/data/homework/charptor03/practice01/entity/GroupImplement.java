package com.snl.data.homework.charptor03.practice01.entity;

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
        for (T t : data)
            t.paint(g);
    }

    @Override
    public void update(double detta) {
        if (isEmpty())
            return;
        Iterator<T> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            T next = iterator.next();
            next.update(detta,null);
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
        if (otherGroup.isEmpty())
            return;
        data.addAll(otherGroup.getData());
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
