package com.snl.swing.game.curve.Bezier;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.curve.Polynomials;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.tank.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class TestBezier extends DiKaErPlus implements TimingTarget {

    Vector2D[] bezier_points, control_points,cp2,bezier_points02,poly_points,poly_curve_points;
    Vector2D p,deri,seconderivative;

    Animator animator;

    //多边形
    SimpleTank tank;
    Vector2D[] derivatePoly,derivative,secondDerivative,secondDerivativePoly;

    int n;
    int clickIndex = -1;

    boolean clicked,drag,moving;

    @Override
    protected void gameInitial() {
        super.gameInitial();

        animator = new Animator(10_000L,this);
        animator.setRepeatCount(Animator.INFINITE);
        animator.setEndBehavior(Animator.EndBehavior.RESET);
        animator.setRepeatBehavior(Animator.RepeatBehavior.LOOP);

        control_points = new Vector2D[] {
                new Vector2D(), new Vector2D(1, 1), new Vector2D(6, 3),new Vector2D(7,3), new Vector2D(10, -1)
        };

        cp2 = new Vector2D[] {
                new Vector2D(10, -1),new Vector2D(11, 3), new Vector2D(13, -3),new Vector2D(7,3),new Vector2D(3,2)
        };

        poly_points = control_points;

        n = 1000;
        bezier_points = new Vector2D[n + 1];
        poly_curve_points = new Vector2D[n + 1];
        bezier_points02 = new Vector2D[n + 1];
        derivative = new Vector2D[n + 1];
        secondDerivative = new Vector2D[n + 1];

        //一阶导多边形
        derivatePoly = new Vector2D[control_points.length - 1];
        //二阶导多边形
        secondDerivativePoly = new Vector2D[control_points.length - 2];
        rebuild();
        //创建坦克
        createTank();
        animator.start();
    }

    private void rebuild() {
        int j;float i = 0.0f;
        float step = 1.0f / n;

        for ( j = 0 ; j < derivatePoly.length ; j ++) {
            derivatePoly[j] = control_points[j + 1].sub(control_points[j]);
        }

        for (j = 0 ; j < secondDerivativePoly.length ; j ++) {
            secondDerivativePoly[j] = control_points[j + 2].
                    sub(control_points[j + 1].scale(2))
                    .add(control_points[j]);
        }


        j = 0;
        for (;i<=1.0f; i+=step) {
            Vector2D t = BezierHelp.evaluate(i,control_points);
            Vector2D t2 = BezierHelp.evaluate(i,cp2);
            bezier_points02[j] = t2;

            bezier_points[j] = t;
            Vector2D s = BezierHelp.derivative(control_points,i,control_points.length - 1);

            derivative[j] = s;

            Vector2D aitken = Polynomials.aitken(poly_points.length - 1, poly_points, i);
            poly_curve_points[j] = aitken;

            Vector2D v = BezierHelp.second_derivative(control_points,i,control_points.length - 1);
            secondDerivative[j++] = v;
        }
    }


    @Override
    protected void processInput(double delta) {
        super.processInput(delta);

        boolean kewDown_A = keyBoardEvent.keyDown(KeyEvent.VK_A);
        boolean kewDown_W = keyBoardEvent.keyDown(KeyEvent.VK_W);
        boolean kewDown_D = keyBoardEvent.keyDown(KeyEvent.VK_D);
        boolean kewDown_S = keyBoardEvent.keyDown(KeyEvent.VK_S);
        boolean kewDown_U = keyBoardEvent.keyDown(KeyEvent.VK_U);
        boolean kewDown_I = keyBoardEvent.keyDown(KeyEvent.VK_I);
        boolean keyDown_Space = keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE);
        boolean keyDown_UP = keyBoardEvent.keyDown(KeyEvent.VK_UP);
        boolean keyDown_DOWN = keyBoardEvent.keyDown(KeyEvent.VK_DOWN);
        boolean keyDown_Tab = keyBoardEvent.keyDown(KeyEvent.VK_C);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);


        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P))
        {
            if (animator.isRunning())
                animator.pause();
            else
                animator.resume();
        }

        if (kewDown_W)
            tank.rotateClockWise(delta);
        if (kewDown_S)
            tank.rotateClock(delta);

        if (keyDown_UP)
            tank.rotatePaoTaiClockWise(delta);
        if (keyDown_DOWN)
            tank.rotatePaoTaiClock(delta);

        if (keyDown_Space)
            if (!tank.isFiring())
                tank.setFiring(true);
        tank.setFiring(tank.isFiring() && keyDown_Space);

        clickIndex = getClicked();

        if (clicked && clickIndex != -1)
            moving = true;
        moving = moving && drag;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);


        Matrix3x3f translate;
        Matrix3x3f rotate = Matrix3x3f.identity();
        if (p != null && deri != null) {
            translate = Matrix3x3f.translate(p);
            rotate = Matrix3x3f.rotate(deri.norm().angle());
        }
        else {
           rotate =  translate = Matrix3x3f.identity();
        }


        tank.update(delta);
        tank.getBase().setTransFrom(translate);
        tank.getBase().setRotateForm(rotate);
        tank.getPaoTai().setRotateForm(rotate);

        if (moving && clickIndex != -1) {
            Vector2D controlPoint = control_points[clickIndex];
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D v = re.mul(mouseDelta);
            control_points[clickIndex] = controlPoint.add(v);
            rebuild();
        }

//        viewMat = Matrix3x3f.translate(tank.getCenter().inv());
//        axis.createAxis(getViewportTransform(),c,wordWidth);

    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.PINK);

        //绘制贝塞尔多边形和贝塞尔曲线
        drawPolyLine(g2, bezier_points,false);
        drawPolyLine(g2, control_points,true);
//
        //绘制贝塞尔多边形和贝塞尔曲线
        drawPolyLine(g2, bezier_points02,false);
        drawPolyLine(g2, cp2,true);

        //绘制一般多项式插值后的系数
//        drawPolyLine(g2,poly_curve_points,false);


        //一阶导不为0
        if (deri != null)
        {
            drawCircle(g2,deri,.05,true);
            drawCircle(g2,deri,.1,false);

            g2.setColor(Color.MAGENTA);
            drawPolyLine(g2,derivatePoly,true);
            drawPolyLine(g2,derivative,false);
        }

        //二阶导不为0
        if (seconderivative != null)
        {
            g2.setColor(Color.cyan);
            drawPolyLine(g2,secondDerivativePoly,true);
            drawPolyLine(g2,secondDerivative,false);
            g2.setColor(Color.green);
            drawCircle(g2,seconderivative,0.05,true);
            drawCircle(g2,seconderivative,0.1,false);
        }

        //绘制坦克
        tank.draw(g2,this);

//
//        Vector2D[] de = Bezier.degree_elevate(control_points.length - 1, control_points);
//        drawPolyLine(g2,de);
//        Vector2D[] de2 = Bezier.degree_elevate(de.length - 1, de);
//        drawPolyLine(g2,de2);
//        Vector2D[] de3 = Bezier.degree_elevate(de2.length - 1, de2);
//        drawPolyLine(g2,de3);

        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestBezier());
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        int index = (int) (fraction * n);
        p = bezier_points[index];
        deri = derivative[index];
        seconderivative = secondDerivative[index];
    }


    public void createTank() {
        tank = new SimpleTank();
        Base base = new Base();
        base.setOutlines(
                new Vector2D[]{
                        new Vector2D(-1.5,-1.25),new Vector2D(1.5,-1.25),new Vector2D(1.5,1.25),new Vector2D(-1.5,1.25)
                }
        );

        base.setAnglerSpeed(Math.PI);
        base.setPaint(Color.green);

        tank.setBase(base);

        Wheel wheel = new Wheel(10,0.5);
        wheel.setAnglerSpeed(Math.PI);
        wheel.setShowPattern(true);

        PaoTai paoTai = new PaoTai(12,1);
        paoTai.setAnglerSpeed(Math.PI);


        Gun gun = new Gun();
        gun.setOutlines(new Vector2D[] {
                new Vector2D(-0.25,-1),new Vector2D(0.25,-1),new Vector2D(0.25,1),new Vector2D(-0.25,1)
        });

        tank.setWheel(wheel);
        tank.setPaoTai(paoTai);
        tank.setGun(gun);
    }

    public int getClicked() {
        int r = -1;
        Vector2D mouse = getMousePointInVector();
        for (int i = 0;i<control_points.length;i++){
            Vector2D controlPoint = control_points[i];
            if (pointInCircle(mouse,controlPoint,1)) {
                r = i;
                break;
                }
        }
        return r;
    }
}
