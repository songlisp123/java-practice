package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;

public class TuoYuanYunDong extends DiKaErPlus {

    double ra,rb;
    Vector2D c0,c0Pos,c1;
    double rot,theta;
    ImageIcon image01,image02;
    Vector2D speed;
    Vector2D[] pol,polCopy;
    double rot02,theta02;

    public TuoYuanYunDong() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        ra = 5;
        rb = 2;
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        theta = Math.PI / 4;
        theta02 = Math.PI / 2;
        initialPos();
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
    }

    private void initialPos() {
        c0Pos = new Vector2D();
        c1 = new Vector2D();
        speed = new Vector2D(1,0);
        rot02 = 0;
        rot = 0;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += theta * delta;
        rot02 += theta02 * delta;
        double dx = speed.getX() * delta;
        c1 = c1.add(new Vector2D(dx,0));
        Matrix3x3f ro = Matrix3x3f.rotate(rot02);
        for (int i=0;i<polCopy.length;i++)
        {
            polCopy[i] = ro.mul(pol[i]);
        }
        handleView(c1);
        Matrix3x3f mat = Matrix3x3f.scale(ra,rb);
        mat = mat.mul(Matrix3x3f.rotate(rot));
        mat = mat.mul(Matrix3x3f.translate(1,0));
        Vector2D v = mat.mul(new Vector2D());
        c0 = c1.add(v);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.cyan);
        drawImage(g2,image02.getImage(),c1);
        drawImage(g2,image01.getImage(),c0);
        drawPolygon(g2,polCopy);
        g2.drawString("按下 A/D 键旋转",30,130);
        g2.setColor(Color.WHITE);
        g2.dispose();
    }

    private void handleView(Vector2D pos) {
        Matrix3x3f view = getViewportTransform();
        for (int i=0;i<polCopy.length;i++)
        {
            polCopy[i] = view.mul(pos.add(polCopy[i]));
        }
    }

    public static void main(String[] args) {
        launchGame(new TuoYuanYunDong());
    }
}
