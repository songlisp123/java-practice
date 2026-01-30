package com.snl.swing.TwoDimensionDemo.layerUi;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SpotLightLayerUiDemo extends LayerUI<JComponent> {
     protected boolean active;
     protected int xPos;
     protected int yPos;

    public SpotLightLayerUiDemo() {

    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        JLayer layer = (JLayer) c;
        layer.setLayerEventMask(0);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JLayer layer = (JLayer) c;
        layer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK |
                AWTEvent.MOUSE_MOTION_EVENT_MASK);

    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g,c);
        Graphics2D g2 = (Graphics2D) g.create();
        if (active) {
            //逻辑C
            Point2D center = new Point2D.Float(xPos,yPos);
            float radius = 75;
            float[] dist = {0.0f,0.5f,1.0f};
            Color[] colors = {
                    new Color(255,255,255),new Color(125,125,125),Color.BLACK
            };
            RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(paint);
//            g2.setComposite(AlphaComposite.getInstance(
//                    AlphaComposite.SRC_OVER,0.65F
//            ));
            g2.fillRect(0,0,c.getWidth(),c.getHeight());
        }
        g2.dispose();
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        if (e.getID() == MouseEvent.MOUSE_ENTERED) active = true;
        if (e.getID() == MouseEvent.MOUSE_EXITED) active = false;
        l.repaint();
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
        xPos = e.getX();
        yPos = e.getY();
        l.repaint();
    }
}
