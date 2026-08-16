package com.snl.swing.game2026;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestScene extends DiKaErPlus {

    Scene scene;
    BufferedImage bi;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        String s = "./images/frame_020.png";
        InputStream in = null;
        readImage(s,in);
        Part p = new PartImplement(0,1.5,bi);
        List<Part> parts = new ArrayList<>();
        parts.add(p);
        s = "./images/frame_021.png";
        readImage(s,in);
        p = new PartImplement(1,2,bi);
        parts.add(p);
        p = new ClosePart(c,2.5,5);
        parts.add(p);
        scene = new Scene("测试",parts,10);
    }

    private void readImage(String path,InputStream in) {
        try {
            in = new FileInputStream(path);
            in = new BufferedInputStream(in);
            bi = ImageIO.read(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launchGame(new TestScene());
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        scene.update(delta);
        if (scene.isEnd())
            scene.flush();
    }


    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (!scene.isEnd())
            scene.render(g2);
        g2.dispose();
    }
}
