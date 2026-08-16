package com.snl.swing.game.curve;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class CameraMoving extends DiKaErPlus implements TimingTarget {

    Vector2D p;
    float animate_time = 4000.f;
    Animator animator;

    Vector2D[] path;
    HermiteCurve curve;
    Linear linear;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        path = new Vector2D[]{
                Vector2D.originPoint,new Vector2D(3,0),new Vector2D(6,0),new Vector2D(3,0),Vector2D.originPoint
        };
        int length = path.length;
        float[] times = new float[]{0.0f,1000L,2000L,3000L,animate_time};
//        p = Polynomials.aitken(4,path,0);
//        viewMat = Matrix3x3f.translate(p.inv());

//        curve = new HermiteCurve();
//        curve.initializeNatural(path,times,length);

        linear = new Linear();
        linear.Initialize(path,times,length);

        animator = new Animator((long) animate_time,this);
        animator.start();
    }

    public static void main(String[] args) {
        launchGame(new CameraMoving());
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);
        drawCircle(g2,p,0.1,false);
        drawCircle(g2,p,0.05,true);
        g2.dispose();
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
//        p = Polynomials.aitken(3,path, (float) fraction);
        float f = (float) (fraction * animate_time);
//        p = curve.evaluate(f);
        p = linear.Evaluate(f);
        viewMat = Matrix3x3f.translate(p.inv());
        axis.createAxis(viewMat,c,wordWidth);
    }
}
