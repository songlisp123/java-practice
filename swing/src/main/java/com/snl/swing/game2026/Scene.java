package com.snl.swing.game2026;

import java.awt.*;
import java.util.List;

//场景类
public class Scene  {

    private List<Part> parts;
    //与下一个场景的间隔
    private int gap;
    private String name;
    //当前索引
//    private int index;
    private double length;
    private double origin,current;



    public Scene(String name, List<Part> parts,int gap) {
        this.name = name;
        this.parts = parts;
        this.gap = gap;
        for (Part part : parts) {
            double end = part.getEnd();
            if (end > length)
                length = end;
        }
        origin = current =  0.0;
    }

    public Scene(String name, List<Part> parts, double origin) {
        this.name = name;
        this.parts = parts;
        this.origin = origin;
        this.current = this.origin;
        double min = Double.POSITIVE_INFINITY;
        for (Part part : parts) {
            double end = part.getEnd();
            if (end > length)
                length = end;
            if (min > end)
                min = end;
        }
        if (origin > min)
            throw new IllegalArgumentException("参数异常");
       else if (origin > length)
            throw new IllegalArgumentException("非法参数异常");

    }

    public boolean isEnd() {
        return current >= length;
    }

    public void update(double delta) {
        current += delta;
    }

    public double getLength() {
        return length;
    }

    public void render(Graphics2D g2)  {
        synchronized (this) {
            for (Part part : parts) {
                if (current >= part.getStart() && current <= part.getEnd()) {
                    part.startAnimator();
                    part.render(g2);
                }
            }
        }
    }

    public void flush() {
        if (parts == null)
            return;
        for(Part part : parts)
            part.flush();
        parts = null;
    }

    public double getOrigin() {
        return origin;
    }

    public double getCurrent() {
        return current;
    }
}
