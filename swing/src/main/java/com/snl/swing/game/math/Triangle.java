package com.snl.swing.game.math;

public class Triangle extends Convexity {

    public Triangle(Vector2D ... vertices) {
        super(null,vertices);
        if (vertices.length != 3)
            throw new IllegalArgumentException("参数必须是3个");
    }

    /**
     * 获取内切圆
     * @return 三角形的内切圆
     */
    public Circle getInsideCircle() {
        Vector2D d = new Vector2D();
        double area = getArea();
        double perimeter = getPerimeter();
        double r = 2 * area / perimeter;
        Vector2D[] vs = getVertices();
        Vector2D v1 = vs[0];
        Vector2D v2 = vs[1];
        Vector2D v3 = vs[2];
        double len = v2.sub(v3).len();
        d = d.add( v1.scale(len));
        len = v3.sub(v1).len();
        d = d.add( v2.scale(len));
        len = v1.sub(v2).len();
        d = d.add( v3.scale(len));
        d = d.div(getPerimeter());
        return new Circle(r,d);
    }

    /**
     * 获取外切圆
     * @return 三角形的外切圆
     */
    public Circle getOutSideCircle() {
        double d1,d2,d3,c1,c2,c3,c;
        double r;
        Vector2D center;
        Vector2D[] ver = getVertices();
        Vector2D v1 = ver[0];
        Vector2D v2 = ver[1];
        Vector2D v3 = ver[2];

        d1 = v3.sub(v1).dot(v2.sub(v1));
        d2 = v3.sub(v2).dot(v1.sub(v2));
        d3 = v1.sub(v3).dot(v2.sub(v3));

        c1 = d2 * d3;
        c2 = d1 * d3;
        c3 = d1 * d2;
        c = c1 + c2 + c3;

        double d = (d1 + d2) * (d2 + d3) * (d3 + d1) / c;
        r = 0.5 * Math.sqrt(d);

        center = v1.scale(c2 + c3)
                .add(v2.scale(c1 + c3))
                .add(v3.scale(c1 + c2))
                .div(2 * c);
        return new Circle(r,center);
    }

    @Override
    public Vector2D pickedRandomPoint(double s, double t) {
        if (s < 0 || s > 1 ||
                t < 0 || t > 1)
            throw new IllegalArgumentException("参数异常，必须是0到1之间");
        double area_sum = 0;
        Vector2D[] ver = getVertices();
        int  i;
        double a,b,c;
        Vector2D v = new Vector2D();
        double[] subTriangleArea = getNormSubTriangleArea();
        for ( i = 0;i<size-2;i++) {
            area_sum += subTriangleArea[i];
            if (area_sum >= s)
                break;
        }
        s = (s- area_sum + subTriangleArea[i]) / subTriangleArea[i];
        t = Math.sqrt(t);

        a = 1 - t;
        b = (1 - s) * t;
        c = s * t;
        v.x = a * ver[0].x + b * ver[i + 1].x + c * ver[i + 2].x;
        v.y = a * ver[0].y + b * ver[i + 1].y + c * ver[i +2].y;
        v.w = 1.0;
        return v;
    }

    public double getArea() {
        return super.getArea() / 2.0;
    }

    @Override
    public boolean containsPoint(Vector2D pos) {
        Vector2D[] vs = getVertices();
        Vector2D p1 = vs[0];
        Vector2D p2 = vs[1];
        Vector2D p3 = vs[2];

        //获取三边向量
        Vector2D ab = p2.sub(p1);
        Vector2D ac = p3.sub(p1);
        Vector2D pa = pos.sub(p1);
        double dot00 = ac.dot(ac);
        double dot01 = ac.dot(ab);
        double dot02 = ac.dot(pa);
        double dot11 = ab.dot(ab);
        double dot12 = ab.dot(pa);
        double denominator = dot00 * dot11 - dot01 * dot01;
        double invD = (double)1.0F / denominator;
        double u = (dot11 * dot02 - dot01 * dot12) * invD;
        if (u <= (double)0.0F) {
            return false;
        } else {
            double v = (dot00 * dot12 - dot01 * dot02) * invD;
            return v > (double)0.0F && u + v <= (double)1.0F;
        }
    }
}
