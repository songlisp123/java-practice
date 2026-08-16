package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;

public abstract class CurveImplement  implements CurveContract{


    //采样点
    protected Vector2D[] mPositions;
    //采样时间
    protected float[] times;
    //曲线长度
    protected float[] mLengths;
    //曲线总长度
    protected float totalLength;
    //采样总数
    protected int count;


    @Override
    public Vector2D evaluate(float t) {
        if (getCount() < 2)
            return Vector2D.originPoint;
        //处理边界情况
        if (t < times[0])
            return mPositions[0];
        if (t >= times[count - 1])
            return mPositions[count - 1];
        return null;
    }

    @Override
    public float arcLength(float t1, float t2) {
        if (t2 <= t1)
            return 0.0F;
        if (t1 < times[0])
            t1 = 0.0F;
        if (t2 > times[count - 1])
            t2 = times[count - 1];

        int seg1,seg2;
        for (seg1 = 0; seg1 < count; ++seg1) {
            if (t1 <= times[seg1 + 1])
                break;
        }

        float u1 = (t1 - times[seg1]) / (times[seg1 + 1] - times[seg1]);
        for ( seg2 = 0; seg2 < count-1; ++seg2 )
        {
            if ( t2 <= times[seg2+1] )
            {
                break;
            }
        }
        float u2 = (t2 - times[seg2])/(times[seg2+1] - times[seg2]);

        float result;
        if (seg1 == seg2) {
            //todo
            result = SegmentArcLength(seg1,u1,u2);
        }else {
            //todo
            result = SegmentArcLength(seg1,u1,1.0F);
            for (int i = seg1 + 1; i < seg2 ; ++ i) {
                result += mLengths[i];
            }

            result += SegmentArcLength(seg2,0.0F,u2);
        }

        return result;
    }

    @Override
    public void flush() {
        int i = 0;
        for (;i < count;i++)
        {
            mPositions[i] = null;
            mLengths[i] = .0f;
            times[i] = 0.0f;
        }
        totalLength = 0.0f;
        count = 0;

        mPositions = null;
        mLengths = null;
        times = null;
    }


    public Vector2D[] getmPositions() {
        return mPositions;
    }

    public float[] getTimes() {
        return times;
    }


    public float[] getmLengths() {
        return mLengths;
    }


    public float getTotalLength() {
        return totalLength;
    }

    public int getCount() {
        return count;
    }


}
