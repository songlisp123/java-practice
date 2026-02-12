package com.snl.test.java2D.collision;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CollisionTestDemo01 extends DiKaErPlus {

    Vector2D[] poly,polyCopy,copy2;
    double rot,theta;
    Vector2D pos;
    int mode;
    Vector2D cord;

    public CollisionTestDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        initial();
        theta = Math.PI / 4;
        polyCopy = new Vector2D[]{
                new Vector2D(-1,1),new Vector2D(1,1),
                new Vector2D(1,-1),new Vector2D(-1,-1)
        };
        poly = new Vector2D[polyCopy.length];
        copy2 = new Vector2D[polyCopy.length];
        resetView();
    }

    private void initial() {
        pos = new Vector2D(3,3);
        cord = pos;
        rot = 0;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1))
        {
            cord = getMousePointInVector();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_1))
        {
            mode = 0;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_2))
        {
            mode = 1;
        }

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += theta * delta;
        handleMove();
    }

    private void handleMove() {
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        if (mode == 0) {
            for (int i = 0; i < polyCopy.length; i++) {
                poly[i] = rotate.mul(polyCopy[i]).add(cord);
            }
        } else if (mode == 1) {
            for (int i = 0; i < polyCopy.length; i++) {
                copy2[i] = polyCopy[i].add(pos);
                poly[i] = rotate.mul(copy2[i].sub(cord)).add(cord);
            }
        }
    }

    @Override
    protected void reset() {
        super.reset();
        initial();
        resetView();
    }

    @Override
    protected void resetView() {
        viewMat = Matrix3x3f.translate(0,-wordHeight / 2.0);
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setColor(Color.cyan);
        drawPoly(g2,poly,false);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.drawString("%s".formatted(mode == 0?"以某点运动":"饶某点运动"),30,150);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new CollisionTestDemo01());
    }
}
