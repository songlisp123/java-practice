package com.snl.swing.game.sprite;

import com.snl.swing.game.input.MouseInputEvent;

import java.awt.*;

public class Sprite extends AbstractSprite {

    public Sprite(String name,  String beiDong,int type, byte yuanShu, String path) {
        super(name,beiDong, type, yuanShu, path);
        setImage(createImage(path));
    }

    @Override
    public void processInput(MouseInputEvent mouseInputEvent) {}

    @Override
    public void update(double delta) {}

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, (int) lx, (int) ly,null);
    }

}
