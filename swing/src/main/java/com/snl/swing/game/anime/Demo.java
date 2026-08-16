package com.snl.swing.game.anime;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.keyBoard.SimpleCleanKeyBoard;
import com.snl.swing.game.utils.ImageCreator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo extends DiKaErPlus {

    Scene scene;
    List<Part> parts;
    SimpleCleanKeyBoard keyBoard;
    TextMoveEffect tm;
    BufferedImage bi;

    public Demo() throws HeadlessException {
        drawAxis = false;
        parts = new ArrayList<>();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        c.setBackground(Color.WHITE);
        TexturePaintEffect t1 =  new TexturePaintEffect(
                TexturePaintEffect.OI, Color.BLACK, Color.YELLOW, 60, 0,200);
        DitherDissolveEffect de = new DitherDissolveEffect(30,300,20,c);
        GunDongEffect effect = new GunDongEffect(10,250,c);
        CloseEffect cff = new CloseEffect(CloseEffect.CUSTOM,0,200,c);
        GradientEffect gf = new GradientEffect(GradientEffect.WID | GradientEffect.INC,
                Color.WHITE,Color.BLACK,0,120);
        TextEffect tff = new TextEffect("傻逼一个",new Font("隶书",Font.BOLD,20),
                TextEffect.SCI  | TextEffect.SCYI,
                Color.CYAN,30,300);
        BackGroundEffect bck = new BackGroundEffect(20,120,c,Color.WHITE,Color.CYAN,Color.blue);
        tm = new TextMoveEffect("你好，我是一个傻逼",c.getWidth(),c.getHeight(),50,170);
        parts.add(gf);
//        parts.add(effect);
//        parts.add(t1);
//        parts.add(tff);
        parts.add(bck);
        parts.add(cff);

        scene = new Scene(parts,"演示","56");
        keyBoard = new SimpleCleanKeyBoard(50,50,300,300);
        //
        try {
            bi = ImageIO.read(new File("./Ours_en_peluche_-_15.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        scene.step(c.getWidth(),c.getHeight());
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        keyBoard.draw(g2,Color.lightGray);
        g2.setColor(Color.green);
//        g2.drawImage(bi,null,null);
        scene.render(c.getWidth(),c.getHeight(),g2);
//        tm.render(c.getWidth(),c.getHeight(),g2);
        g2.dispose();
    }

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (!isBeyond())
            scene.increment();
        else
            scene.reset(c.getWidth(),c.getHeight());
    }

    private boolean isBeyond() {
        return scene.getIndex() > scene.getLength();
    }

    public static void main(String[] args) {
        launchGame(new Demo());
    }
}
