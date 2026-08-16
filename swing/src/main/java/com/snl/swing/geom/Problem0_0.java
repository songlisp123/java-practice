package com.snl.swing.geom;

import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.utils.Generator;
import com.snl.swing.game2026.dataStructure.Array;

import java.awt.*;

public class Problem0_0 extends ProblemSolver{

    Array<Vector2D> points,ps;
    Array<IndexEdge> edges;
    Vector2D eye;
    double d,dt;
    Array<Vector2D> ndsPoint;
    double xmin,ymin,xmax,ymax;
    double zmax;

    Array<Star> stars;
    double dOld,t;


    @Override
    protected void gameInitial() {
        duration = 10000L;
        super.gameInitial();
        sb.append("我们还可以通过添加一种有限形式的动画来增强该程序：底部（或顶部）四个顶点的 xy坐标是半径为r=√2/2 的圆上的四个等间距点，即r(cosθ,sinθ)，其中θ= π4,34π,54π, 和74π。我们可以通过将θ=π4+t,34π+ t,54π+t, 和74π+t用于四个角点，其中t为一个较小的值。通过逐渐 增加t，并在每次重绘模型时更新，我们可以显示一个旋转的立方体。");
        sb.append( "这种方法显式地改变立方体的坐标然后再重新播放，并不特别高效。立方体实际上变成了一个参数化模型，旋转量t作为参数。问题在于，当我们希望在yz平面而不是xy平面旋转立方体时，就需要修改模型。如果我们希望先在一个平面旋转，然后在另一个平面旋转，就必须进行一些繁琐的代数和三角运算。实际上，更简单的方法是只建模一次立方体，然后学习如何通过旋转（或其他操作）来变换其顶点。我们将在接下来的几章中详细讨论这一点。");
        points = new Array<>(8);
        ps = new Array<>(8);
        //世界坐标或者说模型坐标，此时模型的基于世界一致
        points.add(0,new Vector2D(-0.5,-0.5,2.5));
        points.add(1,new Vector2D(0.5,-0.5,2.5));
        points.add(2,new Vector2D(0.5,0.5,2.5));
        points.add(3,new Vector2D(-0.5,0.5,2.5));

//        正方形上部分
        points.add(4,new Vector2D(-0.5,-0.5,3.5));
        points.add(5,new Vector2D(0.5,-0.5,3.5));
        points.add(6,new Vector2D(0.5,.5,3.5));
        points.add(7,new Vector2D(-.5,.5,3.5));

//        points.add(0,new Vector2D(-6,-6,2));
//        points.add(1,new Vector2D(6,-6,2));
//        points.add(2,new Vector2D(6,6,2));
//        points.add(3,new Vector2D(-6,6,2));

//        正方形上部分
//        points.add(4,new Vector2D(-6,-6,6));
//        points.add(5,new Vector2D(6,-6,6));
//        points.add(6,new Vector2D(6,6,6));
//        points.add(7,new Vector2D(-6,6,6));

        ps.add(0,new Vector2D(-6,-6,2));
        ps.add(1,new Vector2D(6,-6,2));
        ps.add(2,new Vector2D(6,6,2));
        ps.add(3,new Vector2D(-6,6,2));

//        正方形上部分
        ps.add(4,new Vector2D(-6,-6,6));
        ps.add(5,new Vector2D(6,-6,6));
        ps.add(6,new Vector2D(6,6,6));
        ps.add(7,new Vector2D(-6,6,6));


        edges = new Array<>(12);
        edges.add(0,new IndexEdge(0,1));
        edges.add(1,new IndexEdge(1,2));
        edges.add(2,new IndexEdge(2,3));
        edges.add(3,new IndexEdge(3,0));



        edges.add(4,new IndexEdge(4,5));
        edges.add(5,new IndexEdge(5,6));
        edges.add(6,new IndexEdge(6,7));
        edges.add(7,new IndexEdge(7,4));

        edges.add(8,new IndexEdge(0,4));
        edges.add(9,new IndexEdge(1,5));
        edges.add(10,new IndexEdge(2,6));
        edges.add(11,new IndexEdge(3,7));

        eye = new Vector2D(0,0,-10);
        d = 1;
        xmin = ymin = -1;
        ymax = xmax = 1;
        zmax = 13;

        stars = new Array<>(1000);
        for (int i = 0;i<1000;i++) {
            Star s = new Star(
                    Generator.generateDouble(-wordHeight / 2.0,wordWidth / 2.0),
                    Generator.generateDouble(-wordHeight / 2.0,wordWidth / 2.0),
                    Generator.generateDouble(-10,-5)
            );
            stars.add(s);
        }

        super.openTextPanel();

    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {
        d = dOld;
    }

    @Override
    void drawContent(Graphics2D g2) {
        if (ndsPoint == null) {
            ndsPoint = new Array<>(points.length());
            double xn,yn,zn;
            for (Vector2D v : points) {
//                zn = -eye.w + v.w;
//                double x =   d * v.x / zn;
//                double y = - d * v.y / zn;

                double x =   d * v.x / v.w;
                double y = - d * v.y / v.w;
//                xn = (x - xmin) / (xmax - xmin);
//                yn = (y - ymin) / (ymax - ymin);
//                Vector2D newV = new Vector2D(xn, yn,1);
                Vector2D newV = new Vector2D(x, y,1);
                ndsPoint.add(newV);
            }
        }

        Array<Vector2D> copy = ndsPoint;
        ndsPoint.clear();
        double xn,yn,zn;
        synchronized (this) {
            for (Vector2D v : points) {
                zn = -eye.w + v.w;
                double x = d * v.x / zn;
                double y = -d * v.y / zn;
                double z = x * zn / v.x; //或者
//            double z = y * zn / v.y;
                Vector2D newV = new Vector2D(x, y, 1);
//            double x =   d * v.x / zn;
//            double y = - d * v.y / zn;
//            xn = (x - xmin) / (xmax - xmin);
//            yn = (y - ymin) / (ymax - ymin);
//            Vector2D newV = new Vector2D(xn, yn,1);
                ndsPoint.add(newV);
            }
        }

        for (int i = 0;i<edges.length();i++) {
            IndexEdge indexEdge = edges.get(i);
            drawLine(g2,ndsPoint.get(indexEdge.index0),ndsPoint.get(indexEdge.index1));
        }

        for (Star s : stars) {
            zn = -eye.w + s.z;
            double x =   d * s.x / zn;
            double y = - d * s.y / zn;
            g2.setColor(s.c);
            drawCircle(g2,new Vector2D(x,y),0.05,true);
        }
    }


    @Override
    public void timingEvent(double fraction) {
        dOld = d;
        d = 20 * fraction;
//        eye.w =  zmax * fraction;
        double a,b;
        for (int i = 0;i<ps.length();i++) {
            Vector2D v = ps.get(i);
            a = Math.cos(Math.PI * 2 * fraction);
            b = Math.sin(Math.PI * 2 * fraction);
            double x = v.x * a - v.y * b;
            double y = v.x * b + v.y * a;
            points.set(i,new Vector2D(x,y,v.w));
        }
    }

    public static void main(String[] args) {
        launchGame(new Problem0_0());
    }

    class IndexEdge {
        int index0,index1;

        public IndexEdge(int index0, int index1) {
            if (index0 < 0)
                throw new IllegalArgumentException();
            this.index0 = index0;
            this.index1 = index1;
        }
    }

    class Star {
        double x,y,z;
        Color c;

        public Star(double x, double y,double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            c = Color.WHITE.darker();
        }
    }
}
