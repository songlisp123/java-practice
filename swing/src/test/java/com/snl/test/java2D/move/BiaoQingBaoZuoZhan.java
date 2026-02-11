package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class BiaoQingBaoZuoZhan extends DiKaErPlus {

    boolean moving;
    ImageIcon image01,image02,image03,image04,image05;
    ImageIcon[] images;
    Vector2D radius,c0,c0Copy;
    double rot,theta;
    Vector2D[] pol,polCopy,polyCopy02;
    double start,end;
    int index;
    Vector2D playPos,playerSpeed,playerDeltaX,playerDeltaY;
    boolean leftMoving,rightMoving,upMoving,downMoving;
    boolean speeding;
    double speedFractor;
    double rot02,theta02;
    Vector2D radiusRotate;

    public BiaoQingBaoZuoZhan() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        moving = true;
        image01 = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        image03 = new ImageIcon("images/愤怒.png" );
        image04 = new ImageIcon("images/疑问.png" );
        image05 = new ImageIcon("images/傻笑.png" );
        radius = new Vector2D(5,0);
        images = new ImageIcon[]{image01,image02,image03,image04,image05};
        c0Copy = new Vector2D(3.5,4.3);
        theta = Math.PI / 4;
        pol = new Vector2D[]{
                new Vector2D(-0.25,.8),new Vector2D(0.25,.8),
                new Vector2D(0.0,.55)
        };
        polCopy = new Vector2D[pol.length];
        polyCopy02 = new Vector2D[pol.length];
        playPos = new Vector2D();
        playerSpeed =  new Vector2D(1.3,1.2);
        speeding = false;
        speedFractor = 2.0;
        theta02 = Math.PI / 2;
        radiusRotate = new Vector2D(1.2,1.2);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }
        leftMoving = keyBoardEvent.keyDown(KeyEvent.VK_A);
        rightMoving = keyBoardEvent.keyDown(KeyEvent.VK_D);
        upMoving = keyBoardEvent.keyDown(KeyEvent.VK_W);
        downMoving = keyBoardEvent.keyDown(KeyEvent.VK_S);
        speeding = keyBoardEvent.keyDown(KeyEvent.VK_SHIFT);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot02 += theta02 * delta;
        viewMat = Matrix3x3f.translate(playPos.inv());
        axis.createAxis(getViewportTransform(),c,wordWidth);
        if (moving) {
            rot += theta * delta;
            updateImage(delta);
            Matrix3x3f mat = Matrix3x3f.rotate(this.rot);
            Vector2D c = c0Copy.sub(radiusRotate);
            c0 = mat.mul(c);
            c0 = c0.add(radiusRotate);
        }
        Matrix3x3f rotate = Matrix3x3f.rotate(rot02);
        handlePolyRotate(pol,polCopy,rotate);
        handleView02(c0,polCopy);
        updatePlayer(delta);
        handlePolyRotate(pol,polyCopy02,rotate);
        handleView02(playPos,polyCopy02);
    }

    private void updatePlayer(double delta) {
        if (leftMoving) {
            if (speeding)
                updatePlayerMoveX(-delta,speedFractor);
            else
                updatePlayerMoveX(-delta,1);
        }
        else if (rightMoving) {
            if (speeding)
                updatePlayerMoveX(delta, speedFractor);
            else
                updatePlayerMoveX(delta,1);
        }
        if (upMoving) {
            if (speeding)
                updatePlayerMoveY(delta,speedFractor);
            else
                updatePlayerMoveY(delta,1);
        }
        else if (downMoving) {
            if (speeding)
                updatePlayerMoveY(-delta, speedFractor);
            else
                updatePlayerMoveY(-delta,1);
        }
    }

    void handlePolyRotate(Vector2D[] pol,Vector2D[] copy,Matrix3x3f mat) {
        if (pol == null || copy == null || mat == null)
            return;
        if (pol.length != copy.length)
            return;
        for (int i=0;i<copy.length;i++) {
            copy[i] = mat.mul(pol[i]);
        }
    }

    private void handleView02(Vector2D playPos, Vector2D[] poly) {
        for (int i=0;i<poly.length;i++)
        {
            poly[i] = poly[i].add(playPos);
        }
    }

    private void updatePlayerMoveY(double delta, double f) {
        double dy = playerSpeed.getY() * f * delta;
        playerDeltaY = new Vector2D(0,dy);
        playPos = playPos.add(playerDeltaY);
    }

    private void updatePlayerMoveX(double delta, double f) {
        double dx = playerSpeed.getX() * f * delta;
        playerDeltaX = new Vector2D(dx,0);
        playPos = playPos.add(playerDeltaX);
    }

    private void updateImage(double delta) {
        end += delta;
        if (end - start >= 1) {
            image01 = images[(++index) % images.length];
            start = end;
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        drawImage(g2,image01.getImage(),c0);
        drawImage(g2,image01.getImage(),playPos);
        g2.setColor(Color.cyan);
        g2.drawString("按下 SPACE 开始",30,130);
        g2.drawString("按下 W/A/S/D 行走",30,150);
        g2.drawString("按下 SHIFT 加速",30,170);
        drawPoly(g2,polCopy,true);
        g2.setPaint(Color.MAGENTA);
        drawPoly(g2,polyCopy02,true);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        rot = 0;
        rot02 = 0;
    }

    public static void main(String[] args) {
        launchGame(new BiaoQingBaoZuoZhan());
    }
}
