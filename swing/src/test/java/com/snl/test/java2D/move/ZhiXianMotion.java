package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

public class ZhiXianMotion extends DiKaErPlus {

    /**
     * 运动模式：0直线运动，1直线匀加速/匀减速运动,2抛物线运动
     */
    int moveMode;

    Vector2D c0,c0Pos;
    double r0;
    boolean moving;
    Vector2D speed,speedCopy;
    ImageIcon image;
    AttributedString as;
    TextLayout layout;
    Vector2D playerSpeed;
    boolean playerMovingLeft, playerMovingRight,jumping;
    double oldYSpeed;
    Vector2D playerPos;
    boolean onGround;
    ImageIcon image02;
    ImageIcon image03;
    ImageIcon image04;
    ImageIcon image05;
    Vector2D c1,c1Pos;
    Vector2D speed1;
    double c1OldXSpeed;
    double a;

    double rot,theta;
    Vector2D c2Pos,c3Pos,c4Pos,c5Pos;
    double r2,guiDao;

    Vector2D radiusVec; // 半径向量（长度固定）
    Vector2D radiusVec02;

    public ZhiXianMotion() throws HeadlessException {
        appSleep = 15;
        appFont = new Font("宋体",Font.BOLD,15);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        r0 = 0.25;
        moveMode = 0;
        a = 0.01;
        rot = 0;
        r2 = 0.25;
        guiDao = 2.5;
        image = new ImageIcon("images/龙王.png");
        image02 = new ImageIcon("images/咧嘴笑.png");
        image03 = new ImageIcon("images/愤怒.png");
        image04 = new ImageIcon("images/疑问.png");
        image05 = new ImageIcon("images/傻笑.png");

        as = new AttributedString("我是*傻逼*");
        as.addAttribute(TextAttribute.FOREGROUND,Color.RED);
        theta = Math.PI / 2;
        initialPos();
    }

    private void initialPos() {
        c0Pos = new Vector2D();
        c1Pos = new Vector2D();
        c2Pos = new Vector2D(0,0);
        c3Pos = new Vector2D();
        playerPos = new Vector2D();
        speed = new Vector2D(1,0);
        speed1 = new Vector2D(0,0);
        playerSpeed = new Vector2D(2,0);
        radiusVec = new Vector2D(r2,0);
        radiusVec02 = new Vector2D(0,r2);
        onGround = true;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = true;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_P))
        {
            moving = false;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R))
        {
            //居中小球
            viewMat = Matrix3x3f.translate(-c0.getX(),c0.getY());
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            a++;
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
        {
            a--;
        }
        playerMovingLeft = keyBoardEvent.keyDown(KeyEvent.VK_D);
        playerMovingRight = keyBoardEvent.keyDown(KeyEvent.VK_A);
        jumping = keyBoardEvent.keyDown(KeyEvent.VK_W);

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        //更新视图矩阵
        viewMat = Matrix3x3f.translate(-c0Pos.getX(),-c0Pos.getY());
        axis.createAxis(getViewportTransform(),c,wordWidth);
        //更新c0位置
        Matrix3x3f m = Matrix3x3f.translate(c0Pos.getX(), c0Pos.getY());
        c0 = m.mul(new Vector2D());

        m = Matrix3x3f.translate(c1Pos.getX(), c1Pos.getY());
        c1 = m.mul(new Vector2D());

        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        mat = mat.mul(Matrix3x3f.translate(guiDao,0));
        Vector2D v = mat.mul(radiusVec);
        mat = mat.mul(Matrix3x3f.translate(-2 * guiDao,0));
        Vector2D v2 = mat.mul(radiusVec02);

        Matrix3x3f mt = Matrix3x3f.identity();
        mt = mt.mul(Matrix3x3f.rotate(rot));
        mt = mt.mul(Matrix3x3f.translate(0, guiDao));
        Vector2D v3 = mt.mul(radiusVec);
        c2Pos = c0Pos.add(v);
        c3Pos = c0Pos.add(v2);
        c4Pos = c0Pos.add(v3);
        mt = mt.mul(Matrix3x3f.translate(0,-2 * guiDao));
        Vector2D v4 = mt.mul(radiusVec);
        c5Pos = c0Pos.add(v4);


        if (moving) {
            speedCopy = speed.mul(delta);
            c0Pos = c0Pos.add(speedCopy);

            c1OldXSpeed = speed1.getX();
            double newXSpeed = c1OldXSpeed + a * delta;
            double dx = (newXSpeed + c1OldXSpeed) *delta / 2.0;
            speed1 = new Vector2D(newXSpeed,speed1.getY());
            c1Pos = c1Pos.add(new Vector2D(dx,0));

            rot += theta * delta;
        }
        jumping = jumping && onGround;
        if (jumping)
        {
            playerSpeed = new Vector2D(
                    playerSpeed.getX(),4
            );
            onGround = false;
        }

        //y轴斜抛运动
        if (!onGround)
        {
            oldYSpeed = playerSpeed.getY();
            double vy = oldYSpeed - 9.8 * delta;
            double dy = (vy + oldYSpeed) * delta / 2.0;
            playerSpeed = new Vector2D(playerSpeed.getX(),vy);
            playerPos = playerPos.add(new Vector2D(0,dy));
            if (playerPos.getY() <=0)
            {
                playerPos = new Vector2D(playerPos.getX(),0);
                onGround  = true;
            }
        }

        //x轴匀速前进
        if (playerMovingLeft)
        {
            double vx = playerSpeed.getX();
            double dx = vx * delta;
            playerPos = playerPos.add(new Vector2D(dx,0));
        }

        if (playerMovingRight){
            double vx = playerSpeed.getX();
            double dx = vx * -delta;
            playerPos = playerPos.add(new Vector2D(dx,0));
        }
    }

    @Override
    protected void reset() {
        super.reset();
        initialPos();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        //TODO
        g2.setPaint(Color.WHITE);
        if (layout == null) {
            layout = new TextLayout(as.getIterator(), g2.getFontRenderContext());
        }
        g2.drawString("按下 空格键 移动",30,130);
        g2.drawString("按下 p 键暂停",30,150);
        g2.drawString("按下 r 键居中小球",30,170);
        g2.drawString("运动：[%s]".formatted(moving),30,190);
        g2.setColor(Color.CYAN);
        g2.drawString("c0速度 ：%.2f".formatted(speed.getX()),30,230);
        g2.drawString("c0距离（原点） ：%.2f".formatted(c0.getX()),30,250);
        g2.setColor(Color.PINK);
        g2.drawString("c1速度 ：%.2f".formatted(speed1.getX()),30,270);
        g2.drawString("加速度 ：%.2f".formatted(a),30,290);
        g2.drawString("c1距离 ：%.2f".formatted(c1.getX()),30,310);
        drawText(g2,layout,playerPos);
        super.drawImage(g2,image05.getImage(),c2Pos);
        super.drawImage(g2,image02.getImage(),c3Pos);
        super.drawImage(g2,image03.getImage(),c4Pos);
        super.drawImage(g2,image04.getImage(),c5Pos);
        super.drawImage(g2,image03.getImage(),c1Pos);
        drawImage(g2,image.getImage(),c0);
        g2.dispose();
    }

    @Override
    protected void drawImage(Graphics2D g2, Image image, Vector2D p) {
        super.drawImage(g2, image, p);
        //绘制轨道
        g2.setColor(Color.WHITE);
        //绘制轨道
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,0.25f
        ));
        drawCircle(g2,p,guiDao);

    }

    public static void main(String[] args) {
        launchGame(new ZhiXianMotion());
    }
}
