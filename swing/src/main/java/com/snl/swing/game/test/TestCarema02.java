package com.snl.swing.game.test;

import com.snl.swing.game.OrthographicCamera;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix4x4f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;

import java.awt.*;

public class TestCarema02 extends DiKaErPlus {

    OrthographicCamera camera;

    @Override
    protected void gameInitial() {
        super.gameInitial();

        Vector3D p1 = Vector3D.point(1,0,0);
        Vector3D p2 = Vector3D.point(0,1,0);
        Vector3D p3 = Vector3D.point(0,0,1);

        Vector3D norm = p2.subtract(p1).crossDot(p3.subtract(p2));
        Vector3D vDirection = norm.inv().norm();


        camera = new OrthographicCamera();
        camera.setDirection(vDirection);

        Vector3D vup = Vector3D.K.subtract(vDirection.mul(Vector3D.K.dot(vDirection) / (vDirection.lengthInSquare()))).norm();
        Vector3D visde;
        visde = vup.crossDot(vDirection).norm();
        vup = vDirection.crossDot(visde).norm();

        camera.setvSide(visde);
        camera.setViewUp(vup);

        camera.setPosition(Vector3D.point(-1,-1,-1));





    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        camera.update();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.green);

        Matrix4x4f cameraMat = camera.getCameraMat();


        Vector3D e1 = cameraMat.getColumn(0);
        System.out.println("e1 = " + e1);
        Vector3D e2 = cameraMat.getColumn(1);
        System.out.println("e2 = " + e2);

        Vector3D column = camera.getViewMat().getRow(1);
        Vector3D o = Vector3D.point(1,1,1);

        System.out.println("column = " + column);

        double v = o.dot(column) - camera.getPosition().x;
        System.out.println("v = " + v);


        double dot = e1.dot(column);
        System.out.println("dot = " + dot);


        Vector3D pi = o.add(
                e1.mul(1).
                        add(e2.mul(2))
        );



        Vector3D vector3D = camera.projectionToNDCSpace(pi);
        System.out.println("vector3D = " + vector3D);

        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestCarema02());
    }
}
