package com.snl.test.java2D.game;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PolyGonEditor extends DiKaErPlus {

    List<Vector2D> polys;
    boolean moving,dragging,clicking;
    Vector2D mouse;

    public PolyGonEditor() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        polys = new ArrayList<>();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        mouse = getMousePointInVector();
        clicking = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON3))
        {
            polys.add(mouse);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            //打印行星
            printPolygon();
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (clicking && pointInPoly(mouse,polys)) {
            moving = true;
        }
        moving = moving && dragging;
        if (moving)
        {
            Matrix3x3f rev = getReverseScaleViewPortMat();
            Vector2D v = rev.mul(mouseDelta);
            polys.replaceAll(v1->v1.add(v));
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        drawPoly(g2,polys,true);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    private void printPolygon() {
        System.out.println("Vector2f[] v = new Vector2f[] { ");
        for (Vector2D v : polys) {
            System.out.print(" new Vector2f(");
            System.out.print(v.getX() + "f, ");
            System.out.print(v.getY() + "f)");
            System.out.println(",");
        }
        System.out.println("};");
    }

    @Override
    protected void reset() {
        super.reset();
        polys.clear();
    }

    public static void main(String[] args) {
        launchGame(new PolyGonEditor());
    }
}
