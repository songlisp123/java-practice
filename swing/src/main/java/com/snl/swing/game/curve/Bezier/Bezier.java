package com.snl.swing.game.curve.Bezier;

import com.snl.swing.game.curve.CurveContract;
import com.snl.swing.game.math.MyMath;
import com.snl.swing.game.math.Vector2D;



public class Bezier implements CurveContract {

   private Vector2D[] mPositions; //暂时实现【0,1】区间采样

    public boolean  initialed( final Vector2D[] mPositions) {
        this.mPositions = new Vector2D[mPositions.length];
        System.arraycopy(mPositions,0,this.mPositions,0,mPositions.length);
        return true;
    }

    /**
     * 已废弃
     * @param base 贝塞尔曲线阶
     * @param t 参数
     * @param min 映射范围
     * @param max 银蛇范围
     * @return B(4,base)基函数函数值
     */
    @Deprecated(since = "2026年8月18日23:45:14")
    public static float bezier_base(int base,float t,float min,float max) {
        float offset = max - min;
        t = (t - min) / offset;
        float invt = 1 - t;
        float result = 1;
        switch (base) {
            case 0 -> result = invt * invt * invt * invt;
            case 1 -> result = 4.0f * t * invt * invt * invt;
            case 2 -> result = 6.0f * t * t  * invt * invt;
            case 3 -> result = 4.0f * t * t * t * invt;
            case 4 -> result = t * t * t * t;
        }

        return result * offset;
    }


    @Deprecated(since = "2026年8月18日23:47:16")
    public static Vector2D cub_bezier(Vector2D p0,Vector2D p1,Vector2D p2,Vector2D p3,float t) {
        float b30 = bezier_base(4, 0, t, 0.0f, 1.0f);
        float b31 = bezier_base(4, 1, t, 0.0f, 1.0f);
        float b32 = bezier_base(4, 2, t, 0.0f, 1.0f);
        float b33 = bezier_base(4, 3, t, 0.0f, 1.0f);
        //TODO
        Vector2D temp = new Vector2D();
        temp.x = p0.x * b30 + p1.x * b31 + p2.x * b32 + p3.x * b33;
        temp.y = p0.y * b30 + p1.y * b31 + p2.y * b32 + p3.y * b33;
        return temp;
    }


    /**
     * 废弃
     * @param nPoints
     * @param base
     * @param t
     * @param min
     * @param max
     * @return
     */
    @Deprecated(since =  "2026年8月18日23:47:56")
    public static float bezier_base(int nPoints, int base, float t, float min, float max) {
        if (base >= nPoints)
            throw new IllegalArgumentException("参数选择错误！");
        
        float offset = max - min;
        t = (t - min) / offset;
        float invt = 1 - t;
        float result = 1,factor = 0;

        int n = nPoints - 1;

        if (base == 0 || base == n)
            factor = 1.0f;
        else {
            factor = MyMath.erChi(base,n);
        }

        result *= factor;

        for (int i = 1; i <= base; i++)
            result *=  t;
        for (int i = 1; i <= n - base; i++)
            result *=  invt;

        return result * offset;
    }


    @Deprecated
    public static Vector2D bezier_curve(Vector2D[] vector2DS, float t,int n) {
        if (vector2DS.length < 3)
            throw new RuntimeException();
        int length = vector2DS.length;
        if (length < n)
            throw new RuntimeException();

        switch (n) {
            case 4 -> {
                return cub_bezier(vector2DS[0],vector2DS[1],vector2DS[2],vector2DS[3],t);
            }

            case 3 -> {
                return bio_bezier(vector2DS[0],vector2DS[1],vector2DS[2],t);
            }

            default -> {
                return qua_bezier(vector2DS[0],vector2DS[1],vector2DS[2],vector2DS[3],vector2DS[4],t);
            }
        }

    }

    @Deprecated
    private static Vector2D bio_bezier(Vector2D p0, Vector2D p1, Vector2D p2, float t) {
        float b20 = Bezier.bezier_base(3, 0, t, 0.0f, 1.0f);
        float b21 = Bezier.bezier_base(3, 1, t, 0.0f, 1.0f);
        float b22 = Bezier.bezier_base(3, 2, t, 0.0f, 1.0f);

        Vector2D temp = new Vector2D();
        temp.x =  p0.x * b20 + p1.x * b21 + p2.x * b22;
        temp.y =  p0.y * b20 + p1.y * b21 + p2.y * b22;

        return temp;
    }

    @Deprecated
    private static Vector2D qua_bezier(Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3, Vector2D p4, float t) {
        float b40 = bezier_base(5, 0, t, 0.0f, 1.0f);
        float b41 = bezier_base(5, 1, t, 0.0f, 1.0f);
        float b42 = bezier_base(5, 2, t, 0.0f, 1.0f);
        float b43 = bezier_base(5, 3, t, 0.0f, 1.0f);
        float b44 = bezier_base(5, 4, t, 0.0f, 1.0f);
        //TODO
        Vector2D temp = new Vector2D();
        temp.x = p0.x * b40 + p1.x * b41 + p2.x * b42 + p3.x * b43 + p4.x * b44;
        temp.y = p0.y * b40 + p1.y * b41 + p2.y * b42 + p3.y * b43 + p4.y * b44;
        return temp;
    }

    @Deprecated
    public static Vector2D  evaluate(Vector2D[] positions,float t,int offset,int count) {
        int len = positions.length;
        if (len < 3)
            throw new IllegalArgumentException("参数长度必须大于等于3");
        if (count <= 0)
            throw new IllegalArgumentException("参数不能为负数");
        if (offset <= 0)
            offset = 0;
        if (len < offset + count)
            throw new IllegalArgumentException("参数异常，请重试");

        switch (count) {
            case 3 -> {
                return bio_bezier(positions[offset],positions[offset + 1],positions[offset + 2],t);
            }
            case  4 -> {
                return cub_bezier(positions[offset],positions[offset + 1],positions[offset + 2],positions[offset + 3],t);
            }

            default -> {
                return qua_bezier(positions[offset],positions[offset + 1],positions[offset + 2],positions[offset + 3],positions[offset + 4],t);
            }
        }
    }

    @Override
    public Vector2D evaluate(float t) {
        return BezierHelp.evaluate(t,mPositions);
    }

    @Override
    public float arcLength(float t1, float t2) {
        return 0;
    }

    @Override
    public float SegmentArcLength(int segment, float u1, float u2) {
        return 0;
    }

    @Override
    public Vector2D derivative(float t) {
        return BezierHelp.derivative(mPositions,t, mPositions.length - 1);
    }

    @Override
    public Vector2D second_derivative(float t) {
        return BezierHelp.second_derivative(mPositions,t, mPositions.length - 1);
    }

    @Override
    public void flush() {

    }
}
