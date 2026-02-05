package com.snl.test.TIME.coords;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;

public class CannonDemo extends DiKaEr {

    final double THETA = Math.PI / 2;
    private Vector2D[] cannon;
    private Vector2D[] cannonCopy;
    private double rot,rotDelta;
    private Vector2D bullet,bulletCopy,bulletStart;
    private Vector2D velocity;
    double dx,dy,oldYSpeed;

    public CannonDemo() throws HeadlessException {
        super();
        initial();
    }

    private void initial() {
        rot = 0;
        rotDelta = THETA;
        velocity = new Vector2D();

        //维护四个点？
//        cannon = new Point2D[]{
//                new Point2D.Double(-0.25,1.25),//左上角，
//                new Point2D.Double(0.25,1.25),//右上角，
//                new Point2D.Double(0.25,.0),//右下角，
//                new Point2D.Double(-0.25,.0),//左下角，
//        };
//
//        bullet = new Point2D[]{
//                new Point2D.Double(-0.05,1.25),
//                new Point2D.Double(.05,1.25),
//                new Point2D.Double(.05,1.0),
//                new Point2D.Double(-.05,1.0),
//        };

        /* 维护四个向量  */
        cannon = new Vector2D[]{
                new Vector2D(0,.25),
                new Vector2D(1.5,.25),
                new Vector2D(1.5,-.25),
                new Vector2D(0,-.25),
        };

        cannonCopy = new Vector2D[cannon.length];

    }

    @Override
    public void processInput(double delta) {
        super.processInput(delta);
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A))
        {
            rot += rotDelta / 2 ;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_D))
        {
            rot -= rotDelta / 2;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            if (bullet == null) {
                Matrix3x3f mat = Matrix3x3f.identity();
                mat = mat.mul(Matrix3x3f.rotate(rot));
                mat = mat.mul(Matrix3x3f.translate(7.0F, 0F));
                velocity = mat.mul(new Vector2D());
                //世界坐标
                Vector2D muzzleLocal = new Vector2D(1.5, 0);
                Matrix3x3f model = Matrix3x3f.identity()
                        .mul(Matrix3x3f.rotate(rot));

                bulletStart = model.mul(muzzleLocal);
                bullet = new Vector2D(bulletStart);
            }
        }
    }

    @Override
    public void updateSprite(double delta) {
        super.updateSprite(delta);
//        TODO
//        Matrix3x3f mat = Matrix3x3f.identity();
//        mat = mat.mul(Matrix3x3f.rotate(rot));
//        for (int i = 0; i < cannon.length; ++i) {
//            copyOfWorld[i] = mat.mul(copyOfWorld[i]);
//        }
//
//        if (bullet != null)
//        {
//            //子弹逻辑
//            double y = velocity.getY() * -9.8 * delta;
//            velocity.setY(y);
//            double x = bullet.getX() * delta;
//            bullet.setX(x);
//            double y2 = velocity.getY() * delta;
//            bullet.setY(y2);
//            bulletsCopy = new Vector2D(bullet);
//            if (bullet.getY() < 2.5) {
//                bullet = null;
//            }
//        }
        //更新点
//        AffineTransform at;
//        af = AffineTransform.getRotateInstance(rot,
//                c.getWidth() / 2.0,c.getHeight() / 2.0);
//        at = (AffineTransform) af.clone();
//        for (Point2D p : cannon)
//        {
//            af.deltaTransform(p,p);
//        }
//
//        dx += velocity.getX() * delta;
//        oldYSpeed = velocity.getY();
//        double ySpeed = -9.8 * delta + oldYSpeed;
//        dy += (ySpeed + oldYSpeed) * delta / 2;
//        at.translate(dx,dy);
//        for (Point2D p : bullet)
//        {
//            at.transform(p,p);
//        }
        //第二种方法，自己维护向量
        Matrix3x3f mat = Matrix3x3f.identity();
        mat = mat.mul(Matrix3x3f.rotate(rot));
        for (int i=0;i<cannonCopy.length;i++) {
            cannonCopy[i] = mat.mul(cannon[i]);
        }

        if (bullet != null)
        {
            oldYSpeed = velocity.getY();
            double newYSpeed = oldYSpeed + -9.8 * delta;
            velocity = new Vector2D(velocity.getX(),newYSpeed);
            dx += velocity.getX() * delta;
            dy +=  (newYSpeed + oldYSpeed) * delta / 2;
            bullet.setX(bulletStart.getX() + dx);
            bullet.setY(bulletStart.getY() + dy);
            bulletCopy = new Vector2D(bullet);
            if (bullet.getY() <= -8f) {
                bullet = null;
                dx = dy = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setColor(Color.MAGENTA);
        g2.drawString("按下 a 键 左转",30,170);
        g2.drawString("按下 d 键 右转",30,190);
//        Matrix3x3f view = Matrix3x3f.identity();
//        view = view.mul(Matrix3x3f.translate(w / 2.0,h / 2.0));
//        view = view.mul(Matrix3x3f.scale(100,-100));
//        g2.setStroke(new BasicStroke(2));
//        drawPolygon(g2,cannon);
//        g2.setColor(Color.WHITE);
//        if (bullet != null) {
//            bulletsCopy = view.mul(bulletsCopy);
//            g.drawRect((int) bulletsCopy.getX() - 2,
//                    (int) bulletsCopy.getY()- 2, 4, 4);
//        }
        double w = c.getWidth() / 2.0;
        double h = c.getHeight()  / 2.0;
        Matrix3x3f view = Matrix3x3f.identity();
        view = view.mul(Matrix3x3f.translate(w,h));
        view = view.mul(Matrix3x3f.scale(camera.scale,-camera.scale));
        for (int i=0;i<cannon.length;i++) {
            cannonCopy[i] = view.mul(cannonCopy[i]);
        }
        drawPolygon(g2,cannonCopy);
        g2.setColor(Color.WHITE);
        if (bullet != null)
        {
            bulletCopy = view.mul(bulletCopy);
            g2.fillRect(
                    (int) (bulletCopy.getX() - 2), (int) (bulletCopy.getY() - 2),
                    4,4
            );
        }
        g2.dispose();
    }

    private void drawPolygon(Graphics2D g2, Vector2D[] polygon) {
        g2.setColor(Color.MAGENTA);
        Vector2D p;
        Vector2D f = polygon[polygon.length -1];
        for (Vector2D v : polygon) {
            p = v;
            Line2D l = new Line2D.Double(
                    f.getX(),f.getY(),
                    p.getX(),p.getY()
            );
            g2.draw(l);
            f = p;
        }
    }

    @Override
    public void reset() {
        super.reset();
        rot = 0;
        dx = dy = 0;
        bullet = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CannonDemo::new);
    }
}
