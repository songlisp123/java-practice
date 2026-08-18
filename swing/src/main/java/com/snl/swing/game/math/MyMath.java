package com.snl.swing.game.math;

public class MyMath {
    public static float PI = 3.1415926535897932384626433832795f;
    public static float E = 1.0e-6f;
    public static float halfOfPi = 1.5707963267948966192313216916398f;
    public static float TowPi = 2f * PI;

    public static boolean invertMatrix(float[] matrix,int n) {
        int[] swap;
        swap = new int[n];
        for (int pivot = 0; pivot < n; ++pivot) {
            //todo
            int row,col;
            int maxRow = pivot;
            float maxElement = ABS(matrix[maxRow + n * pivot]);
            for ( row = pivot+1; row < n; ++row )
            {
                float elem = ABS( matrix[ row + n*pivot ] );
                if ( elem > maxElement )
                {
                    maxElement = elem;
                    maxRow = row;
                }
            }

            //如果矩阵是零矩阵，停止
            if (isZero(maxElement, (float) Epsilon.PRECISION))
                return false;

           //如果不在当前行中，则交换行
            swap[pivot] = maxRow;
            if (maxRow != pivot) {
                //交换行
                for ( col = 0; col < n; ++col )
                {
                    float temp = matrix[ maxRow + n*col ];
                    matrix[ maxRow + n*col ] = matrix[ pivot + n*col ];
                    matrix[ pivot + n*col ] = temp;
                }
            }
            //将当前行乘以1/枢轴以“设置”枢轴为1
            float pivotRecip = 1.0f/matrix[ n*pivot + pivot ];
            for ( col = 0; col < n; ++col )
            {
                matrix[ pivot + n*col ] *= pivotRecip;
            }

            //复制1/枢轴到枢轴点（在原地做逆操作）
            matrix[pivot + n*pivot] = pivotRecip;

            //现在把其他行的主列归零
            for ( row = 0; row < n; ++row )
            {
                // 不要从主行中减去
                if ( row == pivot )
                    continue;

                // 当前行减去主列的倍数，//使主列元素变为0
                float factor = matrix[ row + n*pivot ];

                //清除pivot列元素（在适当的位置做逆操作）//最终将该元素设置为-factor*pivotInverse
                matrix[ row + n*pivot ] = 0.0f;

                // subtract multiple of row
                for ( col = 0; col < n; ++col )
                {
                    matrix[ row + n*col ] -= factor*matrix[ pivot + n*col ];
                }
            }
        }

        // 完成，按列方向撤销交换，以相反的顺序
        int p = n;
        do
        {
            --p;
            //如果行已经交换
            if (swap[p] != p)
            {
                // 交换对应的列
                for ( int row = 0; row < n; ++row )
                {
                    float temp = matrix[ row + n*swap[p] ];
                    matrix[ row + n*swap[p] ] = matrix[ row + n*p ];
                    matrix[ row + n*p ] = temp;
                }
            }
        }
        while (p > 0);
        return true;
    }


    public static float ABS(float val) {
        return (val < 0) ? -val : val;
    }

    public static boolean isZero(float val,float e) {
        return ABS(val) <= e;
    }

    public static float erChi(int base, int n) {

        if (n < 0 || n > base) {
            return 0;
        }

        if (n > base - n) {
            n = base - n;
        }

        float result = 1;

        for (int i = 1; i <= n; i++) {
            result *= (float) (base - n + i) / i;
        }

        return result;
    }
}
