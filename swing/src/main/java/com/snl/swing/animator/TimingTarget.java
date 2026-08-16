package com.snl.swing.animator;

public interface TimingTarget {
    void begin();

    void end();

    void repeat();

    /**
     * 此类的核心方法
     * @param fraction 动画经过的时间百分比
     */
    void timingEvent(double fraction);
}
