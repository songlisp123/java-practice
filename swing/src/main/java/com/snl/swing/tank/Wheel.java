package com.snl.swing.tank;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class Wheel extends CircleObject {

    private boolean showPattern;
    private Vector2D[] temp;

    public Wheel() {
        super();
    }

    public Wheel(int edge, double radius) {
        super(edge, radius);
    }

    @Override
    protected void update(Component parentComponent) {
        double w = parentComponent.getW();
        double h = parentComponent.getH();
        if (h == 0 || w == 0)
            return;
        double dx = - w / 2.0 + getRadius();
        parentForm = parentComponent.modelToWorld().mul(
                Matrix3x3f.translate(dx,- h / 2.0)
        );
    }


    @Override
    public void update(double delta, Component parentComponent) {
        super.update(delta,parentComponent);
        if (isShowPattern()) {
            //TODO
            if (temp == null) {
                temp = new Vector2D[verticesCount()];
            }

            Vector2D[] scaled = getScaled(0.78);
            Matrix3x3f mTOWorld = modelToWorld();
            for (int i = 0; i< scaled.length; i++) {
                temp[i] = mTOWorld.mul(scaled[i]);
            }
        }
    }


    public boolean isShowPattern() {
        return showPattern;
    }

    public void setShowPattern(boolean showPattern) {
        this.showPattern = showPattern;
    }

    @Override
    public void draw(Graphics2D g2, DiKaErPlus d) {
        super.draw(g2, d);
        //绘制条纹
        if (isShowPattern()) {
            for (int i = 0; i < getEdge(); i++) {
                d.drawLine(g2, copy[i], temp[i]);
            }
        }
    }
}
