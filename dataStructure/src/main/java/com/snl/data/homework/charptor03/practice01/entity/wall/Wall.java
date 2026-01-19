package com.snl.data.homework.charptor03.practice01.entity.wall;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 墙壁主要作为阻挡玩家的脚步,只负责重绘
 */
public class Wall extends Sprite  {

    private Color color;
    private Shape shape;
    private TexturePaint paint;

    /**
     * 无参构造器
     */
    public Wall() {
    }

    public Wall(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        this(xPos, yPos, WEIGHT, HEIGHT,Color.ORANGE);
    }

    public Wall(double xPos, double yPos, int WEIGHT, int HEIGHT, Color color) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.color = color;
        shape = new Rectangle2D.Double(xPos,yPos,WEIGHT,HEIGHT);
        initPaint();
    }

    @Override
    public void update(double delta, InputState state) {
        //无实现
    }

    @Override
    public void paint(Graphics g, InputState state) {
        var g2 = (Graphics2D) g.create();
        g2.setPaint(paint);
        g2.fill(shape);
        g2.dispose();
    }

    public void initPaint() {
        BufferedImage bi = getTexture();
        Rectangle2D r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        setPaint(new TexturePaint(bi,r));
    }

    public BufferedImage getTexture() {
        int szie = 20;
        BufferedImage bi = new BufferedImage(szie,szie,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(GameConstants.tuRang);
        g2.fillRect(0,0,szie,szie);
        g2.dispose();
        return bi;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }

    public void setPaint(TexturePaint paint) {
        this.paint = paint;
    }

    public TexturePaint getPaint() {
        return paint;
    }
}
