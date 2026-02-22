package com.snl.swing.game.components;

import com.snl.swing.game.input.MouseInputEvent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Slide {
    double w,h;
    double r0;
    Point2D leftC0Pos;
    Point2D slidePos;
    double total = 100;
    Shape outShape;
    double oldValue;

    boolean clicked;
    boolean dragging;

    private final List<SlideDataChangeListener> listeners =
            new ArrayList<>();

    public Slide(double w, double h, Point2D leftC0Pos) {
        this.w = w;
        this.h = h;
        this.leftC0Pos = leftC0Pos;
        slidePos = leftC0Pos;
        r0 =2 * h;
    }

    public Slide(double x,double y,double w,double h) {
        leftC0Pos = new Point2D.Double(x + h / 2.0,y + h / 2.0);
        this.w = w;
        this.h = h;
        r0 = 2 * h;
        slidePos = leftC0Pos;
        createOutShape();
    }

    public Slide(double x,double y,double w,double h,double total) {
        leftC0Pos = new Point2D.Double(x + h / 2.0,y + h / 2.0);
        this.w = w;
        this.h = h;
        r0 = 2 * h;
        this.total = total;
        slidePos = leftC0Pos;
        createOutShape();
    }

    private void createOutShape() {
        double x,y;
        x = leftC0Pos.getX() - h / 2.0;
        y = leftC0Pos.getY() - h / 2.0;
        outShape = new RoundRectangle2D.Double(x,y,w,h,h,h);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.draw(outShape);
        //滑动块
        //左上角点
        g2.setColor(Color.CYAN);
        double x,y;
        x = slidePos.getX() - r0 / 2.0;
        y = slidePos.getY() - r0 / 2.0;
        Shape c = new Ellipse2D.Double(x,y,r0,r0);
        g2.fill(c);
        //如果滑动
        double sw = slidePos.getX() - leftC0Pos.getX();
        var s  = new RoundRectangle2D.Double(leftC0Pos.getX() - h / 2.0, leftC0Pos.getY()  - h / 2.0,sw,h,h,h);
        g2.fill(s);
    }


    public void move(Point2D pos) {
        double maxX = leftC0Pos.getX() + w;
        double newPos = pos.getX();
        if (newPos < leftC0Pos.getX())
            newPos = leftC0Pos.getX();
        else if (newPos >maxX)
            newPos = maxX;
        oldValue = getValue();
        slidePos = new Point2D.Double(newPos,slidePos.getY());
        fireEvent();
    }


    public double getValue() {
        double f = slidePos.getX() - leftC0Pos.getX();
        double r = f / w * total;
        r = Math.max(0,Math.min(r,total));
        return r;
    }


    public void processInput(MouseInputEvent mouseInputEvent) {
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
    }


    public void update(double delta,Point2D mousePoint) {
        clicked = clicked && contains(mousePoint);
        dragging = dragging && c(mousePoint);
        if (clicked || dragging)
            move(mousePoint);
    }

    private boolean contains(Point2D mousePos) {
        return outShape.contains(mousePos);
    }

    private boolean c(Point2D mouse) {
        double d = slidePos.distanceSq(mouse);
        return d < Math.pow(r0,2);
    }

    public boolean isClicked(boolean clicked,Point2D mouse) {
        return clicked && contains(mouse);
    }

    public boolean isDragging(boolean dragging,Point2D mouse) {
        return dragging && c(mouse);
    }

    public void addListener(SlideDataChangeListener l) {
        listeners.add(l);
    }

    public void removeListener(SlideDataChangeListener l) {
        listeners.remove(l);
    }

    private void fireEvent() {
        for (SlideDataChangeListener l :listeners)
            l.change(this,oldValue,getValue());
    }

    public void setValue(int start) {
        double maxX = leftC0Pos.getX() + w;
        double minX = leftC0Pos.getX();
        double v = start;
        v *= (maxX - minX) / total;
        v += minX;
        slidePos = new Point2D.Double(v,slidePos.getY());
    }
}
