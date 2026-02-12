package com.snl.test.java2D.move;

import com.snl.test.java2D.UTIL.Standards;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.shape.HuoJian;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HuoJianShenKong extends DiKaErPlus {

    HuoJian huoJian;
    double a; //加速度
    boolean firing;
    double oldXSpeed,oldYSpeed;
    Vector2D distanceX,distanceY;
    Vector2D speed;
    double rot,theta;

    public HuoJianShenKong() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetView();
        theta = Math.PI / 4;
    }

    @Override
    protected void reset() {
        super.reset();
        resetView();
    }

    protected void resetView() {
        rot = 0;
        huoJian = new HuoJian(wordWidth,wordHeight);
        a = 20;
        speed = new Vector2D(0,0);
        distanceY = new Vector2D();
        viewMat = Matrix3x3f.translate(0,-wordHeight / 2.0);
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            firing = true;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);

        rot += theta * delta;
        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        huoJian.rotate(mat);
        if (firing) {
            moveX(delta);
            moveY(delta);
        }
    }

    private void moveY(double delta) {
        oldYSpeed = speed.getY();
        double newYSpeed = oldYSpeed  + ((a - Standards.GRAVITY) * delta);
        double dy = (newYSpeed + oldYSpeed) * delta / 2.0;
        distanceY = distanceY.add(new Vector2D(0,dy));
        speed = new Vector2D(speed.getX(),newYSpeed);
        huoJian.addPos(distanceY);
    }

    private void moveX(double delta) {
        //TODO
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setColor(Color.cyan);
        drawPoly(g2, huoJian.getOutShape(), false);
        drawPoly(g2, huoJian.getShapes(), true);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new HuoJianShenKong());
    }
}
