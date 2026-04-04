package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TestPolyGon extends DiKaErPlus {

    Polygon polygon,sheared;
    boolean pMoving,clicking,drag;
    double[][] shearF = {
            {0.2,0.2,1.0,1.0},
            {-0.2,0,-1.0,1.0},
            {0.0,-0.2,-1.0,-1.0},
            {0.2,0.2,1.0,1.0},
    };

    double[][] scaleF = {
            {0.2,0.2,1.0,1.0},
            {-0.2,0,-1.0,1.0},
            {0.0,-0.2,-1.0,-1.0},
            {0.2,0.2,1.0,1.0},
    };

    double shearX,shearY;
    int shearIndex,scaleIndex;
    boolean shearing;

    double scaleX ,scaleY;

    double rot,theta;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        polygon = new Polygon(new Vector2D[] { new Vector2D(-0.5, 0.5),
                new Vector2D(1, 0), new Vector2D(-0.5, -0.5),
                new Vector2D(), });
        polygon = new Polygon(new Vector2D[]{
                new Vector2D(-1,1),new Vector2D(1,1),
                new Vector2D(1,-1),new Vector2D(-1,-1)
        });
        polygon.shear(0.5,0.5);
        shearing = true;

        theta = Math.PI / 3;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicking = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        if (clicking && pointInPoly(mouse,polygon)) {
            pMoving = true;
        }
        pMoving = pMoving && drag;
        if (pMoving) {
            //如果移动点
            Matrix3x3f re = this.getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            polygon.translate(d);
        }
        shearX += shearF[shearIndex][0] * delta;
        shearY += shearF[shearIndex][1] * delta;
        if (shearX < -1.0F || shearX > 1.0f || shearY < -1.0F || shearY > 1.0F)
        {
            shearX = shearF[shearIndex][2];
            shearY = shearF[shearIndex][3];
            if (shearIndex++ == shearF.length - 1)
            {
                shearIndex = 0;
            }
        }
        //缩放
        scaleX += scaleF[scaleIndex][0] * delta;
        scaleY += scaleF[scaleIndex][1] * delta;
        if (scaleX < -1.0F || scaleX > 1.0f || scaleY < -1.0F || scaleY > 1.0F)
        {
            scaleX = scaleF[scaleIndex][2];
            scaleY = scaleF[scaleIndex][3];
            if (scaleIndex++ == scaleF.length - 1)
            {
                scaleIndex = 0;
            }
        }
        rot += theta * delta;
        clicking = false;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setColor(Color.WHITE);
        g2.drawString("shearX="+shearX,30,150);
        g2.drawString("shearY="+shearY,30,170);
        g2.drawString("scaleX="+scaleX,30,190);
        g2.drawString("scaleY="+scaleY,30,210);
        g2.drawString("旋转角度="+rot,30,230);
        drawPolyGon(g2,polygon,false);
//        Vector2D center = polygon.getCenter();
//        drawCircle(g2,center,0.10,true);
//
//        Polygon translated = polygon.getTranslated(1, -1);
//        drawPolyGon(g2,translated,false);
//        drawCircle(g2,translated.getCenter(),0.10,true);
//
//        Polygon rotateInstance = polygon.getRotateInstance(Math.PI / 2.0, 2, 0);
//        drawPolyGon(g2,rotateInstance,false);
//
//        Polygon scaled = translated.getScaled(2, 2);
//        drawPolyGon(g2,scaled,false);
        sheared = polygon.getSheared(shearX, shearY); //剪切
        sheared.scale(scaleX,shearY); //缩放
        sheared.rotate(rot); //旋转
        drawPolyGon(g2,sheared,false);
    }

    public static void main(String[] args) {
        launchGame(new TestPolyGon());
    }
}
