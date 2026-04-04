package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class TestAABB extends DiKaErPlus {

    AABB aabb,c;
    AABB aabb2;
    boolean contains;
    boolean moving,clicked,drag;
    Vector2D cp;

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

        Polygon sheared = aabb.getSheared(0.5, 0.5);
        drawPolyGon(g2,sheared,false);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestAABB());
    }
}
