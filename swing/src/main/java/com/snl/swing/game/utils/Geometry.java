package com.snl.swing.game.utils;

import com.snl.swing.game.math.Convexity;
import com.snl.swing.game.math.Epsilon;
import com.snl.swing.game.math.Vector2D;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 一个帮助方法
 */
public class Geometry {

    private static final double FRACTOR = 1 / 3.0;

    /**
     * 获取 凸多边形 重心，重心由边的平分线描述
     * @param convexity 凸多边形
     * @return 重心坐标
     */
    public static  Vector2D getAverageCenter(Convexity convexity) {
        Vector2D temp = new Vector2D();
        Vector2D center = new Vector2D();
        Iterator<Vector2D> iterator = convexity.iterator();
        while (iterator.hasNext()) {
            Vector2D v2 = iterator.next();
            temp = temp.add(v2);
        }
        int size = convexity.getSize();
        center.x = temp.x / size;
        center.y = temp.y / size;
        return center;
    }
    
    public static Vector2D getAverageCenter(Vector2D...vector2DS) {
        if (vector2DS == null) 
            throw new IllegalArgumentException("参数不能为null");
        int size = vector2DS.length;
        if (size == 0) {
            throw new IllegalArgumentException("数组长度不能为0");
        } else if (size == 1) {
            Vector2D center = vector2DS[0];
            if (center == null)
                throw new NullPointerException("数组元素不能为null");
            return center.clone();
        }else {
            Vector2D ac = new Vector2D(); //初始化
            for (Vector2D v : vector2DS) {
                //遍历所有点
                if (v == null)
                    throw new NullPointerException("数组元素不能为null");
                /*
                我们之所以描述这个，是因为一个公式，一个多边形的顺时针顶点的和
                除以点的个数等于该多边形的重心
                 */
                ac = ac.add(v);
            }

            ac.x /= size;
            ac.y /= size;
            return ac;
        }
    }

    public static Vector2D getAverageCenter(List<Vector2D> vector2DS) {
        return getAverageCenter(vector2DS.toArray(Vector2D[]::new));
    }

    /**
     * 获取面积权重中心
     * @param vector2DS 点坐标
     * @return 权重中心
     */
    public static Vector2D getAreaWeightedCenter(Vector2D...vector2DS) {
        //获取重心
        Vector2D ac = getAverageCenter(vector2DS);
        int size = vector2DS.length;
        Vector2D awc = new Vector2D();
        double area = 0;

        //计算面积的权重
        for (int i = 0;i<size;i++) {
            Vector2D p1 = vector2DS[i];
            Vector2D p2 = i + 1 < size ? vector2DS[i + 1] : vector2DS[0];
            /*
            以下步骤：计算子三角的面积
             */
            p1 = p1.sub(ac);
            p2 = p2.sub(ac);
            //叉积 计算 有向三角形面积 ，这是个带符号的有向三角形面积的两倍
            double crossed = p1.cross2D(p2);
            //获取面积
            double triangleArea = crossed / 2.0;
            area += triangleArea;
            //判断新的中心
            awc.add(
                    p1.add(p2).mul(FRACTOR).mul(triangleArea)
            );
        }

        if (Math.abs(area) <= Epsilon.E) {
            //如果 面积 小于精度
            return vector2DS[0].clone();
        }else {
            /*
            c = c / 总面积 + 重心
             */
            awc = awc.div(area).add(ac);
            return awc;
        }
    }

    public static Vector2D getAreaWeightedCenter(Convexity convexity) {
        return getAreaWeightedCenter(convexity.getVertices());
    }

    public static Vector2D getAreaWeightedCenter(Collection<Vector2D> collection) {
        return getAreaWeightedCenter(collection.toArray(Vector2D[]::new));
    }


}
