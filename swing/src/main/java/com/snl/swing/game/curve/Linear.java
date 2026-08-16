package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;

public class Linear {
    private Vector2D[] sample_points;
    private float[] times;
    private int count; //区间长度

    //无参构造器
    public Linear() {
    }

    public boolean Initialize(final Vector2D[] sample_points,final  float[] times,int count) {
        if (this.count != 0)
            return false;
        if (count < 2 || sample_points.length < 2 || times.length < 2)
            return false;

        //设置数组
        this.sample_points = new Vector2D[count];
        this.times = new float[count];
        this.count = count;

        //复制数组
        System.arraycopy(sample_points,0,this.sample_points,0,count);
        System.arraycopy(times,0,this.times,0,count);
        return true;
    }


    public Vector2D Evaluate(float time) {
        if (count < 2)
            return Vector2D.originPoint;

        //处理边界情况
        if (time <= times[0])
            return sample_points[0];
        if (time >= times[count - 1])
            return sample_points[count - 1];

        //寻找片段
        int i;
        for ( i = 0; i < count-1; ++i )
        {
            if ( time < times[i+1] )
            {
                break;
            }
        }

        float t0 = times[i];
        float t1 = times[i + 1];
        float u = (time - t0) / (t1 - t0);
        return sample_points[i].scale( 1- u)
                .add(sample_points[i + 1].scale(u));
    }


    public Vector2D getSamplePoint(int index) {
        return sample_points[index];
    }


    public float getTime(int index) {
        return times[index];
    }

    public Vector2D[] getSample_points() {
        return sample_points;
    }

    public float[] getTimes() {
        return times;
    }

    public int getCount() {
        return count;
    }
}
