package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;
import com.snl.swing.animator.interpolator.DiscreteInterpolator;
import com.snl.swing.animator.interpolator.Inverter;
import com.snl.swing.animator.interpolator.SineInterpolator;
import com.snl.swing.animator.interpolator.SplineInterpolator;
import com.snl.swing.game.gameFrame.DiKaErPlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ShowImage extends DiKaErPlus implements TimingTarget {



    private int min_x,max_x;
    private final int y_pos = 150;
    int x = min_x;
    BufferedImage bf;
    Animator animator;

    public ShowImage() throws HeadlessException {
        min_x = 100;
        max_x = 400;
        String filename = "./images/me.jpg";
        bf = readImage(filename);
        WIDTH = HEIGHT = 600;
        wordWidth = 9;
        wordHeight = 12;

        animator = new Animator(2000,Animator.INFINITE, Animator.RepeatBehavior.LOOP,this);
//        animator = new Animator(2000,-1, Animator.RepeatBehavior.REVERSE,this);
//        animator.setIntRepeatCount(false);
//        animator = new Animator(2000,this);
//        animator = new Animator(2000,this);
//        animator.setAcceleration(0.2);
//        animator.setDeceleration(0.5);
//        animator.setInterpolator(DiscreteInterpolator.getInstance());
//        animator.setInterpolator(new SplineInterpolator(0,1,1,0));
//        animator.setInterpolator(Inverter.getInstance());
        animator.setInterpolator(new SineInterpolator());
        animator.start();
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
        g2.drawImage(bf,x,y_pos,30,30,null);
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new ShowImage());
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        System.out.println("fraction = " + fraction);
        x = (int) (min_x + fraction * (max_x - min_x));
    }
}
