package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ZhiXianYUnDongDemo01 extends DiKaErPlus {

    Vector2D pos;
    ImageIcon image01,image02,image03,image04,image05;
    Vector2D v1,v2,v3,v4,v5;
    Vector2D v1Pos,v2Pos,v3Pos,v4Pos,v5Pos;
    //挑选那个人作为中心
    int mode;
    boolean following;

    boolean moving;
    Vector2D[] pol,polCopy;

    public ZhiXianYUnDongDemo01() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        //TODO 待做
        pos = new Vector2D();
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        image03 = new ImageIcon("images/愤怒.png" );
        image04 = new ImageIcon("images/疑问.png" );
        image05 = new ImageIcon("images/傻笑.png" );
        initialSpeed();
        following = true;
    }

    private void initialSpeed() {
        v1Pos = v2Pos = v3Pos = v4Pos = v5Pos = new Vector2D();
        v1 = new Vector2D(1.32,0.0);
        v2 = new Vector2D(0.2,0.0);
        v3 = new Vector2D(0.6,0.0);
        v4 = new Vector2D(0.8,0.0);
        v5 = new Vector2D(1.0,0.0);
        mode = 5;
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        //TODO
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_H))
        {
            following = false;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_T))
        {
            if (!following)
                following = true;
            mode++;
            if (mode >= 6)
                mode = 1;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (following) {
            switch (mode) {
                case 2:
                    handleView(v2Pos);
                    break;
                case 3:
                    handleView(v3Pos);
                    break;
                case 4:
                    handleView(v4Pos);
                    break;
                case 5:
                    handleView(v5Pos);
                    break;
                case 1:
                    handleView(v1Pos);
                    break;
            }
        }
        if (moving) {
            v1Pos = handleSpeed(delta, v1, v1Pos);
            v2Pos = handleSpeed(delta, v2, v2Pos);
            v3Pos = handleSpeed(delta, v3, v3Pos);
            v4Pos = handleSpeed(delta, v4, v4Pos);
            v5Pos = handleSpeed(delta, v5, v5Pos);
        }
        Matrix3x3f view = getViewportTransform();
        for (int i=0;i<polCopy.length;i++)
        {
            polCopy[i] = view.mul(polCopy[i]);
        }
    }

    private void handleView(Vector2D pos) {
        viewMat = Matrix3x3f.translate(-pos.getX(),pos.getY());
        axis.createAxis(getViewportTransform(),c,wordWidth);
        for (int i=0;i<polCopy.length;i++)
        {
            polCopy[i] = pos.add(pol[i]);
        }
    }

    private Vector2D handleSpeed(double delta, Vector2D v, Vector2D pos) {
        //距离
        double dx = v.getX()  * delta;
        return pos.add(new Vector2D(dx,0));
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        drawImage(g2,image01.getImage(),v1Pos);
        drawImage(g2,image02.getImage(),v2Pos);
        drawImage(g2,image03.getImage(),v3Pos);
        drawImage(g2,image04.getImage(),v4Pos);
        drawImage(g2,image05.getImage(),v5Pos);
        g2.setColor(Color.cyan);
        g2.drawString("按下 SPACE 开始",30,130);
        g2.drawString("按下 T 键变更角色",30,150);
        g2.drawString("按下 P 键暂停",30,170);
        drawPolygon(g2,polCopy);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        initialSpeed();
    }

    public static void main(String[] args) {
        launchGame(new ZhiXianYUnDongDemo01());
    }
}
