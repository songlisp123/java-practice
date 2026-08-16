package com.snl.swing.animator;

import java.util.ArrayList;

public abstract class TimingSource {

    protected ArrayList<TimingEventListener> listeners = new ArrayList<>();

    public TimingSource() {
    }

    public abstract void start();
    public abstract void stop();

    /**
     * 这个核心方法设置动画速度一般来说，{@code resolution}的值决定动画播放速度
     * @param resolution 动画速度，越小动画播放越快
     */
    public abstract void setResolution(int resolution);

    /**
     * 设置动画播放开始延迟时间
     * @param startDelay 距离播放开始延迟时间
     */
    public abstract void setStartDelay(int startDelay);

    public final void addEventListener(TimingEventListener l) {
        synchronized (this) {
            if (!this.listeners.contains(l))
                this.listeners.add(l);
        }
    }

    public final void removeEventListener(TimingEventListener l) {
        synchronized (this)
        {
            this.listeners.remove(l);
        }
    }

    public final void timingEvent() {
        synchronized (this) {
            for (TimingEventListener l : listeners)
                l.timingSourceEvent(this);
        }
    }
}
