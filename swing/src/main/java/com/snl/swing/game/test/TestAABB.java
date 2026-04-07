package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.*;
import com.snl.swing.game.math.Polygon;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class TestAABB extends DiKaErPlus {

    AABB aabb,c;
    AABB aabb2;
    boolean contains;
    boolean moving,clicked,drag;
    Vector2D cp;
    Circle circle;

    public TestAABB() throws HeadlessException {
        drawAxis = true;
        aabb = new AABB(
                new Vector2D(-1,-1),new Vector2D(1,1)
        );
        aabb2 = new AABB(
                new Vector2D(-1,3),new Vector2D(5,5)
        );
        c = new AABB(
                new Vector2D(-.5,-.5),new Vector2D(.5,.5)
        );
        cp = aabb.pickedRandomPoint(0.2,0.6);
        double[] subTriangleArea = aabb.getSubTriangleArea();
        System.out.println(Arrays.toString(subTriangleArea));

        circle = new Circle(0.56,new Vector2D(2,-2));
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
        contains = aabb.contains(c);
        Vector2D mouse = getMousePointInVector();
        if (clicked && aabb.containsPoint(mouse))
            moving = true;

        moving = moving && drag;
        if (moving)
        {
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D d = re.mul(mouseDelta);
            aabb.translate(d);
        }

        clicked= false;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        g2.setPaint(Color.cyan);
        drawAAbb(g2,aabb,false);
        if (contains)
            drawAAbb(g2,c,true);
        g2.setColor(Color.red);
        drawAAbb(g2,aabb2,false);
        drawCircle(g2,cp,.1,true);

//        Polygon sheared = aabb.getSheared(0.5, 0.5);
//        drawPolyGon(g2,sheared,false);
        boolean b = aabb.collisionAABB(aabb2);
        if (b) {
            AABB intersection = aabb.intersection(aabb2);
            drawAAbb(g2,intersection,true);
        }

        Vector2D center = aabb.getCenter();
        drawCircle(g2,center,0.05,true);

        drawCircle(g2,circle,false);

        if (aabb.collisionCircle(circle)) {
            System.out.println("相撞了");
        }

        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        aabb.reset();
    }

    public static void main(String[] args) {
        launchGame(new TestAABB());
    }
}
