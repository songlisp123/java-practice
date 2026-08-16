package com.snl.swing.animator;

public interface TimingEventListener {

    /**
     * 回调函数用来触发特定时间时间产生的事件
     * @param timingSource 有关此类的说明，请参阅{@link  TimingSource}
     */
    void timingSourceEvent(TimingSource timingSource);
}
