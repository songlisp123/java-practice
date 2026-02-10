package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class XiePaoYunDongDemo01 extends DiKaErPlus {

    Vector2D[] poly,polyCopy;
    private Vector2D bullet,bulletCopy,bulletStart;
    Vector2D velocity; //子弹速度
    double rot,theta;
    double oldYSpeed,dx,dy;//子弹属性

    public XiePaoYunDongDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        initPos();
        theta = Math.PI / 4;
    }

    private void initPos() {
        poly = new Vector2D[]{
                new Vector2D(0,0.25),new Vector2D(2,0.25),
                new Vector2D(2,-0.25),new Vector2D(0,-0.25)
        };
        polyCopy = new Vector2D[poly.length];
        rot = 0;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A))
        {
            rot += theta;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_D))
        {
            rot -= theta;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            if (bullet == null) {
                Matrix3x3f mat = Matrix3x3f.identity();
                mat = mat.mul(Matrix3x3f.rotate(rot));
                mat = mat.mul(Matrix3x3f.translate(7.0F, 0F));
                velocity = mat.mul(new Vector2D());
                //世界坐标
                Vector2D muzzleLocal = new Vector2D(2, 0);
                Matrix3x3f model = Matrix3x3f.identity()
                        .mul(Matrix3x3f.rotate(rot));
                bulletStart = model.mul(muzzleLocal);
                bullet = new Vector2D(bulletStart);
            }
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        handle();
        if (bullet != null)
        {
            oldYSpeed = velocity.getY();
            double newYSpeed = oldYSpeed + -9.8 * delta;
            velocity = new Vector2D(velocity.getX(),newYSpeed);
            dx += velocity.getX() * delta;
            dy +=  (newYSpeed + oldYSpeed) * delta / 2;
            bullet.setX(bulletStart.getX() + dx);
            bullet.setY(bulletStart.getY() + dy);
            bulletCopy = getViewportTransform().mul(bullet);
            if (bullet.getY() <= -8f) {
                bullet = null;
                dx = dy = 0;
            }
        }
    }

    private void handle() {
        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        for (int i=0;i<polyCopy.length;i++)
        {
            polyCopy[i] = mat.mul(poly[i]);
        }
        mat = getViewportTransform();
        for (int i=0;i<polyCopy.length;i++)
        {
            polyCopy[i] = mat.mul(polyCopy[i]);
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setColor(Color.PINK);
        g2.setStroke(new BasicStroke(2));
        drawPolygon(g2,polyCopy);
        g2.setColor(Color.cyan);
        g2.drawString("按下 A/D 键旋转",30,130);
        g2.setColor(Color.WHITE);
        if (bullet != null)
        {
            g2.fillRect(
                    (int) (bulletCopy.getX() - 2), (int) (bulletCopy.getY() - 2),
                    4,4
            );
        }
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        initPos();
    }

    public static void main(String[] args) {
        launchGame(new XiePaoYunDongDemo01());
    }
}
