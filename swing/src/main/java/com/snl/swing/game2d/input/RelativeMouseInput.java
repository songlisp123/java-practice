package com.snl.swing.game2d.input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class RelativeMouseInput implements MouseListener, MouseWheelListener, MouseMotionListener {

//    private static final int BUTTON_COUNT = 3;
//
//    private Point mousePos;
//    private Point currentPos;
//
//    private boolean[] mouse;
//    private int[] polled;
//    private int notches;
//    private int polledNotches;
//
//    private int dx,dy;
//    private Robot robot;
//    private Component component;
//    private boolean relative;
//
//    public RelativeMouseInput(Component component) {
//        this.component = component;
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//        //当前鼠标位置
//        mousePos = new Point(0,0);
//        currentPos = new Point(0,0);
//        mouse = new boolean[BUTTON_COUNT];
//        polled = new int[BUTTON_COUNT];
//    }
//
//
//    public boolean isRelative() {
//        return relative;
//    }
//
//    public void setRelative(boolean relative) {
//        this.relative = relative;
//    }
//
//    public Point getMousePos() {
//        return mousePos;
//    }
//
//    public int getNotches() {
//        return notches;
//    }
//
//    public synchronized void poll() {
//        if (isRelative()) {
//            //如果是相对位置
//            mousePos = new Point(dx,dy);
//
//        }else
//            //否则
//            mousePos = new Point(currentPos);
//        dx = dy = 0;
//        polledNotches = notches;
//        notches = 0;
//
//        for (int i = 0;i<mouse.length;i++ ){
//            if (mouse[i])
//                //如果按下
//                polled[i]++;
//            else
//                //如果松开
//                polled[i] = 0;
//        }
//    }
//
//    public boolean buttonDownOnce(int button) {
//        return polled[ button - 1] == 1;
//    }
//
//    public boolean buttonDown(int button) {
//        return polled[button - 1] > 0;
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {}
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        int button = e.getButton() - 1;
//        if (button > 0 && button < mouse.length) {
//            mouse[button] = true;
//        }
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        int button = e.getButton() - 1;
//        if (button > 0 && button < mouse.length) {
//            mouse[button] = false;
//        }
//    }
//
//    @Override
//    public synchronized void mouseEntered(MouseEvent e) {
//        mouseMoved(e);
//    }
//
//    @Override
//    public synchronized void mouseExited(MouseEvent e) {mouseMoved(e);}
//        //TODO}
//
//    @Override
//    public synchronized void mouseDragged(MouseEvent e) {
//        mouseMoved(e);
//    }
//
//    @Override
//    public synchronized void mouseMoved(MouseEvent e) {
//        if (isRelative()) {
//            //如果是相对位置
//            Point point = e.getPoint();
//            Point componentCenter = getComponentCenter();
//            dx += point.x - componentCenter.x;
//            dy += point.y - componentCenter.y;
//            centerMouse();
//        }else
//            //fouze
//            currentPos = e.getPoint();
//
//    }
//
//    @Override
//    public void mouseWheelMoved(MouseWheelEvent e) {
//        notches += e.getWheelRotation();
//    }
//
//    private Point  getComponentCenter() {
//        int w = component.getWidth();
//        int h = component.getHeight();
//        return new Point(w / 2,h / 2);
//    }
//
//    private void centerMouse() {
//        if( robot != null && component.isShowing() ) {
//            Point center = getComponentCenter();
//            SwingUtilities.convertPointToScreen( center, component );
//            robot.mouseMove( center.x, center.y );
//        }
//    }
private static final int BUTTON_COUNT = 3;

    private Point mousePos;
    private Point currentPos;
    private boolean[] mouse;
    private int[] polled;
    private int notches;
    private int polledNotches;

    private int dx, dy;
    private Robot robot;
    private Component component;
    private boolean relative;

    public RelativeMouseInput( Component component ) {

        this.component = component;
        try {
            robot = new Robot();
        } catch( Exception e ) {
            // Handle exception [game specific]
            e.printStackTrace();
        }

        mousePos = new Point( 0, 0 );
        currentPos = new Point( 0, 0 );
        mouse = new boolean[ BUTTON_COUNT ];
        polled = new int[ BUTTON_COUNT ];
    }

    public synchronized void poll() {

        if( isRelative() ) {
            mousePos = new Point( dx, dy );
        } else {
            mousePos = new Point( currentPos );
        }
        dx = dy = 0;

        polledNotches = notches;
        notches = 0;

        for( int i = 0; i < mouse.length; ++i ) {
            if( mouse[i] ) {
                polled[i]++;
            } else {
                polled[i] = 0;
            }
        }
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative( boolean relative ) {
        this.relative = relative;
        if( relative ) {
            centerMouse();
        }
    }

    public Point getPosition() {
        return mousePos;
    }

    public int getNotches() {
        return polledNotches;
    }

    public boolean buttonDown( int button ) {
        return polled[ button - 1 ] > 0;
    }

    public boolean buttonDownOnce( int button ) {
        return polled[ button - 1 ] == 1;
    }

    public synchronized void mousePressed( MouseEvent e ) {
        int button = e.getButton() - 1;
        if( button >= 0 && button < mouse.length ) {
            mouse[ button ] = true;
        }
    }

    public synchronized void mouseReleased( MouseEvent e ) {
        int button = e.getButton() - 1;
        if( button >= 0 && button < mouse.length ) {
            mouse[ button ] = false;
        }
    }

    public void mouseClicked( MouseEvent e ) {
        // Not needed
    }

    public synchronized void mouseEntered( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseExited( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseDragged( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseMoved( MouseEvent e ) {
        if( isRelative() ) {
            Point p = e.getPoint();
            Point center = getComponentCenter();
            dx += p.x - center.x;
            dy += p.y - center.y;
            centerMouse();
        } else {
            currentPos = e.getPoint();
        }
    }

    public synchronized void mouseWheelMoved( MouseWheelEvent e ) {
        notches += e.getWheelRotation();
    }

    private Point getComponentCenter() {
        int w = component.getWidth();
        int h = component.getHeight();
        return new Point( w / 2, h / 2 );
    }

    private void centerMouse() {
        if( robot != null && component.isShowing() ) {
            Point center = getComponentCenter();
            SwingUtilities.convertPointToScreen( center, component );
            robot.mouseMove( center.x, center.y );
        }
    }
}
