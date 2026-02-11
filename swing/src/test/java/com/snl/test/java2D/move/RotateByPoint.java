package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RotateByPoint extends DiKaErPlus {

    Vector2D[] poly,polyCopy;
    Vector2D cord;
    double rot,theta;


    public RotateByPoint() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        poly = new Vector2D[]{
                new Vector2D(0,0.25),new Vector2D(2,0.25),
                new Vector2D(2,-0.25),new Vector2D(0,-0.25)
        };
        polyCopy = new Vector2D[poly.length];
        rot = 0;
        theta = Math.PI / 4;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1))
        {
            //按下左键
            Matrix3x3f mat = getReverseWorldTransForm();
            cord = mat.mul(mousePos);
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += theta*delta;
        Matrix3x3f mat = Matrix3x3f.rotate(this.rot);
        for (int i=0;i<polyCopy.length;i++) {
            polyCopy[i] = poly[i].sub(new Vector2D(2,2));
            polyCopy[i] = mat.mul(polyCopy[i]);
        }
        Matrix3x3f view = getViewportTransform();
        for (int i=0;i<polyCopy.length;i++) {
            polyCopy[i] = polyCopy[i].add(new Vector2D(2,2));
            polyCopy[i] = view.mul(polyCopy[i]);
        }

    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        //TODO
        g2.setPaint(Color.WHITE);
        drawPolygon(g2,polyCopy);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new RotateByPoint());
    }
}
