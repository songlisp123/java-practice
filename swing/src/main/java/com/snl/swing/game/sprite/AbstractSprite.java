package com.snl.swing.game.sprite;

import com.snl.swing.game.input.MouseInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class AbstractSprite {
    protected BufferedImage image,cacheImage;
    protected final int type;
    protected final byte yuanShu;
    private final String name;
    private final String beiDong;
    //位置
    protected double lx,ly;
    protected double w,h;

    protected static final int PICTURE_W = 250;

    protected boolean clicked,dragging;

    public AbstractSprite(String name, String beiDong,int type, byte yuanShu,String path) {
        this.name = name;
        this.type = type;
        this.yuanShu = yuanShu;
        this.beiDong = beiDong;
    }

    public AbstractSprite(String name, int type, byte yuanShu, String beiDong,
                          double lx, double ly) {
        this.name = name;
        this.type = type;
        this.yuanShu = yuanShu;
        this.beiDong = beiDong;
        this.lx = lx;
        this.ly = ly;
    }


    public AbstractSprite(BufferedImage image, int type, byte yuanShu, String name, String beiDong,
                          double lx, double ly) {
        this.image = image;
        this.type = type;
        this.yuanShu = yuanShu;
        this.name = name;
        this.beiDong = beiDong;
        this.lx = lx;
        this.ly = ly;
    }

    public abstract void draw(Graphics g);
    public abstract void processInput(MouseInputEvent mouseInputEvent);
    public abstract void update(double delta);

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    protected boolean pointIn(Point2D point) {
        return point.getX() >= lx && point.getX() <= lx + w &&
                point.getY() >= ly && point.getY() <= ly + h;
    }

    public void setCacheImage(BufferedImage cacheImage) {
        this.cacheImage = cacheImage;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getCacheImage() {
        return cacheImage;
    }

    public String getName() {
        return name;
    }

    public byte getYuanShu() {
        return yuanShu;
    }

    public String getBeiDong() {
        return beiDong;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "name='" + name + '\'' +
                ", yuanShu=" + yuanShu +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, type, yuanShu, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractSprite))
            return false;
        AbstractSprite other = (AbstractSprite) obj;
        if (other == this) return true;
        return this.image == other.image &&
                this.type == other.type &&
                this.yuanShu == other.yuanShu;
    }

    protected BufferedImage createImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() == 0 || icon.getIconHeight() == 0) {
            throw new RuntimeException("文件暂未找到");
        }
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        int newH = PICTURE_W * h / w;
        BufferedImage bi = new BufferedImage(PICTURE_W,newH,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0,0,bi.getWidth(),bi.getHeight());
        g2.setComposite(AlphaComposite.Src);
        g2.drawImage(icon.getImage(),0,0,bi.getWidth(),bi.getHeight(),null);
        g2.dispose();
        //设置宽高
        setW(PICTURE_W);
        setH(newH);
        return bi;
    }
}
