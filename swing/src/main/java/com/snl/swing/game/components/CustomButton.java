package com.snl.swing.game.components;

import com.snl.swing.game.input.MouseInputEvent;
import com.snl.swing.game.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CustomButton extends CollideObj {

    protected String string;
    protected String actionstring;
    boolean hanging;
    boolean clicked;

    //动画效果
    private static final float originalSize = 25F;
    private static final float hangingSize = 35F;
    private float size;

    //颜色
    private final Color Hanging_color = Color.GREEN;
    private final Color Default_color = Color.white;
    private Color color;

    private final List<CollideEventListener> listeners = new ArrayList<>();

    public CustomButton(double leftX, double leftY, double totalW, double totalH) {
        super(leftX, leftY, totalW, totalH);
    }

    public CustomButton(double leftX, double leftY, double totalW, double totalH, String string) {
        super(leftX, leftY, totalW, totalH);
        this.string = string;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        //绘制背景
        if (hanging) {
            size = hangingSize;
            color = Hanging_color;
        }else {
            size = originalSize;
            color = Default_color;
        }
//        g2.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),125));
//        //绘制背景板
//        Shape r = new Rectangle2D.Double(leftX,leftY,totalW,totalH);
//        g2.fill(r);
        g2.setColor(color);
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout(string, Utils.liShu.deriveFont(size), frc);
        Utils.drawText(g2,leftX,leftY,totalW,tl);
        g2.dispose();
    }

    @Override
    public void processInput(MouseInputEvent mouseInputEvent) {
        Point2D cp = mouseInputEvent.getCurrentPoint();
        hanging = pointIn(cp);
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
    }

    public void update(double delta) {
        clicked = hanging && clicked;
        if (clicked) {
            //TODO
            fireEvent();
        }
        clicked = false;
    }

    private void fireEvent() {
        ClickedEvent event = new ClickedEvent(this,actionstring);
        for (CollideEventListener l : listeners)
            l.clicked(event);
    }

    private boolean pointIn(Point2D p) {
        return p.getX() >= leftX && p.getX() <= leftX + totalW
                && p.getY() >= leftY && p.getY() <= leftY + totalH;
    }


    public void setClickedString(String string)
    {
        this.actionstring = string;
    }

    public void  addListeners(CollideEventListener l) {
        listeners.add(l);
    }

    public void  removeListeners(CollideEventListener l) {
        listeners.remove(l);
    }
}
