package com.snl.swing.game.components;

import com.snl.swing.game.components.enm.Direction;
import com.snl.swing.game.input.MouseInputEvent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Row extends CollideObj {
    
    private GeneralPath path;
    
    private static final Direction EAST = Direction.EAST;
    private static final Direction WEST = Direction.WEST;
    private static final Direction NORTH = Direction.NORTH;
    private static final Direction SOUTH = Direction.SOUTH;
    
    private final Direction direction;

    boolean clicked;
    private float alpha = 0.0F;
    private boolean ani;

    private List<CollideEventListener> listeners = new ArrayList<>();

    public Row(double leftX, double leftY, double totalW, double totalH, Direction direction) {
        super(leftX, leftY, totalW, totalH);
        if (direction == null ||
                Arrays.stream(Direction.values()).noneMatch(directionRow -> directionRow == direction))
            throw new IllegalArgumentException("非法参数异常");
        this.direction = direction;
        createRow();
    }

    private void createRow() {
        path = new GeneralPath(Path2D.WIND_NON_ZERO);
        switch (direction) {
            case EAST -> {
                path.moveTo(leftX,leftY);
                path.lineTo(leftX,leftY + totalH);
                path.lineTo(leftX + totalW,leftY + totalH / 2.0);
            }
            case WEST -> {
                path.moveTo(leftX,leftY + totalH / 2.0);
                path.lineTo(leftX + totalW,leftY);
                path.lineTo(leftX + totalW,leftY + totalH);
            }

            case NORTH -> {
                path.moveTo(leftX,leftY + totalH);
                path.lineTo(leftX + totalW,leftY + totalH);
                path.lineTo(leftX + totalW / 2.0,leftY);
            }

            case SOUTH -> {
                path.moveTo(leftX,leftY);
                path.lineTo(leftX ,leftY + totalH);
                path.lineTo(leftX + totalW / 2.0,leftY + totalH);
            }
        }
        path.closePath();
    }


    @Override
    public void processInput(MouseInputEvent mouseInputEvent) {
        Point2D cp = mouseInputEvent.getCurrentPoint();
        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
        clicked = clicked && path.contains(cp);
    }

    @Override
    public void update(double delta) {
        if (clicked)
        {
            alpha = 1.0f;
            ani = true;
            fireEvent();
        }
        clicked = false;
        alpha  = (alpha <= 0.0F) ? 0.0F : (float) (alpha - delta);
        ani = (ani && alpha > 0);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.draw(path);
        if (ani) {
            g2.setColor(new Color(1.0F, 0.0F, 1.0F, alpha));
            g2.fill(path);
        }
    }

    public GeneralPath getPath() {
        return path;
    }

    public Direction getDirection() {
        return direction;
    }

    private void fireEvent() {
        ClickedEvent event = new ClickedEvent(this,null);
        for (CollideEventListener l : listeners)
            l.clicked(event);
    }

    public void  addListeners(CollideEventListener l) {
        listeners.add(l);
    }

    public void  removeListeners(CollideEventListener l) {
        listeners.remove(l);
    }
}
