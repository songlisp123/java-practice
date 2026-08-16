package com.snl.swing.game.sprite;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class Sequence {

    //无限循环
    private static final int infiniteCycle = -1;

    private List<BufferedImage> images;
    private Vector2D lastPaintLocation;
    //定时器类，用来调度动画
    private Stopwatch cellAdvanceTimer;

    private BufferedImage currentImage,lastPaintedImage;
    //循环次数，当前循环数。
    private long cellAdvanceInterval,currentCycle,cyclesPerAnimation;

    //渲染当前图像在序列中的索引
    private int currentImageIndex;

    public Sequence() {
        init();
    }

    public Sequence(BufferedImage...buf) {
        init();
        for (BufferedImage b : buf)
            addImage(b);
    }

    public void init() {
        images = new ArrayList<>();
        lastPaintLocation = new Vector2D();
        cellAdvanceTimer = new Stopwatch();
        currentCycle = cellAdvanceInterval = 0;
        cyclesPerAnimation = infiniteCycle;
    }


    public void addImage(BufferedImage image) {
        //首次赋值，引用第一个图片
        if (currentImage == null) {
            currentImage = image;
            currentImageIndex = 0;
        }
        images.add(image);
    }

    public void removeImages(BufferedImage bi) {
        if (bi == null)
            return;
        int index = images.indexOf(bi);
        if (index == -1)
            //不存在该图片
            throw new IllegalArgumentException("图片不存在！");
        if (index == currentImageIndex)
            //如果恰好删除当前展示的图片，抛出异常
            throw new IllegalArgumentException("图片正在展示，无法删除");
        images.remove(bi);
    }

    public boolean needsRepainting(Vector2D v) {
        return (v.x != lastPaintLocation.x)
                || (v.y != lastPaintLocation.y)
                || currentImage != lastPaintedImage;
    }

    public boolean timeToAdvanceCell() {
        //计算经过的时间
        return cellAdvanceTimer.elapsedTime() >= cellAdvanceInterval;
    }

    public boolean isOver() {
        //动画是否结束
        return (cyclesPerAnimation != infiniteCycle)
                && (currentCycle >= cyclesPerAnimation);
    }

    public void  advance() {
        if (isLastImage())
        {
            //如果是最后一张图片，循环加一
            ++currentCycle;
        }
        currentImage = nextSequenceImage();
        //重新计算间隔时间
        cellAdvanceTimer.reset();
        //TODO
    }

    public int getNumberImages() {
        return images.size();
    }

    public void start() {
        cellAdvanceTimer.start();
    }

    public void paint(Graphics2D g2, Vector2D leftConor, Vector2D dimension, Matrix3x3f mat) {

    }


    //?????
    public void paint(Graphics2D g2, double x, double y, double w, double h, ImageObserver observer) {
        g2.drawImage(currentImage, (int) x, (int) y, (int) w, (int) h,observer);
        //更新
        lastPaintLocation.x = x;
        lastPaintLocation.y = y;
        lastPaintedImage = currentImage;
    }

    public Vector2D getLastPaintLocation() {
        return lastPaintLocation;
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public BufferedImage getLastPaintedImage() {
        return lastPaintedImage;
    }

    public long getCellAdvanceInterval() {
        return cellAdvanceInterval;
    }

    public void setCellAdvanceInterval(long cellAdvanceInterval) {
        this.cellAdvanceInterval = cellAdvanceInterval;
    }

    public long getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(long currentCycle) {
        this.currentCycle = currentCycle;
    }

    public long getCyclesPerAnimation() {
        return cyclesPerAnimation;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public BufferedImage getFirstImage() {
        return images.getFirst();
    }


    public BufferedImage nextSequenceImage() {
        if (isLastImage()) {
            //如果是最后一个
            currentImageIndex = 0;
        }
        else {
            currentImageIndex++;
        }
        return images.get(getCurrentImageIndex());

    }

    public boolean isLastImage() {
        return getCurrentImageIndex() == getNumberImages() - 1;
    }
}
