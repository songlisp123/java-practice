package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.interpolator.SineInterpolator;
import com.snl.swing.animator.interpolator.SplineInterpolator;
import com.snl.swing.animator.keyframe.KeyFrames;
import com.snl.swing.animator.keyframe.KeyValues;
import com.snl.swing.animator.keyframe.PropertySetter;
import com.snl.swing.game.gameFrame.DiKaErPlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TestPropertyDemo extends DiKaErPlus {

    TestImage im;
    Animator animator,a2;

    public TestPropertyDemo() throws HeadlessException {
        im = new TestImage("./images/me.jpg");
        KeyFrames k = new KeyFrames(
                KeyValues.create(0.0,200.0,300.2,500.32), new SplineInterpolator(0.5,0,0.5,1),
                new SplineInterpolator(0.5,1,1,0.2),new SplineInterpolator(1,0,0.5,1)
        );
        animator = PropertySetter.createAnimator(2000, im, "xpos",k);
        a2 = PropertySetter.createAnimator(10000, im, "rot",0.0, 6.28);
        animator.setIntRepeatCount(true);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new SineInterpolator());
        animator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
        animator.start();
        a2.setRepeatCount(-1);
        a2.setInterpolator(new SplineInterpolator(0,1,1,0));
        a2.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
        a2.start();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            animator.pause();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_ENTER))
            animator.resume();
    }

    public static void main(String[] args) {
        launchGame(new TestPropertyDemo());
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(im.rot,im.xpos,im.ypos);
        g2.drawImage(im.im,im.xpos,im.ypos,150,150,null);
        g2.dispose();
    }

    public class TestImage {
        BufferedImage im;
       public int xpos,ypos = 150;

       float alpha = 1.0f;
       double rot = 0;

        public TestImage(String path) {
            try {
                InputStream in = new FileInputStream(path);
                im = ImageIO.read(in);
                BufferedImage bm = new BufferedImage(im.getWidth(),im.getHeight(),BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bm.createGraphics();
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0,0,bm.getWidth(),bm.getHeight());
                g2.setComposite(AlphaComposite.Src);
                g2.drawImage(im,null,null);
                g2.dispose();
                im = bm;
                System.gc();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void setXpos(double xpos) {
            System.out.println("xpos = " + xpos);
            this.xpos = (int) xpos;
        }

        public void setRot(double rot) {
            this.rot = rot;
            System.out.println("rot = " + rot);
        }
    }
}
