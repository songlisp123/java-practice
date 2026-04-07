package com.snl.swing.game.math;

import com.snl.swing.game.math.contract.AbstractShape;

/**
 * 线段建模
 */
public class SegMent extends AbstractShape implements Cloneable {
    public Vector2D p1,p2;

    private static final int DONT_INTERSECT = 0; //不相交
    private static final int DO_INTERSECT = 1; //交叉
    private static final int COLLINEAR = 2;  //共线

    private static final int PointBeyondP1 = 3; //在 p1 外侧
    private static final int PointBeyondP2 = 4; //在 p2 外侧
    private static final int ONP1P2RANGE = 5; //在 p1、p2 区间

    private static final int OUTSIDE = 6; //线段 在 圆 外面
    private static final int INSIDE = 7; //线段 在 圆 里面
    private static final int INSECTIONINONEPOINT = 8; //线段 与多边形 相交于一点
    private static final int COLLISION = 9; //线段 与 相交于一点
    private static final int TARGENT = 10; //线段 与 相交于一点

    public SegMent() {
    }

    public SegMent(Vector2D p1, Vector2D p2) {
        validate(p1,p2);
        this.p1 = p1;
        this.p2 = p2;
    }

    public SegMent(SegMent scaledSegment) {
        this.p1 = scaledSegment.p1;
        this.p2 = scaledSegment.p2;
    }

    private boolean validate(Vector2D p1, Vector2D p2) {
        if (p1 == null) {
            throw new IllegalArgumentException("p1不能为null");
        } else if (p2 == null) {
            throw new IllegalArgumentException("p2不能为Null");
        } else if (p1.equals(p2)) {
            throw new IllegalArgumentException("p1不能语p2共点");
        } else {
            return true;
        }
    }

    public Range projection(Vector2D on) {
        Range r1 = new Range();
        Vector2D unit = on.norm();
        r1.min = p1.dot(unit);
        r1.max = p2.dot(unit);
        r1.sort();
        return r1;
    }

    /*
    判断是否与另一条线段相交
     */
    public int collisionSegment(SegMent segMent) {
        double a1,a2,b1,b2,c1,c2; //线系数
        double r1,r2,r3,r4; //符号值
        double denom; //中间值

        /*
        计算当前线的系数方程：
        a1*x + b1 * y + c1 =  0
         */
        a1  = p2.y - p1.y;
        b1 = p1.x - p2.x;
        c1 = p2.x * p1.y - p2.y * p1.x;

        /*
        计算 r3和r4
         */
        r3 = a1 * segMent.p1.x + b1 * segMent.p1.y + c1;
        r4 = a1 * segMent.p2.x + b1 * segMent.p2.y + c1;

        //如果符号相同
        if (r3 != 0 && r4 != 0 && this.sameSigns(r3,r4)) {
            //无相交
            return DONT_INTERSECT;
        }

        /*
        计算第二个线的方程系数
         */
        a2 = segMent.p2.y - segMent.p1.y;
        b2 = segMent.p1.x - segMent.p2.x;
        c2 = segMent.p2.x * segMent.p1.y - segMent.p2.y * segMent.p1.x;

        /*
        计算 r1 和 r2
         */
        r1 = a2 * this.p1.x + b2 * this.p1.y + c2;
        r2 = a2 * this.p2.x + b2 * this.p2.y + c2;

        //判断符号
        if (r1 != 0 && r2 != 0 && sameSigns(r1,r2)) {
            //无相交
            return DONT_INTERSECT;
        }

        denom  = a1 * b2  - a2 * b1;
        if (denom == 0)
            return COLLINEAR;
        //否则 相交
        return DO_INTERSECT;
    }

    //返回交点
    public Vector2D collidePoint(SegMent segMent) {
        Vector2D v = new Vector2D();
        int mode = this.collisionSegment(segMent);
        switch (mode) {
            case DONT_INTERSECT  , COLLINEAR -> {
                return null;
            }
            case DO_INTERSECT -> {
                double a1,a2,b1,b2,c1,c2;
                double demo,num;
                a1  = this.p2.y - this.p1.y;
                b1 = this.p1.x - this.p2.x;
                c1 = p2.x * p1.y - p2.y * p1.x;

                a2 = segMent.p2.y - segMent.p1.y;
                b2 = segMent.p1.x - segMent.p2.x;
                c2 = segMent.p2.x * segMent.p1.y - segMent.p2.y * segMent.p1.x;

                demo = a1 * b2 - a2 * b1;

                num = b1 * c2 - b2 * c1;
//                v.x = (num < 0 ? num-offset : num + offset) / demo;
                v.x = num / demo;

                num = a2 * c1 - a1 * c2;
//                v.y = (num < 0? num - offset : num + offset) / demo;
                v.y = num / demo;
            }
        }
        return v;
    }

    /**
     * 获取 点 的模式
     * @param p 测试点
     * @return
     */
    public int getNearestPointMode(Vector2D p) {
        if (p == null)
            throw new IllegalArgumentException("非法参数异常，参数不能为null");
        double t = (p.x - p1.x) * (p2.x - p1.x) +
                (p.y - p1.y) * (p2.y - p1.y);
        if (t < 0) {
            //p 在 线段p1、p2外，接近p1
            return PointBeyondP1;
        }else {
            //判断是否接近p2
            t = (p2.x - p.x) * (p2.x - p1.x)
                    + (p2.y - p.y) * (p2.y - p1.y);
            if (t < 0)
                // p 在 p2侧 外
                return PointBeyondP2;
            else
            {
                //P 在 p1、p2区间内
                return ONP1P2RANGE;
            }
        }
    }

    /**
     * 获取接近点p的线上的点
     * @param p 测试点
     * @return
     */
    public Vector2D getNearestPoint(Vector2D p) {
        int mode = this.getNearestPointMode(p);
        Vector2D v = null;
        switch (mode) {
            case PointBeyondP1 -> v = this.p1;
            case PointBeyondP2 -> v = this.p2;
            case ONP1P2RANGE -> {
                //否则 p在线段 区间中
                //这一个方法能成功，引入了一个新的线段，但是不够优雅
                //一个不够优雅的解决方案
//                Vector2D m = p1.sub(p2).norm();
//                Line l = new Line(p2,m,Line.YINGSHI);
//                v = l.nearestPoint(p);

                //第二个方案
                Vector2D pToP1 = p.sub(p1);
                Vector2D p2ToP1 = p2.sub(p1);
                double ab2 = p2ToP1.dot(p2ToP1);
                double ab = pToP1.dot(p2ToP1);

                double t = ab / ab2;
                return p1.add(p2ToP1.mul(t));
            }
        }
        return v;
    }

    /**
     * 是否符号相同
     * @param a 测试数一
     * @param b 测试数2
     * @return 是否相同符号
     */
    private boolean sameSigns(double a,double b) {
        return a*b > 0;
    }

    /*
    获取中心点
     */
    public Vector2D getCenter() {
        if (p1.equals(p2))
            return p1;
//        Vector2D center;
//        Vector2D m = p2.sub(p1).norm(); //计算方向向量 - 归一化
//        center = p1.add(m.scale(length() / 2));
//        return center;
        //或者
        double x = p1.x + p2.x;
        double y = p1.y + p2.y;
        return new Vector2D(x / 2.0,y / 2.0);

    }

    /*
    直线长度
     */
    public double length() {
        return p2.sub(p1).len();
    }

    /**
     * 获取 点p到直线的距离
     * @param p 测试点
     * @return
     */
    public double getDistanceOfPoint(Vector2D p) {
        int mode = this.getNearestPointMode(p);
        double a = -1;
        switch (mode) {
            case PointBeyondP1 -> a = p.sub(p1).len();
            case PointBeyondP2 -> a = p.sub(p2).len();
            case ONP1P2RANGE -> {
                //否则 p在线段 区间中
                Vector2D m = p1.sub(p2).norm();
                Line l = new Line(p2,m,Line.YINGSHI);
                Vector2D nr = l.nearestPoint(p);
                a = p.sub(nr).len();
            }
        }
        return a;
    }

    public boolean collideCircleInBoolean(Vector2D center,double radius) {
        return collideCircleInBoolean(new Circle(radius,center));
    }

    public boolean collideCircleInBoolean(Circle circle) {
        int mode = collideCircle(circle);
        return mode == COLLISION || mode == INSECTIONINONEPOINT ||
                mode == TARGENT;
    }

     //TODO
    public int collideCircle(Circle circle) {
        //内部
        if (!circle.containsPointInBoolean(this.p1)
        && !circle.containsPointInBoolean(this.p2)) {
            //在外部，分为三种情况
            //第一种 ： 完全不相交
            double d , r;
            d = this.getDistanceOfPoint(circle.getCenter());
            r = circle.r;

            double diff = d - r;
            if (diff > 0) {
                return OUTSIDE;
            }
            else if (diff < 0)
                return COLLISION;
            else if (Math.abs(diff) <= Epsilon.PRECISION) {
                return TARGENT;
            }
        }
        //外部
        if (circle.containsPointInBoolean(this.p1)
        && circle.containsPointInBoolean(this.p2)) {
            return INSIDE;
        }
        //相交与一点
        if (circle.containsPointInBoolean(this.p1)
            && !circle.containsPointInBoolean(this.p2)) {
            //如果p1在内部 且 p2 在外部
            return INSECTIONINONEPOINT;
        }

        if (!circle.containsPointInBoolean(this.p1)
            && circle.containsPointInBoolean(this.p2))
        {
            //如果 p1 在外部 且 p2 在内部
            return INSECTIONINONEPOINT;
        }
        throw new UnsupportedOperationException("为支持的操作");
    }

    public Vector2D[] collideCircleToVectorArray(Circle circle) {
        int mode = this.collideCircle(circle);
        switch (mode) {
//            case -1 -> throw new RuntimeException("发生未知异常");
            case OUTSIDE,INSIDE -> {
                return null;
            }
            case INSECTIONINONEPOINT -> {
                Vector2D[] v = new Vector2D[1];
                double dot;
                Vector2D norm = p2.sub(p1).norm();
                Line l = new Line(p1,norm,Line.YINGSHI);
                Vector2D[] vector2DS = circle.collidePointInLine(l);
                for (Vector2D v2d : vector2DS) {
                    dot = v2d.sub(circle.getCenter()).dot(norm);
                    if (dot < 0)
                        //在外侧
                        continue;
                    else
                        //在内测
                        v[0] = v2d;
                }
                return v;
            }
            case COLLISION -> {
                Vector2D[] v;
                Vector2D norm = this.p1.sub(this.p2).norm();
                Line l = new Line(p2,norm,Line.YINGSHI);
                v = circle.collidePointInLine(l);
                return v;
            }
        }
        return null;
    }

    // 圆 vs 线段：返回穿透深度
    public double getCollideDepth(Vector2D pos, double r) {
        Circle c = new Circle(r, pos);

        int mode = this.collideCircle(c);

        switch (mode) {

            case OUTSIDE -> {
                return 0;
            }

            case INSIDE, COLLISION, INSECTIONINONEPOINT, TARGENT -> {

                // 线方向
                Vector2D lineDir = p2.sub(p1);
                Vector2D norm = lineDir.prep().norm(); // 法线

                // 圆心到直线距离（点到线距离公式）
                double d = Math.abs(
                        c.getCenter().sub(p1).dot(norm)
                );

                // 穿透深度
                double depth = r - d;

                // 防御：不穿透返回0
                return Math.max(depth, 0);
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "SegMent{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }

    @Override
    public double getArea() {
        throw new UnsupportedOperationException("暂不支持该操作");
    }

    @Override
    public void rotate(double rotateTheta) {
        p1.rotate(rotateTheta);
        p2.rotate(rotateTheta);
    }

    @Override
    public <T extends Polygon> T rotateWithTheta(double rotateTheta) {
        return null;
    }

    @Override
    public void rotate(double rot, Vector2D center) {
        //TODO
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        p1 = rotate.mul(p1.sub(center)).add(center);
        p2 = rotate.mul(p2.sub(center)).add(center);
    }

    @Override
    public void rotate(double rot, double x, double y) {
        //TODO
        this.rotate(rot,new Vector2D(x,y));
    }

    @Override
    public void scale(double scale) {

        Vector2D p1ToP2 = p2.sub(p1).norm();
        Vector2D center = getCenter();

        double length = length();
        double l = length / 4.0;

        p2 = center.add(p1ToP2.mul(l));

        Vector2D p2ToP1 = p1.sub(p2).norm();
        p1 = center.add(p2ToP1.mul(l));
    }

    @Override
    public void scale(double sx, double sy) {
        throw new UnsupportedOperationException("未受支持的操作");
    }

    @Override
    public void shear(double sx, double sy) {
        p1.shear(sx,sy);
        p2.shear(sx,sy);
    }

    @Override
    public void translate(Vector2D translated) {
        p1.add(translated);
        p2.add(translated);
    }

    @Override
    public void translate(double x, double y) {
        p1.x += x;
        p1.y += y;
        p2.x += x;
        p2.y += y;
    }

    @Override
    public SegMent clone() {
        return new SegMent(p1.clone(),p2.clone());
    }

    public Vector2D getSegmentIntersection(SegMent segMent) {
        return getSegmentIntersection(p1,p2,segMent.p1,segMent.p2,true);
    }

    public AABB createAABB() {
        double maxX = p1.x;
        double minX = p2.x;

        if (minX > maxX) {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }

        double maxY = p1.y;
        double minY = p2.y;
        if (minY > maxY) {
            double temp = minY;
            minY = maxY;
            maxY = temp;
        }

        Vector2D min = new Vector2D(minX,minY);
        Vector2D max = new Vector2D(maxX,maxY);

        return new AABB(min,max);
    }

    public boolean tangentCircle(Circle circle) {
        return this.collideCircle(circle) == TARGENT;
    }

    public boolean tangentCircle(Vector2D pos,double r) {
        Circle circle = new Circle(r,pos);
        return tangentCircle(circle);
    }

    /******************* 静态方法 **********************/

    public static final Vector2D getLineIntersection(Vector2D ap1,Vector2D ap2,Vector2D bp1,Vector2D bp2) {
        Vector2D A = ap2.sub(ap1);
        Vector2D B = bp2.sub(bp1);

        double BCorssA = B.cross2D(A);
        if (Math.abs(BCorssA) <= Epsilon.PRECISION) {
            //分母不能为零
            return null;
        }else {
            double amxA = ap1.sub(bp1).cross2D(A);
            if (Math.abs(amxA) <= Epsilon.PRECISION) {
                //分支为零
                return null;
            }
            //否则，克莱姆法则
            double t = amxA / BCorssA;
            return bp1.add(B.mul(t));
        }
    }

    public Vector2D getLineIntersection(SegMent segMent) {
        return getLineIntersection(p1,p2,segMent.p1,segMent.p2);
    }

    public static final Vector2D getSegmentIntersection(Vector2D ap1,Vector2D ap2,Vector2D bp1,Vector2D bp2) {
        return getSegmentIntersection(ap1,ap2,bp1,bp2,true);
    }

    public static final Vector2D getSegmentIntersection(Vector2D ap1,Vector2D ap2,Vector2D bp1,Vector2D bp2,boolean inclusive) {
        Vector2D A = ap2.sub(ap1);
        Vector2D B = bp2.sub(bp1);

        double BCorssA = B.cross2D(A);
        if (Math.abs(BCorssA) <= Epsilon.PRECISION) {
            //分母不能为零
            return null;
        }
        else {
            double amxA = ap1.sub(bp1).cross2D(A);
            if (Math.abs(amxA) <= Epsilon.PRECISION) {
                return null;
            }else {
                double t = amxA / BCorssA;
                //测试 是否在线段上
                if (inclusive) {
                    if (t < 0.0 || t > 1.0)
                        return null;
                } else if (t <= 0.0 || t >= 1.0) {
                    return null;
                }

                Vector2D ip = bp1.add(B.mul(t));
                double ta = ip.sub(ap1).dot(A) / A.dot(A);
                if (inclusive) {
                    if (ta < 0.0 || ta > 1.0)
                        return null;
                } else if (ta <= 0.0 || ta >= 1.0) {
                    return null;
                }
                return ip;
            }
        }
    }
}
