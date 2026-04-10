package com.snl.swing.game.colliison;

import com.snl.swing.game.math.contract.Convex;

public class Fixture {

protected final Convex convex;

protected Filter filter;
protected boolean sensor;
protected Object userData;

    public Fixture(Convex convex) {
        if (convex == null)
            throw new IllegalArgumentException("参数为null");
        this.convex = convex;
        filter = Filter.DEFAULT_FILTER;
        this.sensor = false;
    }

    public Convex getConvex() {
        return convex;
    }

    public Filter getFilter() {
        return filter;
    }

    public boolean isSensor() {
        return sensor;
    }

    public Object getUserData() {
        return userData;
    }

    public void setFilter(Filter filter) {
        if (filter == null)
            throw new IllegalArgumentException("参数错误");
        this.filter = filter;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
