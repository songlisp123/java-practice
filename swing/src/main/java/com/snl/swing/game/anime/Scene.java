package com.snl.swing.game.anime;

import java.awt.*;
import java.util.List;

/**
 * 场景类，维护了一系列{@code part}和场景切换之间的时间
 * 有关更多信息，参阅{@link Part}
 */
public class Scene {

    private Object name;
    private Object pauseAmt;
    private Boolean participate = Boolean.TRUE;
    private List<Part> parts;
    private int index;  //索引
    private int length; //片段的长度

    public Scene(List<Part> parts, Object name, Object pauseAmt) {
        this.parts = parts;
        this.name = name;
        this.pauseAmt = pauseAmt;
        for (Part part : parts)
        {
            int l = part.getEnd();
            if (l > length)
                length = l;
        }
    }

    public void reset(int w,int h) {
        index = 0;
        for (Part part : parts)
        {
            part.reset(w,h);
        }
    }

    public void render(int w, int h, Graphics2D g2) {
        for (Part part : parts)
        {
            if (index >= part.getStart() && index <= part.getEnd()) {
                part.render(w, h, g2);
            }
        }
    }

    public void step(int w,int h) {
        for (Part part : parts)
        {
            if (index >= part.getStart() && index <= part.getEnd())
                part.step(w,h);
        }
    }

    public boolean pause() {
        try {
            Thread.sleep(Long.parseLong((String) pauseAmt));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //这一步是什么意思？？？？
        System.gc();
        return true;
    }

    public void increment() {
        index++;
    }

    public int getLength() {
        return length;
    }

    public int getIndex() {
        return index;
    }
}
