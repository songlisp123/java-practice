package com.snl.swing.animator.keyframe;

import java.util.ArrayList;

public class KeyTimes {

    private ArrayList<Double> times = new ArrayList<>();

    public KeyTimes(double ... times) {
        checkInput(times);
    }


    private void checkInput(double ... times) {
        if (times == null)
            throw new IllegalArgumentException("参数不能为null");
        if (times[0] != 0.0)
            throw new IllegalArgumentException("参数第一个值必须是0.0");
        if (times[times.length-1] != 1.0)
            throw new IllegalArgumentException("最后一个参数必须为1.0");
        double prev = 0.0;
        for (double t : times) {
            if (t < prev)
                throw new IllegalArgumentException("区间必须是单调递增的");
            this.times.add(t);
            prev = t;
        }
    }

    public ArrayList<Double> getTimes() {
        return times;
    }

    public int size() {
        return this.times.size();
    }

    public double getTime(int index) {
        if (index < 0 || index >= size())
            throw new ArrayIndexOutOfBoundsException("超出异常");
        return this.times.get(index);
    }

    //核心方法

    /**
     * 计算当前动画在哪一时间区域内
     * @param fraction 动画播放百分比
     * @return 动画的确切区间
     */
    public int getInterval(double fraction) {
        int prevIndex = 0;

        for(int i = 1; i < this.times.size(); prevIndex = i++) {
            double time = this.times.get(i);
            if (time >= fraction) {
                return prevIndex;
            }
        }

        return prevIndex;
    }

    public int getSize() {
        return this.times.size();
    }
}
