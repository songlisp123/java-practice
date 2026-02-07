package com.snl.test.TIMEANDSPACE.UTIL;

import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Star {

    int alpha;
    Color color;
    Vector2D pos;
    boolean shinning;

    public Star(Vector2D pos) {
        this.pos = pos;
        this.alpha = RandomGeneratorClass.random(255);
        this.color = Color.white;
    }

    public Star(Vector2D pos, Color color) {
        this.pos = pos;
        this.color = color;
    }

    public Star(int alpha, Color color, Vector2D pos) {
        this.alpha = alpha;
        this.color = color;
        this.pos = pos;
    }

    public void draw(Graphics g, Matrix3x3f mat) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        Vector2D v = mat.mul(this.pos);
        double centerX = v.getX();
        double centerY = v.getY();
        Shape s = new Ellipse2D.Double(
                centerX - 1,centerY - 1,2,2
        );
        g2.fill(s);
    }

    public void update(double delta) {
        if (!shinning)
        {
            alpha -= RandomGeneratorClass.random(10);
            if (alpha <= 80)
            {
                alpha = 80;
                shinning = !shinning;
            }
        }
        else {
            alpha += RandomGeneratorClass.random(10);
            if (alpha >= 200)
            {
                alpha = 200;
                shinning = !shinning;
            }
        }
        color = new Color(
                this.color.getRed(),
                this.color.getGreen(),
                this.color.getBlue(),
                alpha
        );
    }

}
