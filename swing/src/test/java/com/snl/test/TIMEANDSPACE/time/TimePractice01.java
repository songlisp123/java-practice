package com.snl.test.TIMEANDSPACE.time;

import com.snl.test.frame.SimpleGameFramePlus;
import com.snl.test.vwctor.Matrix3x3f;
import com.snl.test.vwctor.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;

public class TimePractice01 extends SimpleGameFramePlus implements MouseWheelListener {

    Vector2D c0,c0Pos;
    double r0;
    boolean c0Moving,c0Collision;
    boolean clicked,sDragging;
    double rot,theta;
    boolean stopping;

    public TimePractice01() throws HeadlessException {
        super();
        addMouseWheelListener(this);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        resetPos();
        //创建轴
        r0 = 1;
        rot = 0;
        theta = Math.PI / 4;
    }

    private void resetPos() {
        c0Pos = new Vector2D(3,3);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //TODO
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        sDragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            stopping = !stopping;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //TODO 关键是如何移动世界坐标？？？
        //现在我的坐标系写死了，当我按下鼠标中建的时候，会产生拖动事件，
        //拖动事件应该渲染当前的世界坐标系，而不是简单的放大减小。该如何完成呢？
        Matrix3x3f mat = Matrix3x3f.translate(c0Pos.getX(), c0Pos.getY());
        c0 = mat.mul(new Vector2D());

        if (!stopping) {
            if (clicked && pointInCircle(c0, r0))
                c0Moving = true;

            rot += theta * delta;

            c0Moving = c0Moving && sDragging;
            // 像素 → 世界单位
            Matrix3x3f scale = getReverseScaleViewPortMat();
            Vector2D move = scale.mul(mouseDelta);
            if (c0Moving)
                c0Pos = c0Pos.add(move);
            c0Collision = circleCollision(c0, r0);
        }
    }

    private boolean circleCollision(Vector2D c, double r) {
        //判断左边
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        Vector2D c1 = rotate.mul(c);
        Vector2D vx = new Vector2D(c1.getX(),0);
        Vector2D vy = new Vector2D(0,c1.getY());
        double d = Math.pow(r,2);
        return vy.lenSqr() <= d||
                vx.lenSqr() <= d;

    }

    private boolean pointInCircle(Vector2D c, double r) {
        Matrix3x3f transForm = getReverseWorldTransForm();
        Vector2D v = transForm.mul(mousePos);
        v = v.sub(c);
        return v.lenSqr() < Math.pow(r,2);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(Color.WHITE);
        //todo
        g2.drawString("运动模式:[%s]".formatted(!stopping),30,130);
        g2.drawString("这是一个测试圆周运动的例子，圆以每秒四分之pi运动",30,150);
        drawCircle(g2,c0,r0);
        g2.dispose();
    }

    private void drawCircle(Graphics2D g2, Vector2D c, double r) {
        g2.setColor(c0Collision?Color.red:Color.green);
        Matrix3x3f vt = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        vt = vt.mul(rotate);
        Vector2D c1 = rotate.mul(c);
        Vector2D p = vt.mul(c);
        Vector2D v = new Vector2D(2 * r,2 * r);
        v = scale.mul(v);
        //获取坐标
        double w = v.getX();
        double h = v.getY();
        double leftX = p.getX() - w / 2.0;
        double leftY = p.getY() - h / 2.0;
        Shape s = new Ellipse2D.Double(
                leftX,leftY,w,h
        );
        g2.drawString("[%.2f,%.2f]".formatted(c1.getX(),c1.getY()),
                (int) p.getX(), (int) (p.getY() - 10));
        Shape radius = new Ellipse2D.Double(
                p.getX()-2,p.getY()-2,4,4
        );
        g2.draw(s);
        g2.fill(radius);
    }

    @Override
    protected void reset() {
        super.reset();
        resetPos();
    }

    public static void main(String[] args) {
        launchGame(new TimePractice01());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            wordWidth--;
            wordHeight--;
            axis.createAxis(getViewportTransform(),c);
        }

        if (wheelRotation == 1) {
            wordWidth++;
            wordHeight++;
            axis.createAxis(getViewportTransform(),c);
        }

        if (wordWidth <= 2)
            wordWidth = wordHeight = 2;

        if (wordWidth == WIDTH || wordHeight == HEIGHT)
        {
            wordWidth = WIDTH;
            wordHeight = HEIGHT;
        }
    }
}
