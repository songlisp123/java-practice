package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Convexity;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.utils.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class TestConversity extends DiKaErPlus {

    Convexity convexity,convexity02;
    boolean drag,cMoving,clicked,collision;

    Vector2D nr,testPoint;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        convexity = new Convexity(new Vector2D(),
                new Vector2D(1,2),new Vector2D(3,4),new Vector2D(-1,3));
        convexity.setShowVer(true);

        testPoint = new Vector2D(3,5);


        Iterator<Vector2D> iterator = convexity.getVertexIterator();
        while (iterator.hasNext()) {
            Vector2D next = iterator.next();
            System.out.println("next = " + next);
        }

        Vector2D center = convexity.getCenter();
        Vector2D averageCenter = Geometry.getAverageCenter(convexity);
        System.out.println("averageCenter.equals(center) = " + averageCenter.equals(center));

        convexity02 = new Convexity(
                new Vector2D(),new Vector2D(0,1),new Vector2D(2,3),new Vector2D(5,2)
        );
        convexity02.setShowVer(true);


    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Vector2D mouse = getMousePointInVector();
        boolean b = pointInPoly(mouse, convexity);
        if (clicked && b) {
            cMoving = true;
        }

        cMoving = cMoving && drag;
        if (cMoving) {
            //如果移动点
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            convexity.translate(d);
        }

        nr = convexity.getNearestPoint(testPoint);

        collision = convexity.collideOtherConvexity(convexity02);
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.yellow);
        drawConvexity(g2,convexity,false);
        drawConvexity(g2,convexity02,false);
        drawCircle(g2,convexity.getCenter(),0.15,true);
        AABB aabb = convexity.getAABB();
        AABB aabb1 = convexity02.getAABB();

        g2.setPaint(Color.CYAN);
        drawAAbb(g2,aabb,false);
        drawAAbb(g2,aabb1,false);

        drawCircle(g2,testPoint,0.10,true);
        drawCircle(g2,nr,0.05,true);

//        System.out.println("nr = " + nr);

        if (collision) {
            g2.setPaint(Color.RED);
            drawAAbb(g2,aabb,false);
            drawAAbb(g2,aabb1,false);
        }

        g2.dispose();
    }


    public static void main(String[] args) {
        launchGame(new TestConversity());
    }
}
