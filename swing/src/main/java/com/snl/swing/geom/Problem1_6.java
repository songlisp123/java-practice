package com.snl.swing.geom;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Problem1_6 extends ProblemSolver {

    Vector2D[] arm,smallArm,hand,finger01,finger02;
    Matrix3x3f translationForm,rotateTransForm;
    Matrix3x3f worldTransForm;

    double armRot;
    final double ROTATE = Math.PI / 4;
    double sx,sy;

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        sb.append("测试手臂！！！");

        arm = new Vector2D[]{
                new Vector2D(-1.5,-0.5),new Vector2D(1.5,-0.5),new Vector2D(1.5,0.5),new Vector2D(-1.5,0.5)
        };

        smallArm = new Vector2D[] {
                new Vector2D(-0.75,-0.2),new Vector2D(0.75,-0.2),new Vector2D(0.75,0.2),new Vector2D(-0.75,0.2)
        };


        hand = new Vector2D[] {
                new Vector2D(-0.2,-0.2),new Vector2D(0.2,-0.6),new Vector2D(0.2,0.6),new Vector2D(-0.2,0.2)
        };

        finger01 = new Vector2D[]{
                new Vector2D(-.2,-.1),new Vector2D(.2,-.1),new Vector2D(.2,.1),new Vector2D(-.2,.1)
        };
        armRot = 0;

        translationForm = Matrix3x3f.identity();
        rotateTransForm = Matrix3x3f.rotate(armRot);
        sx = sy = 1;
        super.openTextPanel();
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_W)) {
            //手臂旋转
            armRot += delta * ROTATE;
            rotateTransForm = Matrix3x3f.rotate(armRot);
        }

        if(keyBoardEvent.keyDown(KeyEvent.VK_S)) {
            armRot -= delta * ROTATE;
            rotateTransForm = Matrix3x3f.rotate(armRot);
        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_UP)) {
            sx += delta;

        }

        if (keyBoardEvent.keyDown(KeyEvent.VK_DOWN)) {
            sx -= delta;

        }
    }

    @Override
    void drawContent(Graphics2D g2) {
        worldTransForm = translationForm.mul(
                Matrix3x3f.translate(-1.5,0).mul(rotateTransForm).mul(Matrix3x3f.translate(1.5,0))
        );

        Vector2D[] pCopy = new Vector2D[4];
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = arm[i];
            pCopy[i] = worldTransForm.mul(v);
        }

        drawPoly(g2,pCopy,true);

        //小臂
        Matrix3x3f smallArmToWorld = worldTransForm.mul(Matrix3x3f.translate(1.5 + 0.75,0));
        smallArmToWorld = smallArmToWorld.mul(
                Matrix3x3f.translate(-0.75,0).mul(Matrix3x3f.scale(sx,sy)).mul(Matrix3x3f.translate(0.75,0))
        );
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = smallArm[i];
            pCopy[i] = smallArmToWorld.mul(v);
        }

        drawPoly(g2,pCopy,true);

        //手掌
        Matrix3x3f handTransForm = smallArmToWorld.mul(Matrix3x3f.translate(0.75 + 0.2,0));
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = hand[i];
            pCopy[i] = handTransForm.mul(v);
        }

        drawPoly(g2,pCopy,true);

        //指节1
        Matrix3x3f f01 = handTransForm.mul(Matrix3x3f.translate(.4,0));
        f01 = f01.mul(
                Matrix3x3f.translate(-0.2,0)
                        .mul(Matrix3x3f.rotate(ROTATE))
                        .mul(Matrix3x3f.translate(0.2,0))
        );
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = finger01[i];
            pCopy[i] = f01.mul(v);
        }
        drawPoly(g2,pCopy,true);

        //关节节点
        Matrix3x3f joint01 = f01.mul(Matrix3x3f.translate(.2 + .1,0));
        Vector2D jC = joint01.mul(new Vector2D());
        drawCircle(g2,jC,0.1,true);

        //第二关节
        Matrix3x3f f02 = joint01.mul(Matrix3x3f.translate(0.1 + 0.2,0));
        f02 = f02.mul(
                Matrix3x3f.translate(-0.3,0).
                        mul(Matrix3x3f.rotate(ROTATE))
                        .mul(Matrix3x3f.translate(0.3,0))
        );
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = finger01[i];
            pCopy[i] = f02.mul(v);
        }
        drawPoly(g2,pCopy,true);

        //第二连接点
        Matrix3x3f joint02 = f02.mul(Matrix3x3f.translate(0.3,0));
        jC = joint02.mul(Vector2D.originPoint);
        drawCircle(g2,jC,0.1,true);


        //第三关节
        Matrix3x3f f03 = joint02.mul(Matrix3x3f.translate(0.3,0));
        f03 = f03.mul(
                Matrix3x3f.translate(-0.3,0).
                        mul(Matrix3x3f.rotate(ROTATE))
                        .mul(Matrix3x3f.translate(0.3,0))
        );
        for(int i = 0;i<pCopy.length;i++) {
            //TODO
            Vector2D v = finger01[i];
            pCopy[i] = f03.mul(v);
        }
        drawPoly(g2,pCopy,true);
    }

    public static void main(String[] args) {
        launchGame(new Problem1_6());
    }
}
