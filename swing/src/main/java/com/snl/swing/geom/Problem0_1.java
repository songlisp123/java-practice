package com.snl.swing.geom;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Matrix4x4f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;
import com.snl.swing.game2026.dataStructure.Array;

import java.awt.*;

//测试斜投影
public class Problem0_1 extends ProblemSolver {

    Array<Vector3D> points,vp;
    Array<Vector2D> nds;
    Array<IndexEdge> edges;
    Vector3D eye;
    static final Vector3D K = Vector3D.direction(0,0,1);
    static final Vector3D J = Vector3D.direction(0,1,0);
    double d = 1;
    double n = 2.5,f = 9999;
    double r = 1,l = -1;
    double b = -1,t = 1;
    Vector3D center = Vector3D.point(1,1,1);

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();


        sb.append("我们可以用面来表示一个形状，而不是用边；例如，在杜雷尔程序中的立方体，可以用六个正方形面来表示，而不是用立方体的12条边。然后我们可以选择只绘制朝向眼睛的面。“绘制”在这种情况下可能仅指绘制面的边。结果是一种线框物体的渲染，但只显示可见的面。如果物体是凸的，这种渲染是正确的；如果不是凸的，一个面可能会部分遮挡另一个面。对于像立方体这样的凸形状，其任意一个面的前两条边不平行，判断一个顶点为 (P₀, P₁, P₂, ...) 的面是否可见就相当容易：你计算向量的叉积 w = (P₂−P₁) × (P₁−P₀)，并将它与从眼睛 E 到 P₀ 的向量 v = P₀−E 比较。如果 v 和 w 的点积为负，则该面是可见的。这个规则依赖于对每个面的顶点进行排序，使得叉积 w 是一个向量，如果将其放在面的中心，会指向自由空间，而不是指向物体内部。");
        points = new Array<>(8);

//        points.add(0,new Vector3D(-6,-6,2));
//        points.add(1,new Vector3D(6,-6,2));
//        points.add(2,new Vector3D(6,6,2));
//        points.add(3,new Vector3D(-6,6,2));

//        正方形上部分
//        points.add(4,new Vector3D(-6,-6,6));
//        points.add(5,new Vector3D(6,-6,6));
//        points.add(6,new Vector3D(6,6,6));
//        points.add(7,new Vector3D(-6,6,6));

        points.add(0,Vector3D.point(-0.5,-0.5,-0.5));
        points.add(1,Vector3D.point(0.5,-0.5,-.5));
        points.add(2,Vector3D.point(0.5,0.5,-.5));
        points.add(3,Vector3D.point(-0.5,0.5,-.5));

//        正方形上部分
        points.add(4,Vector3D.point(-0.5,-0.5,0.5));
        points.add(5,Vector3D.point(0.5,-0.5,.5));
        points.add(6,Vector3D.point(0.5,.5,.5));
        points.add(7,Vector3D.point(-.5,.5,.5));



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

        eye = Vector3D.point(0,2,-3);
        super.openTextPanel();
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }


    @Override
    void drawContent(Graphics2D g2) {
        Vector3D vDir = center.subtract(eye).norm();
        System.out.println("vDir = " + vDir);

        Vector3D vUp = K.subtract(vDir.mul(K.dot(vDir))).norm();
//        Vector3D vUp = J.subtract(vDir.mul(J.dot(vDir))).norm();
//        Vector3D vUp = J;
        System.out.println("vUp = " + vUp);
        Vector3D vSide = vUp.crossDot(vDir).norm();

        System.out.println("vSide = " + vSide);
        Matrix4x4f matrix4x4f = Matrix4x4f.translate(eye.inv());
        matrix4x4f.setColumn(0,vSide);
        matrix4x4f.setColumn(1,vUp);
        matrix4x4f.setColumn(2,vDir);

        matrix4x4f = matrix4x4f.inverse();

        vp = new Array<>(points.length());
        for (int i = 0;i<vp.length();i++) {
            Vector3D mulled = matrix4x4f.mul(points.get(i));
            vp.add(i,mulled);
        }


        nds = new Array<>(points.length());
        for (Vector3D v : vp) {
            double zn = v.z;
            double x = (2 * n * v.x /zn) / (r - l) ;
            double y = (2 * n * v.y / zn) / (t - b);
            double z = ((n + f) / (n - f)) * zn + 2 * n * f / (n- f);
            Vector2D newV = new Vector2D(x,y);
            nds.add(newV);
        }



        for (int i = 0;i<edges.length();i++) {
            IndexEdge indexEdge = edges.get(i);
            Vector2D v0 = nds.get(indexEdge.index0);
            Vector2D v1 = nds.get(indexEdge.index1);
            drawLine(g2,v0,v1);
        }
    }

    public static void main(String[] args) {
        launchGame(new Problem0_1());
    }

    static class IndexEdge {
        int index0,index1;

        public IndexEdge(int index0, int index1) {
            if (index0 < 0)
                throw new IllegalArgumentException();
            this.index0 = index0;
            this.index1 = index1;
        }
    }

    @Override
    public void timingEvent(double fraction) {
        double theta = Math.PI  * fraction;
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        center.x =  cos -  sin;
        center.y = sin +  cos;
    }
}
