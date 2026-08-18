package com.snl.swing.game.curve.Bezier;

import com.snl.swing.game.math.MyMath;
import com.snl.swing.game.math.Vector2D;

import java.util.Arrays;


public class BezierHelp {

    public static final float[][] CUBLIC_BASE = bernstein_Base_Mat(3);
    public static final float[][] QUA_BASE = bernstein_Base_Mat(4);
    public static final float[][] BIO_BASE = bernstein_Base_Mat(2);

    /**
     * 给出一般的伯恩斯坦基矩阵形式
     * @param degree 伯恩斯坦阶级
     * @return 伯恩斯坦一般形式的基
     * @since 2026年8月18日23:41:12
     */
    static float[][] bernstein_Base_Mat(int degree) {
        int count = degree + 1;
        float[][] result = new float[count][count];
        int i,j,sign;
        for (i = degree; i >= 0;--i) {
            int reminderI = degree - i;
            for (j = 0; j <= reminderI; ++j) {
                sign = ((reminderI + j) % 2 == 0 ) ? 1 : -1;
                result[j ][i] = sign * MyMath.erChi(degree,i) * MyMath.erChi(reminderI,j);
            }
        }
        return result;
    }

    /**
     * [t^n…… t^0] * [M] * [position]^T </br>
     * 其中M = [col0,col1,col2 …… colN]</br>
     * 计算n阶贝塞尔曲线位置
     * @param t 参数
     * @param positions 采样点几何
     * @return 返回贝塞尔曲线上参数 {@code t}的一点
     * @since 2026年8月19日00:24:49
     * @implNote 我们假设采样区间在[0,1]之间
     */
    public static Vector2D evaluate(float t, Vector2D[] positions) {
        return evaluate(t,positions, positions.length-1,0);
//        if (t < 0.0f || t > 1.0f)
//            throw new IllegalArgumentException("参数值必须在0到1之间");
//        int degree = positions.length - 1;
//        if (degree < 1)
//            throw new IllegalArgumentException("贝塞尔曲线至少2阶");
//        float[][] bezierBaseT;
//        switch (degree) {
//            case 2 -> bezierBaseT = BIO_BASE;
//            case 3 -> bezierBaseT = CUBLIC_BASE;
//            case 4 -> bezierBaseT = QUA_BASE;
//            default -> bezierBaseT = bernstein_Base_Mat(degree);
//        }
//
//        int i,j;
//        //获取到基变换矩阵，获取参数值
//        float[] parameterT = new float[degree + 1];
//        float[] bezierBase = new float[degree + 1];
//        Arrays.fill(parameterT,1.0f);
//        for ( i = 0; i <= degree - 1; i++) {
//            for ( j = 1; j <= degree - i; j++) {
//                parameterT[i] *= t;
//            }
//        }
//
//        //计算贝塞尔基
//
//        for (i = 0; i<=degree;i++) {
//            for (j = 0; j <= degree;j++){
//                bezierBase[i] += parameterT[j]*bezierBaseT[i][j];
//            }
//        }
//        //参数
//
//        Vector2D temp = new Vector2D();
//        for (i = 0; i<=degree;i++) {
//            temp.x += bezierBase[i] * positions[i].x;
//            temp.y += bezierBase[i] * positions[i].y;
//        }
//
//        return temp;
    }

    public static Vector2D evaluate(float t,Vector2D[] positions,int degree,int offset) {
        if (t < 0.0f || t > 1.0f)
            throw new IllegalArgumentException("参数值必须在0到1之间");
        if (degree < 1)
            throw new IllegalArgumentException("贝塞尔曲线至少二阶");
        offset = Math.max(0,offset);
        int len = positions.length;
        if ((offset + degree) >= len)
            throw new IllegalArgumentException("数组参数不足，请重新赋值");
        float[][] bezierBaseT;
        switch (degree) {
            case 2 -> bezierBaseT = BIO_BASE;
            case 3 -> bezierBaseT = CUBLIC_BASE;
            case 4 -> bezierBaseT = QUA_BASE;
            default -> bezierBaseT = bernstein_Base_Mat(degree);
        }
        int i,j;
        //获取到基变换矩阵，获取参数值
        float[] parameterT = new float[degree + 1];
        float[] bezierBase = new float[degree + 1];
        Arrays.fill(parameterT,1.0f);
        for ( i = 0; i <= degree - 1; i++) {
            for ( j = 1; j <= degree - i; j++) {
                parameterT[i] *= t;
            }
        }

        //计算贝塞尔基

        for (i = 0; i<=degree;i++) {
            for (j = 0; j <= degree;j++){
                bezierBase[i] += parameterT[j]*bezierBaseT[i][j];
            }
        }

        Vector2D temp = new Vector2D();
        for (i = 0; i<=degree;i++) {
            if (positions[offset + i] == null )
                throw new IllegalArgumentException("数组元素不能为null");
            temp.x += bezierBase[i] * positions[offset + i].x;
            temp.y += bezierBase[i] * positions[offset + i].y;
        }

        return temp;
    }


    /**
     * 微分
     * @param control_points 控制点
     * @param t 参数
     * @param degree 贝塞尔阶数
     * @return 切向量
     * @implNote <>曲线与曲面建模第四章</>，同理假定采样区间在【0,1】内
     */
    public static Vector2D derivative(Vector2D[] control_points, float t, int degree) {
        //一阶导控制多边形
        if (degree < 1)
            throw new IllegalArgumentException("导数深度必须大于等于0");
        int len = control_points.length;
        if (len < 3)
            throw new IllegalArgumentException("采样点必须大于等于3个");
//        int count = n - degree;
//        if (count <= 0)
//            throw new IllegalArgumentException("参数异常");
//        // 当前控制多边形
//        Vector2D[] poly = new Vector2D[len];
//        System.arraycopy(control_points,0,poly,0,len);
//
        // 连续求导
//        for (int d = 1; d <= degree; d++) {
//            Vector2D[] next = new Vector2D[poly.length - 1];
//            for (int i = 0; i < next.length; i++) {
//                next[i] = poly[i + 1].sub(poly[i]).scale(currentDegree);
//                next[i] = poly[i + 1].sub(poly[i]);
//            }
//
//            poly = next;
//        }

//       return bezier_curve(poly,t, poly.length);

        //实际上使用以下公式更加好一点：
        /*
        d(b^n) = n * (deltabj * B(n - 1,j)) 对于j ∈（0，n-1）
         */
        //获取差分向量
        Vector2D[] v = new Vector2D[degree];
        Vector2D start = control_points[0];
        for (int i = 1; i<=degree; i++) {
            v[i - 1] = control_points[i].sub(start);
            start = control_points[i];
        }
        return evaluate(t,v);
    }

    public static Vector2D second_derivative(Vector2D[] control_points, float t, int degree) {
        //一阶导控制多边形
        if (degree < 1)
            throw new IllegalArgumentException("导数深度必须大于等于0");
        int len = control_points.length;
        if (len < 3)
            throw new IllegalArgumentException("采样点必须大于等于3个");

        /*
        d(b^n) = n * (deltabj * B(n - 1,j)) 对于j ∈（0，n-1）
         */
        //获取差分向量
        int newDegree = degree - 1;
        Vector2D[] v = new Vector2D[newDegree];
        Vector2D start = control_points[0];
        for (int i = 1; i<=newDegree; i++) {
            Vector2D d0 = control_points[i].sub(start);
            Vector2D d1 = control_points[i + 1].sub(control_points[i]);
            v[i - 1] = d1.sub(d0);
            start = control_points[i];
        }
        return evaluate(t,v);
    }

    /**
     * 升阶操作，添加一个控制点
     * @param degree 度数
     * @param control_points 控制点集合
     * @return 新控制点集合
     */
    public static Vector2D[] degree_elevate(int degree, Vector2D[] control_points) {
        int i,degrees;
        degrees = degree + 1;
        Vector2D[] temp = new Vector2D[control_points.length + 1];
        temp[0] = control_points[0];

        for (i = 1; i <=degree; i ++) {
            temp[i] = control_points[i - 1].scale(i)
                    .add(control_points[i].scale(degrees - i));
            temp[i] = temp[i].div(degrees);
        }

        temp[degrees] = control_points[degree];
        return temp;
    }

    /**
     * 使用算法模拟贝塞尔曲线
     * @param degree 吧贝塞尔曲线深度
     * @param coeff 参考点坐标
     * @param t 参数
     * @param type 算法类型
     * @return 曲线上的点的坐标
     * @since 2026年8月18日23:42:14
     * @implNote 《曲线与曲面建模》 -- 第四章，贝塞尔曲线性质
     */
    private static float decas(int degree,float[] coeff,float t,BezierType type) {

        int length = coeff.length;
        if (length < degree + 1)
            throw new RuntimeException();

        int i;
        float t1 = 1- t;
        float result = -999;
        switch (type) {
            case Horner_Algorithm -> {
                int n_choose_i;
                float fact,aux;

                fact =1.0F;
                n_choose_i = 1;
                aux = coeff[0] * t1;

                for (i = 1; i < degree;i++) {
                    fact = fact * t;
                    n_choose_i = n_choose_i * (degree - i + 1) / i;
                    aux = (aux + fact * n_choose_i * coeff[i]) * t1;
                }
                aux += fact * t * coeff[degree];
                result = aux;
            }
            case De_Casteljau_Algorithm -> {
                int r;
                float[] coeffCopy = new float[10];

                for (i = 0; i <= degree ; i++)
                    coeffCopy[i] = coeff[i];

                for (r = 1; r <= degree; r++) {
                    for (i = 0; i <= degree - r; i++)
                        coeffCopy[i] = t1 * coeffCopy[i] + t * coeffCopy[i + 1];
                };
                result =  coeffCopy[0];
            }
        }
        return result;
    }


    /**
     * 插值贝塞尔曲线上一点
     * @param degree
     * @param nPoints
     * @param coeff
     * @param type
     * @return
     */
    public static float[] bez_to_Points(int degree,int nPoints,float[] coeff,BezierType type) {
        float t,delt;
        int i;

        float points[] = new float[nPoints + 1];

        delt = 1.0f / (float) nPoints; //步长
        t = 0.0f;
        for ( i = 0 ;  i <= nPoints ; i++) {
            points[i] = decas(degree,coeff,t,type);
            t += delt;
        }

        return points;
    }

}
