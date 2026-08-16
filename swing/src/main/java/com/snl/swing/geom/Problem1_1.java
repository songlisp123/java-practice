package com.snl.swing.geom;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Problem1_1 extends ProblemSolver{

    Vector2D body;
    double ra,rb;
    Matrix3x3f matB,matArm;
    double theta;
    AABB arm;
    Polygon p;
    boolean ani;

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();

        sb.append("测试局部坐标系");

        ra = 0.5;
        rb = 0.5;

        matB = new Matrix3x3f(new double[][] {
                {1,0,2},
                {0,1,2},
                {0,0,1}
        });

        body = new Vector2D(2,2);

        arm = new AABB(
                new Vector2D(ra,- ra / 2),new Vector2D(ra + rb,ra / 2)
        );
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_D)) {
            body.x += delta;
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_A)) {
            body.x -= delta;
        }


        if (keyBoardEvent.keyDown(KeyEvent.VK_W)) {
            body.y += delta;
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_S))
            body.y -= delta;
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    void drawContent(Graphics2D g2) {
        g2.setColor(Color.green);

        matB.setValue(body.x, 2,0);
        matB.setValue(body.y, 2,1);

        drawEllipse(g2, matB.mul(new Vector2D()),ra,rb,true);

        Vector2D h = body.clone();
        h.x = 0;
        h.y = 0.5;

        drawCircle(g2,matB.mul(h),ra,true);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        matArm = Matrix3x3f.rotate(theta);

        Vector2D v ;

        v = matArm.mul(new Vector2D(1,0));
        v = matB.mul(v);
        drawEllipse(g2,v,.2,0.1,true);

        Vector2D min = arm.getMin();
        min = matB.mul(min);

        Vector2D max = arm.getMax();
        max = matB.mul(max);
        AABB aabb = new AABB(min,max);
        drawAAbb(g2,aabb,true);

        //旋转中心
//        Vector2D min1 = arm.getMin();
//        Vector2D max1 = arm.getMax();
        Vector2D lc = new Vector2D(min.getX(),max.getY());
        Vector2D rc = min.add(lc).div(2);

        p =  aabb.getRotateInstance(theta,rc);
        drawPolyGon(g2,p,true);


        //手臂局部坐标
        Vector2D lb = new Vector2D(-0.5,-0.5);
        Vector2D tt = new Vector2D(0.5,0.5);


        //手臂坐标
        Matrix3x3f rotate = Matrix3x3f.rotate(theta);
        //局部坐标
        Vector2D v1 = rotate.mul(new Vector2D(-1.5, -0.5));
        Vector2D v2 = rotate.mul(new Vector2D(1.5, .5));

        Matrix3x3f t = Matrix3x3f.translate(body);
//        t.setValue(1,2,0);
//        t.setValue(1,2,1);
//        t = rotate.mul(t).mul(matB).mul(rotate);
//        Vector2D v1 = t.mul(new Vector2D(-1.5, -0.5));
//        Vector2D v2 = t.mul(new Vector2D(1.5, .5));
        v1 = t.mul(v1);
        v2 = t.mul(v2);





        drawCircle(g2,v2,0.5,true);
        drawCircle(g2,v1,0.5,true);
//        drawCircle(g2,arm,0.5,true);


        //bvody空间手臂
        Vector2D vl = new Vector2D(0,0);
        Vector2D vr = new Vector2D(3,0);

        Vector2D leg = new Vector2D(0,-3);


        boolean showing = keyBoardEvent.keyDownOnce(KeyEvent.VK_L) && !ani;
        if (showing)
            ani = true;
        if (ani) {
            Matrix3x3f m1 = Matrix3x3f.identity();
            m1.setColumn(0,new Vector2D(cos,sin,0));
            m1.setColumn(1,new Vector2D(-sin,cos,0));
//            System.out.println("m1 = " + m1);
            vl = m1.mul(vl);
            vr = m1.mul(vr);
            leg = m1.mul(leg);



        } {
            vl = matB.mul(vl);
            vr = matB.mul(vr);
            leg = matB.mul(leg);
        }
        drawLine(g2,vl,vr);
        drawLine(g2,vl,leg);
    }

    public static void main(String[] args) {
        launchGame(new Problem1_1());
    }

    @Override
    public void timingEvent(double fraction) {
        theta = 0.15 *  Math.PI * fraction;
        if (fraction >= 1.0)
            ani = false;
    }
}
