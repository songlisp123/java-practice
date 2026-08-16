package com.snl.swing.game2026.comp;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;

public class ScrollPanel {

    private int xLeft,yLeft;
    private int wight,height;
    private final Mode mode;
    private int borderWeight;

    private Scrollbar bar;

    public ScrollPanel(int xLeft, int yLeft, int wight, int height, Mode mode) {
        this.xLeft = xLeft;
        this.yLeft = yLeft;
        this.wight = wight;
        this.height = height;
        this.mode = mode;
        borderWeight = 3;
    }

    public enum Mode {
        VERTICAL,HORIZONTAL
    }


    public void draw(Graphics2D g2, Matrix3x3f view) {
        float screenX,screenY,sw,sh;
        Vector2D c0 = view.getColumn(0);
        Vector2D c1 = view.getColumn(1);
        Vector2D c2 = view.getColumn(2);

        screenX = (float) (xLeft * c0.x + yLeft * c1.x + c2.x);
        screenY = (float) (xLeft * c0.y + yLeft * c1.y + c2.y);

        sw = (float) (wight * c0.x ); //x轴缩放
        sh =- (float) (height * c1.y ); //Y轴缩放，反转Y轴


        g2.drawRect((int) screenX , (int) screenY , (int) sw , (int) sh );
        g2.drawLine((int) (screenX + sw * 9 / 10), (int) screenY ,
                (int) (screenX + sw * 9 / 10), (int) (screenY + sh));
    }
}
