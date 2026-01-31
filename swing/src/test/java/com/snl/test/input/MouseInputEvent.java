package com.snl.test.input;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class MouseInputEvent implements MouseListener, MouseMotionListener  {

    final int MOUSE_BUTTON =  3;
    private boolean[] mouses;
    private byte[] polled;
    private Point2D mousePoint;
    private Point2D currentPoint;

    private Point2D absPoint;

    RectangularShape mouseShape;
    final int MOUSE_WIDTH = 8;
    final int MOUSE_HEIGHT = 8;

    public MouseInputEvent() {
        mousePoint = new Point2D.Double(0,0);
        currentPoint = mousePoint;
        absPoint = mousePoint;
        mouses = new boolean[MOUSE_BUTTON];
        polled = new byte[MOUSE_BUTTON];
        mouseShape = new Rectangle2D.Double(currentPoint.getX() - MOUSE_WIDTH / 2.0,
                currentPoint.getY() - MOUSE_HEIGHT / 2.0,MOUSE_WIDTH,MOUSE_HEIGHT);
    }

    public synchronized void poll() {
        //获取
        for (int i=0;i<mouses.length;i++) {
            if (mouses[i]) {
                polled[i] ++;
            }else {
                polled[i] = 0;
            }
        }
    }

    public boolean mouseButtonDown(int button) {
        return polled[button-1] > 0;
    }

    public boolean mouseButtonDownOnce(int button) {
        return polled[button-1] == 1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //不实现
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouses.length)
        {
            mouses[button] = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouses.length)
        {
            mouses[button] = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        move(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        move(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        move(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        move(e);
    }

    private void move(MouseEvent e) {
        currentPoint = e.getPoint();
        absPoint = e.getLocationOnScreen();
        mouseShape.setFrame(currentPoint.getX() - MOUSE_WIDTH / 2.0,
                currentPoint.getY() - MOUSE_HEIGHT / 2.0,MOUSE_WIDTH,MOUSE_HEIGHT);
    }

    public Point2D getCurrentPoint() {
        return currentPoint;
    }

    public String checkButton() {
        String s = null;
        for (int i=0;i<mouses.length;i++) {
            if (s != null)
                break;
           if (mouses[i])
           {
               switch (i) {
                   case 0 :
                       s = "鼠标左键";
                       break;
                   case 1:
                       s = "鼠标中键" ;
                       break;
                   case 2:
                       s = "鼠标右键";
                       break;
                   default:
                       break;
               }
           }
        }
        return s;
    }

    public Point2D getAbsPoint() {
        return absPoint;
    }

    public RectangularShape getMouseShape() {
        return mouseShape;
    }
}
