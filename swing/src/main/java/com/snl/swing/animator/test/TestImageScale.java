package com.snl.swing.animator.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TestImageScale extends DiKaErPlus {

    BufferedImage bf;
    Vector2D v0,v1;
    double scale;

    public TestImageScale() throws HeadlessException {
        String filename = "./images/me.jpg";
        bf = readImage(filename);
        v0 = new Vector2D(-0.5,0.5);
        v1 = new Vector2D(0.5,-0.5);
        scale = 0.15;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
        {
            v0.x = scale(v0.x,scale);
            v0.y = scale(v0.y,scale);
            v1.x = scale(v1.x,scale);
            v1.y = scale(v1.y,scale);
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
        {
            v0.x = scale(v0.x,-scale);
            v0.y = scale(v0.y,-scale);
            v1.x = scale(v1.x,-scale);
            v1.y = scale(v1.y,-scale);
        }
    }

    private double scale(double v,double scale) {
        return v + v * scale;
    }

    public static void main(String[] args) {
        launchGame(new TestImageScale());
    }

    private BufferedImage readImage(String filename) {
        try {
            InputStream in = new FileInputStream(filename);
            return ImageIO.read(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        drawImage(g2,getViewportTransform(),bf,v0,v1);
        g2.dispose();
    }


    private void drawImage(Graphics2D g2,Matrix3x3f view,BufferedImage image,Vector2D leftUp,Vector2D rightBottom) {
        Vector2D leftCopy = view.mul(leftUp);
        Vector2D right = view.mul(rightBottom);
        int w = (int) Math.abs(right.x - leftCopy.x);
        int h = (int) Math.abs(right.y - leftCopy.y);
        g2.drawImage(image, (int) leftCopy.x, (int) leftCopy.y,w,h,null);
    }
}
