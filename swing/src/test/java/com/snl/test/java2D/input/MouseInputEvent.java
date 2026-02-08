package com.snl.test.java2D.input;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class MouseInputEvent implements MouseListener, MouseMotionListener , MouseWheelListener {

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
        int clickCount = e.getClickCount();
        int button = e.getButton() - 1;
        if (clickCount > 1)
            polled[button] = 2;
        else
            polled[button] = 0;

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

    /**
     * 返回屏幕坐标
     * @return 鼠标当前的屏幕坐标
     */
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

    public Point2D getRelativePoint(Component component,double x,double y) {
        int width = component.getWidth() / 2;
        int height = component.getHeight() / 2;
        double vx = x - width;
        double vy = height - y;
        return new Point2D.Double(vx,vy);
    }

    public Point2D getRelativePoint(Component c,Point2D p) {
        return this.getRelativePoint(c,p.getX(),p.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //TODO
    }

    public boolean mouseClickedTwo(int keyCode) {
        return polled[keyCode-1] == 2;
    }

}
