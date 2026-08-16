package com.snl.swing.game.practice;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.sprite.Sequence;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TestSequence extends DiKaErPlus {

    Sequence sequence;
    BufferedImage[] bi;

    @Override
    protected void gameInitial()  {
        super.gameInitial();
        //读取图片
        bi = new BufferedImage[20];
        fillImages();
        sequence = new Sequence(bi);
        sequence.setCellAdvanceInterval(50L);
        sequence.start();
    }

    private void fillImages() {
        for (int i = 0;i<bi.length;i++) {
            String s = (i < 9) ? "frame_0"  : "frame_";
//            String s = (i < 10) ? "spin0"  : "spin";
            bi[i] = readImage("./images/" + s + (i + 1) + ".png");
        }
    }

    private BufferedImage readImage(String s) {
        try {
            InputStream in = new FileInputStream(s);
            in = new BufferedInputStream(in);
            BufferedImage bi = ImageIO.read(in);
            in.close();
            return bi;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (sequence.timeToAdvanceCell())
            sequence.advance();
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        g.drawImage(sequence.getCurrentImage(),20,20,20,20,this);
    }

    public static void main(String[] args) {
        launchGame(new TestSequence());
    }
}
