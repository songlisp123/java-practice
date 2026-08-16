package com.snl.swing.animator.keyframe;

import com.snl.swing.animator.interpolator.Interpolator;

public class KeyFrames {

    private KeyValues keyValues;
    private KeyTimes keyTimes;
    private KeyInterpolators interpolators;

    public KeyFrames(KeyValues keyValues) {
        this.init(keyValues, null, (Interpolator)null);
    }

    public KeyFrames(KeyValues keyValues, KeyTimes keyTimes) {
        this.init(keyValues, keyTimes, (Interpolator)null);
    }

    public KeyFrames(KeyValues keyValues, KeyTimes keyTimes, Interpolator... interpolators) {
        this.init(keyValues, keyTimes, interpolators);
    }

    public KeyFrames(KeyValues keyValues, Interpolator... interpolators) {
        this.init(keyValues, null, interpolators);
    }

    private void init(KeyValues keyValues, KeyTimes keyTimes, Interpolator... interpolators) {
        //获取帧数量
        int numFrames = keyValues.getSize();
        if (keyTimes == null) {
            //如果不提供时间，我们平均整个时间区间
            double[] keyTimesArray = new double[numFrames];
            double timeVal = 0.0F;
            keyTimesArray[0] = timeVal;

            //平均时间区间
            for(int i = 1; i < numFrames - 1; ++i) {
                timeVal += 1.0F / (double)(numFrames - 1);
                keyTimesArray[i] = timeVal;
            }

            keyTimesArray[numFrames - 1] = 1.0F;
            this.keyTimes = new KeyTimes(keyTimesArray);
        } else {
            this.keyTimes = keyTimes;
        }

        this.keyValues = keyValues;
        if (numFrames != this.keyTimes.getSize()) {
            throw new IllegalArgumentException("请确保时间与值成一个数对");
        } else if (interpolators != null && interpolators.length != numFrames - 1 && interpolators.length != 1) {
            throw new IllegalArgumentException("要么提供null插值器，要么提供一个插值器，或者提供与"+(numFrames-1)+"的插值器,如果都不，" +
                    "则抛出异常");
        } else {
            this.interpolators = new KeyInterpolators(numFrames - 1, interpolators);
        }
    }

    public Class<?> getType() {
        return this.keyValues.getType();
    }

    public KeyValues getKeyValues() {
        return this.keyValues;
    }

     public KeyTimes getKeyTimes() {
        return this.keyTimes;
    }

    //当期动画处于哪一个区间？？
    public int getInterval(double fraction) {
        return this.keyTimes.getInterval(fraction);
    }

    //核心算法，其实委托给keyValues计算
    Object getValue(double fraction) {
        int interval = this.getInterval(fraction);
        double t0 = this.keyTimes.getTime(interval);
        double t1 = this.keyTimes.getTime(interval + 1);
        //区间归一化
        double t = (fraction - t0) / (t1 - t0);
        //找到属于该区间的插值器插值
        double interpolatedT = this.interpolators.interpolate(interval, t);
        if (interpolatedT < 0.0F) {
            interpolatedT = 0.0F;
        } else if (interpolatedT > 1.0F) {
            interpolatedT = 1.0F;
        }

        return this.keyValues.getValue(interval, interval + 1, interpolatedT);
    }


}
