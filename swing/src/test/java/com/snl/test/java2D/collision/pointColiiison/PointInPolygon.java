package com.snl.test.java2D.collision.pointColiiison;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PointInPolygon extends DiKaErPlus {

    final List<Vector2D> posLists = new ArrayList<>();
    Vector2D pos;
    boolean collision;
    boolean clicked,dragging;

    public PointInPolygon() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        pos = new Vector2D(3,3);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (dragging)
        {
            Vector2D mouse = getMousePointInVector();
            posLists.add(mouse);
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        collision = pointInPoly(pos,posLists);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        drawPolygon(g2,posLists);
        if (collision)
            g2.setPaint(Color.RED);
        else
            g2.setPaint(Color.GREEN);
        drawCircle(g2,pos,.1);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        posLists.clear();
    }

    public static void main(String[] args) {
        launchGame(new PointInPolygon());
    }
}
