package com.snl.swing.game2026;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.keyframe.KeyFrames;
import com.snl.swing.animator.keyframe.KeyValues;
import com.snl.swing.animator.keyframe.PropertySetter;
import com.snl.swing.game.input.MouseInputEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Button  {
    private String bs;

    private final ArrayList<ActionListener> listeners = new ArrayList<>();

    private boolean drawBorder;
    //背景透明？？
    private boolean opcatity;
    private Color backGround,foreGround;

    private int x,y,w,h;

    private Animator animator;

    //悬挂
    private boolean hanging,clicking;

    private Animator changingAnimator;


    public Button(String bs) {
        this.bs = bs;
        this.x = this.y = 0;
        initial();
    }

    public Button(String bs, int x, int y) {
        this.bs = bs;
        this.x = x;
        this.y = y;
        initial();
    }

    private void initial() {
        drawBorder = true;
        opcatity = false;
        this.backGround = Color.BLACK;
        this.foreGround = Color.WHITE;
    }


    public void  addActionListener(ActionListener l) {
        synchronized (this) {
            if (!listeners.contains(l))
                listeners.add(l);
        }
    }

    public void  removeActionListener(ActionListener l) {
        synchronized (this) {
            listeners.remove(l);
        }
    }

    public void draw(Graphics2D g2) {
        if (drawBorder)
            drawBorder(g2);
        drawText(g2);
    }

    private void drawText(Graphics2D g2) {
        g2.setColor(foreGround);
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.drawString(bs,x + 10,y + 20 + fontMetrics.getDescent());
    }

    private void drawBorder(Graphics2D g2) {
        g2.setColor(foreGround);
        FontMetrics fontMetrics = g2.getFontMetrics();
        w = fontMetrics.stringWidth(bs);
        h = fontMetrics.getHeight();
        g2.drawRoundRect(x,y,w + 20 ,h + 20 ,5,5);
    }


    public void processInput(MouseInputEvent mouseInputEvent) {
        if (mouseInputEvent == null)
            return;
        Point2D currentPoint = mouseInputEvent.getCurrentPoint();
        hanging = contains(currentPoint);
        clicking = hanging && mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
        if (hanging) {
            if (animator == null || !animator.isRunning())
                foreGround = Color.CYAN;
        }
        else
            if (animator == null || !animator.isRunning())
                foreGround = Color.WHITE;
        if (clicking) {
            //左键，确认
            if (animator == null) {
                KeyValues<Color> kc = KeyValues.create(Color.CYAN.darker().darker(), Color.CYAN);
                KeyFrames kf = new KeyFrames(kc);
                animator = PropertySetter.createAnimator(1000, this, "foreGround", kf);
                animator.setRepeatCount(1.0f);
                animator.setEndBehavior(Animator.EndBehavior.HOLD);
            }
            else
            {
                if (animator.isRunning())
                    animator.stop();
            }
            animator.start();
            fireEvent();
        }
    }

    private void fireEvent() {
        synchronized (this) {
            for (ActionListener l : listeners)
                l.actionPerformed(null);
        }
    }


    public void setForeGround(Color foreGround) {
        this.foreGround = foreGround;
    }

    public boolean contains(Point2D p) {
        if (w == 0 || h == 0)
            //尚未加载成功
            return false;
        return p.getX() > x && p.getX() < x + w + 20 &&
                p.getY() > y && p.getY() < y + h + 20;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

}
