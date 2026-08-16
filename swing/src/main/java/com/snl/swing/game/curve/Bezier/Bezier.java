package com.snl.swing.game.curve.Bezier;

import com.snl.swing.game.math.Vector2D;

public class Bezier {
    
    public static float[][] CUBLIC_BASE = null;
    public static float[][] QUA_BASE = null;
    public static float[][] BIO_BASE = null;


    /**
     * 给出一般的伯恩斯坦基矩阵形式
     * @param degree 伯恩斯坦阶级
     * @return 伯恩斯坦一般形式的基
     */
    public static float[][] bernstein_Base_Mat(int degree) {
        int count = degree + 1;
        float[][] result = new float[count][count];
        int col,row;
        float factor,i,j;
        for (col = 0; col < count; col++) {
            for (row = 0; row < count; row++) {
                if (result[col][row] != 0.0f || result[row][col] != 0.0f) {
                    factor = ((col - row) % 2 == 0) ? 1.0f : -1.0f;
                    j = erChi(col, degree);
                    i = erChi(col, row);
                    result[row][col] = factor * j * i;
                    result[col][row] = result[row][col];
                }
            }
        }
        return result;
    }
    
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
            factor = erChi(base,n);
        }

        result *= factor;

        for (int i = 1; i <= base; i++)
            result *=  t;
        for (int i = 1; i <= n - base; i++)
            result *=  invt;

        return result * offset;
    }

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

    private static Vector2D bio_bezier(Vector2D p0, Vector2D p1, Vector2D p2, float t) {
        float b20 = Bezier.bezier_base(3, 0, t, 0.0f, 1.0f);
        float b21 = Bezier.bezier_base(3, 1, t, 0.0f, 1.0f);
        float b22 = Bezier.bezier_base(3, 2, t, 0.0f, 1.0f);

        Vector2D temp = new Vector2D();
        temp.x =  p0.x * b20 + p1.x * b21 + p2.x * b22;
        temp.y =  p0.y * b20 + p1.y * b21 + p2.y * b22;

        return temp;
    }

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

    private static float erChi(int base,int n) {
        int i ;
        int f = 1,s = 1,t = 1;
        for (i = 0;i <= n; i++) {
            f *= (i == 0) ? 1 : i;
        }

        for (i = 0;i <= base; i++) {
            t *= (i == 0) ? 1 : i;
        }

        for (i = 0;i <= n - base; i++) {
            s *= (i == 0) ? 1 : i;
        }

        return (float) f / (t * s);
    }


    public static Vector2D derivative(Vector2D[] control_points, float t, int degree) {
        //一阶导控制多边形
        if (degree < 1)
            throw new IllegalArgumentException("导数深度必须大于等于0");
        int len = control_points.length;
        int n = len - 1;
        if (len < 3)
            throw new IllegalArgumentException("采样点必须大于等于3个");
        int count = n - degree;
        if (count <= 0)
            throw new IllegalArgumentException("参数异常");
        // 当前控制多边形
        Vector2D[] poly = new Vector2D[len];
        System.arraycopy(control_points,0,poly,0,len);

        // 连续求导
        for (int d = 1; d <= degree; d++) {
            Vector2D[] next = new Vector2D[poly.length - 1];
            for (int i = 0; i < next.length; i++) {
//                next[i] = poly[i + 1].sub(poly[i]).scale(currentDegree);
                next[i] = poly[i + 1].sub(poly[i]);
            }

            poly = next;
        }

       return bezier_curve(poly,t, poly.length);
    }
    
    
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

    public static Vector2D[] degree_elevates(int degree,int r,Vector2D[] control_points) {
        int i ,degrees;
        degrees = degree + r;
        Vector2D[] temp = new Vector2D[degrees + 1];
        temp[0] = control_points[0];
        throw new UnsupportedOperationException("暂未实现该功能！请稍后再试");
    }

    public static float factor(int n) {
        int r = 1;
        for (int i = 1; i <= n; i++)
            r *= i;
        return r;
    }
}
