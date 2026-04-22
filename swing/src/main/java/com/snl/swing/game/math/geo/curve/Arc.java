package com.snl.swing.game.math.geo.curve;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.contract.AbstractShape;
import com.snl.swing.game.math.geo.PathIterator;

public class Arc extends AbstractShape {

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    //封闭状态
    public static final int OPEN = 0;
    public static final int CHORD = 1;
    public static final int PIE = 2;

    //包围四边形
    protected double x;
    protected double y;
    protected double w;
    protected double h;

    //点
    private double startAngle;
    private double extent;

    //类型
    private int type;

    public Arc() {
        this(OPEN);
    }

    public Arc(int type) {
        setArcType(type);
    }

    public Arc(double x, double y, double w, double h,
               double startAngle, double extent, int type) {
        this(type);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.startAngle = startAngle;
        this.extent = extent;
    }

    public Arc(AABB aabb,double startAngle,double extent,int type) {
        this(type);
        this.x = aabb.getMin().x;
        this.y = aabb.getMax().y;
        this.w = aabb.getMax().x - aabb.getMin().x;
        this.h = aabb.getMax().y - aabb.getMin().y;
        this.startAngle = startAngle;
        this.extent = extent;
    }

    public void setArcType(int type) {
        if (type < OPEN || type > PIE)
            throw new IllegalArgumentException("非法参数异常");
        this.type = type;
    }

    public boolean isEmpty() {
        return w <= 0 || h <= 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getExtent() {
        return extent;
    }

    public int getType() {
        return type;
    }

    //获取圆弧的开始点
    /*
    xi = x + w / 2 * (cos(theta)) + w / 2
     */
    public Vector2D getStartPoint() {
        double r = Math.toRadians(-getStartAngle());
        double x = getX() + (Math.cos(r) * 0.5 + 0.5) * getW();
        double y = getY() + (Math.sin(r) * 0.5 + 0.5) * getH();
        return new Vector2D(x,y);
    }


    //获取圆弧的结束点
    /*
    xi = x + w / 2 * (cos(θ + extent)) + w / 2
     */
    public Vector2D getEndPoint() {
        double r = Math.toRadians(-getStartAngle() - getExtent());
        double x = getX() + (Math.cos(r) * 0.5 + 0.5) * getW();
        double y = getY() + (Math.sin(r) * 0.5 + 0.5) * getH();
        return new Vector2D(x,y);
    }

    public void setAngleStart(Vector2D p) {
        //设置初始点
        double dx,dy,centerX,centerY;
        centerX = x + getW() / 2.0;
        centerY = y + getH() / 2.0;
        dx = getH() * (p.x - centerX);
        dy = getW() * (p.y - centerY);
        setStartAngle(Math.toDegrees(Math.atan2(dy,dx)));
    }

    public void setAngles(double x1,double y1,double x2,double y2) {
        double dx1,dy1,dx2,dy2,centerX,centerY;
        double angle1,angle2,diff;
        double width = getW();
        double height = getH();
        centerX = x + width / 2.0;
        centerY = y + height / 2.0;
        //计算dx1,dy1
        dx1 = (x1 - centerX) * height;
        dy1 = (y1 - centerY) * width;
        angle1 = Math.toDegrees(Math.atan2(dy1,dx1));

        //计算终点
        dx2 = (x2 - centerX) * height;
        dy2 = (y2 - centerY) * width;
        angle2 = Math.toDegrees(Math.atan2(dy2,dx2));

        diff = angle2 - angle1;
        setStartAngle(angle1);
        setExtent(diff);
    }

    public boolean containsAngle(double angle) {
        double e = getExtent();
        boolean backwards = (e < 0);
        if (backwards) {
            e = -e;
        }
        if (e > 360.0)
            return true;
        angle = normalizeDegrees(e) - normalizeDegrees(getStartAngle());
        if (backwards)
            angle = -angle;
        if (angle< 0)
            angle += 360.0;
        return angle >= 0 && angle < e;
    }

    public void setAngles(Vector2D start,Vector2D end) {
        this.setAngles(start.x,start.y,end.x,end.y);
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public void setExtent(double extent) {
        this.extent = extent;
    }

    //帮助方法，剪切角度
    static double normalizeDegrees(double angle) {
        if (angle > 180.0) {
            if (angle <= (180.0 + 360.0)) {
                angle = angle - 360.0;
            } else {
                angle = Math.IEEEremainder(angle, 360.0);
                if (angle == -180.0) {
                    angle = 180.0;
                }
            }
        } else if (angle <= -180.0) {
            if (angle > (-180.0 - 360.0)) {
                angle = angle + 360.0;
            } else {
                angle = Math.IEEEremainder(angle, 360.0);
                if (angle == -180.0) {
                    angle = 180.0;
                }
            }
        }
        return angle;
    }

    @Override
    public double getArea() {
        //TODO
        throw new UnsupportedOperationException("暂不支持该操作");
    }

    @Override
    public boolean containsPoint(double x, double y) {
        //获取圆弧的边界椭圆
        double ellw = getW();
        if (ellw <= 0)
            return false;
        double normx = (x - getX()) / ellw + 0.5; //为什么要加0.5？？
        double ellh = getH();
        if (ellh <= 0)
            return false;
        double normy = (y - getY()) / ellh + 0.5;
        double distance = normx * normx + normy * normy;
        if (distance >= 0.25)
            return false;
        double angExt = Math.toDegrees(getExtent());
        if (angExt >= 360.0)
            return true;
        boolean inarc = containsAngle(Math.toDegrees(-Math.atan2(normy,normx)));
        if (type == PIE)
            return inarc;
        //其他弦与弧线
        if (inarc)
        {
            if (angExt >= 180.0)
                return true;
        }else {
            if(angExt <= 180)
                return false;
        }
        //TODO
        //实现更多信息
        return false;
    }

    @Override
    public AABB getAABB() {
        //TODO
        if (isEmpty())
            return new AABB();
        Vector2D min,max;
        double x1,y1,x2,y2;
        if (getType() == PIE)
        {
            x1 = x2 = y1 = y2 = 0;
        }
        else {
            x1 = y1 = 1.0;
            x2 = y2 = -1.0;
        }
        double angle = 0.0;
        for (int  i = 0;i<6;i++) {
            if (i < 4) {
                angle += 90;
                if (!containsAngle(angle))
                    continue;
            } else if (i == 4) {
                angle = getStartAngle();
            }else {
                angle += getExtent();
            }

            double rads =Math.toRadians(-angle);
            double xe = Math.cos(rads);
            double ye = Math.sin(rads);
            x1 = Math.min(x1, xe);
            y1 = Math.min(y1, ye);
            x2 = Math.max(x2, xe);
            y2 = Math.max(y2, ye);
        }
        min = new Vector2D(x1,y1);
        max = new Vector2D(x2,y2);
        return new AABB(min,max);
    }

    @Override
    public PathIterator getPathIterator(Matrix3x3f transform) {
        return null;
    }

    @Override
    public void rotate(double rotateTheta) {

    }

    @Override
    public void rotate(double rot, double x, double y) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double sx, double sy) {

    }

    @Override
    public void translate(double x, double y) {

    }
}
