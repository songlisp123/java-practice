package com.snl.swing.game.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.utils.Geometry;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class TestGeom extends DiKaErPlus {

    boolean drag,cMoving,clicked,collision;
    Polygon polygon;
    Vector2D center,ac;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        polygon = new Polygon(new Vector2D[]{
                new Vector2D(0,3),
                new Vector2D(2,2),
                new Vector2D(3,0),
                new Vector2D(2,-2),
                new Vector2D(0,-3),
                new Vector2D(-2,-2),
                new Vector2D(-3,0),
                new Vector2D(-2,9)
        });
        polygon = new Polygon(new Vector2D[]{
                new Vector2D(-3,1),
                new Vector2D(-1,3),
                new Vector2D(2,3),
                new Vector2D(4,1),
                new Vector2D(3,-2),
                new Vector2D(1,-3),
                new Vector2D(-2,-2)
        });
        //重心
       center =  Geometry.getAverageCenter(polygon);
       //质心
        ac = Geometry.getAreaWeightedCenter(polygon);
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

    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.yellow);
        drawPolyGon(g2,polygon,false);
        drawCircle(g2,center,0.1,false);
        drawCircle(g2,ac,0.15,false);
        if (collision) {
            g2.setPaint(Color.RED);
        }

        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new TestGeom());
    }
}
