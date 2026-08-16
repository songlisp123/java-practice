package com.snl.swing.game.curve;

import com.snl.swing.game.math.MyMath;
import com.snl.swing.game.math.Vector2D;

import java.util.Arrays;

public class HermiteCurve extends CurveImplement {

    protected Vector2D[] inTangents;
    protected Vector2D[] outTangents;


    public HermiteCurve() {
    }

    /**
     *
     * @param positions
     * @param inTangents
     * @param outTangents
     * @param times
     * @param count
     * @return
     */
    public boolean initialize(final Vector2D[] positions,final Vector2D[] inTangents,
                              final Vector2D[] outTangents,final float[] times,
                              int count) {
        if (getCount() != 0)
            return false;

        super.mPositions = new Vector2D[count];
        this.inTangents = new Vector2D[count - 1];
        this.outTangents = new Vector2D[count - 1];
        super.times = new float[count];
        super.count = count;

        //复制数据
        int i;
        for (i = 0; i < count; ++i) {
            mPositions[i] = positions[i];
            if (i < count - 1) {
                this.inTangents[i] = inTangents[i];
                this.outTangents[i] = outTangents[i];
            }
            super.times[i] = times[i];
        }

        //设置曲线片段
        mLengths = new float[count - 1];
        totalLength = 0.0F;

        for (i = 0; i< count-1;++i) {
            mLengths[i] = SegmentArcLength(i,0.0F,1.0F); //TODO
            totalLength += mLengths[i];
        }

        return true;
    }

    /**
     * 初始化加紧样条
     * @param sample_points 采样点
     * @param times 时间
     * @param count 采样总数
     * @param in 入射向量
     * @param out 出射向量
     * @return {@code false},样条创建失败
     * @implNote 请参阅 {@code 游戏基础数学：可视化交互}第九章
     * @since 2026年8月16日16:12:26
     */
    public boolean initializeClamped(Vector2D[] sample_points,float[] times,int count,final Vector2D in,Vector2D out) {
        if (getCount() != 0)
            return false;
        int n = count;
        float[] A = new float[n * n];
        Arrays.fill(A,0);
        A[0] = 1.0f;
        int i;
        for (i = 1; i < n -1; i++) {
            A[i + n * i - n] = 1.0f;
            A[i + n * i] = 4.0f;
            A[i + n * i + n] = 1.0f;
        }
        A[n * n - 1] = 1.0f;

        //对它进行求逆，我们可能会得到更好的精度如果我们对线性系统解3次，//对x， y和z各解一次，但这样更有效率
        if (!MyMath.invertMatrix(A,n)) {
            A = null;
            return false;
        }

        //复制数组
        super.mPositions = new Vector2D[count];
        this.inTangents = new Vector2D[count - 1];
        this.outTangents = new Vector2D[count - 1];
        super.times = new float[count];
        super.count = count;

        //处理边界情况
        mPositions[0] = sample_points[0];
        super.times[0] = times[0];
        outTangents[0] = out;

        mPositions[n - 1] = sample_points[ n- 1];
        super.times[n - 1] = times[n - 1];
        inTangents[n - 2] = in;

        for (i = 1; i < count - 1; i++) {
            mPositions[i] = sample_points[i];
            super.times[i] = times[i];

            //b乘以A的逆就得到x
            outTangents[i] = out.scale(A[i]).add(in.scale(A[i + n * n - n]));

            for (int  j = 1; j < n -1; j++) {
                Vector2D b_j = sample_points[j + 1].sub(sample_points[ j - 1]).scale(3.0f);
                outTangents[i] = outTangents[i].add(b_j.scale( A[i + n*j]));
            }

            inTangents[ i - 1]  = outTangents[i];

        }

        //设置曲线段长度
        mLengths = new float[count-1];
        totalLength = 0.0f;
        for ( i = 0; i < count-1; ++i )
        {
            mLengths[i] = SegmentArcLength(i, 0.0f, 1.0f);
            totalLength += mLengths[i];
        }
        return true;
    }


    /**
     *
     * @param sample_positions
     * @param times
     * @param count
     * @return
     * @implNote 请参阅 {@code 游戏基础数学：可视化交互}第九章
     * @since 2026年8月16日16:12:26
     */
    public boolean initializeNatural(final Vector2D[] sample_positions,final float[] times,int count) {
        // make sure not already initialized
        if (getCount() != 0)
            return false;
        
        //复制数组
        mPositions = new Vector2D[count];
        inTangents = new Vector2D[count-1];
        outTangents = new Vector2D[count-1];
        super.times = new float[count];
        super.count = count;


        //复制数组
        for ( int i = 0; i < count; ++i )
        {
            mPositions[i] = sample_positions[i];
            super.times[i] = times[i];
        }

        //构造切线数据
        int n = count;
        float L;                          // 对角矩阵入口
        float[] U = new float[n];          // 上对角三角形项目
        Vector2D[] z = new Vector2D[n];  // 下对角系统Lz = b的解

        //求上矩阵和部分解z
        L = 2.0f;
        U[0] = 0.5f;
        z[0] = sample_positions[1].sub(sample_positions[0]).scale(3.0f).div(L);
        for ( int i = 1; i < n-1; ++i )
        {
            // add internal entry to linear system for smooth spline
            L = 4.0f - U[i-1];
            U[i] = 1.0f/L;
            z[i] = sample_positions[i + 1].sub(sample_positions[i - 1]).scale(3.f);
            z[i] = z[i].sub(z[i-1]);
            z[i] = z[i].div(L);
        }
        L = 2.0f - U[n-2];
        z[n-1] = sample_positions[n-1].sub(sample_positions[n-2]).scale(3.0f);
        z[n-1] = z[n - 1].sub(z[n-2]);
        z[n - 1] = z[n-1].div(L);

        // 求解Ux = z（详见Burden and Faires）
        inTangents[n-2] = z[n-1];
        for ( int i = n-2; i > 0; --i )
        {
            inTangents[i-1] = z[i] .sub( inTangents[i].scale(U[i]));
            outTangents[i] = inTangents[i-1];
        }
        outTangents[0] = z[0] .sub( inTangents[0].scale(U[0]));

        // 设置曲线段长度
        mLengths = new float[count-1];
        totalLength = 0.0f;
        for ( int i = 0; i < count-1; ++i )
        {
            mLengths[i] = SegmentArcLength(i, 0.0f, 1.0f);
            totalLength += mLengths[i];
        }

        return true;
    }


    /**
     * 这是个什么鬼？？？
     * @param i
     * @param u1
     * @param u2
     * @return
     */
    public float SegmentArcLength(int i,float u1,float u2) {


        //这是什么东西？？？？？？？
        final float x[] =
                {
                        0.0000000000f, 0.5384693101f, -0.5384693101f, 0.9061798459f, -0.9061798459f
                };

        final float c[] =
                {
                        0.5688888889f, 0.4786286705f, 0.4786286705f, 0.2369268850f, 0.2369268850f
                };


        if ( u2 <= u1 )
            return 0.0f;

        if ( u1 < 0.0f )
            u1 = 0.0f;

        if ( u2 > 1.0f )
            u2 = 1.0f;
        //todo

        // 使用高斯正交法
        float sum = 0.0f;
        //建立了计算埃尔米特导数的方法
        Vector2D A = mPositions[i].scale(2.0f)
                .add(mPositions[i + 1].scale(-2.0f))
                .add(inTangents[i])
                .add(outTangents[i]);

        Vector2D B = mPositions[i].scale(-3.0f)
                .add(mPositions[i + 1].scale(3.0f))
                .sub(outTangents[i])
                .sub(inTangents[i].scale(2.0f));

        Vector2D C = outTangents[i];

        for ( int j = 0; j < 5; ++j )
        {
            float u = 0.5f*((u2 - u1)*x[j] + u2 + u1);
//            Vector2D derivative = C + u*(2.0f*B + 3.0f*u*A);
            Vector2D derivative = C.add(
                    B.scale(2.0f).add(
                            A.scale(u).scale(3.0f)
                    ).scale(u)
            );
            sum += (float) (c[j]*derivative.len());
        }
        sum *= 0.5f*(u2-u1);
        return sum;
    }

    @Override
    public Vector2D derivative(float t) {
        if ( getCount() < 2 )
            return Vector2D.originPoint;

        // handle boundary conditions
        if ( t <= times[0] )
            return outTangents[0];
        else if ( t >= times[count-1] )
            return inTangents[count-2];

        // find segment and parameter
        int i;
        for ( i = 0; i < count-1; ++i )
        {
            if ( t < times[i+1] )
            {
                break;
            }
        }
        float t0 = times[i];
        float t1 = times[i+1];
        float u = (t - t0)/(t1 - t0);

        // evaluate
        Vector2D A = mPositions[i].scale(2.0f)
                .add(mPositions[i + 1].scale(-2.0f))
                .add(inTangents[i])
                .add(outTangents[i]);

        Vector2D B = mPositions[i].scale(-3.0f)
                .add(mPositions[i + 1].scale(3.0f))
                .sub(inTangents[i])
                .sub(outTangents[i].scale(2.0f));

        return outTangents[i].add(
                B.scale(2.0f).add(
                        A.scale(u).scale(3.0f)
                ).scale(u)
        );
    }

    @Override
    public Vector2D second_derivative(float t) {
        if ( getCount() < 2 )
            return Vector2D.originPoint;

        // handle boundary conditions
        if ( t <= times[0] )
            t = 0.0f;
        else if ( t > times[count-1] )
            t = times[count-1];

        // find segment and parameter
         int i;
        for ( i = 0; i < count-1; ++i )
        {
            if ( t <= times[i+1] )
            {
                break;
            }
        }
        float t0 = times[i];
        float t1 = times[i+1];
        float u = (t - t0)/(t1 - t0);

        // evaluate
        Vector2D A = mPositions[i].scale(2.0f)
                .add(mPositions[i +1].scale(-2.0f))
                .add(inTangents[i])
                .add(outTangents[i]);

        Vector2D B = mPositions[i].scale(-3.0f)
                .add(mPositions[i + 1].scale(3.0f))
                .sub(inTangents[i])
                .sub(outTangents[i].scale(2.0f));

        return B.scale(2.0f).add(A.scale(6.0f).scale(u));
    }


    @Override
    public Vector2D evaluate(float t) {
        if (getCount() < 2)
            return Vector2D.originPoint;
        //处理边界情况
        if ( t <= times[0] )
            return mPositions[0];
        else if ( t >= times[count-1] )
            return mPositions[count-1];

        //寻找片段
        int i;
        for ( i = 0; i < count-1; ++i )
        {
            if ( t < times[i+1] )
            {
                break;
            }
        }

        float t0 = times[i];
        float t1 = times[i+1];
        float u = (t - t0)/(t1 - t0);

        // evaluate
        Vector2D A = mPositions[i].scale(2.0f)
                .add(mPositions[i + 1].scale( -2.0f))
                .add(inTangents[i])
                .add(outTangents[i]);
        Vector2D B = mPositions[i].scale(-3.0f)
                .add(mPositions[i + 1].scale(3.0f))
                .sub(inTangents[i])
                .sub(outTangents[i].scale(2.0f));
        return mPositions[i].add(
                outTangents[i].add(
                        B.add(
                                A.scale(u)
                        ).scale(u)
                ).scale(u)
        );
    }

    public Vector2D[] getInTangents() {
        return inTangents;
    }

    public Vector2D[] getOutTangents() {
        return outTangents;
    }
}
