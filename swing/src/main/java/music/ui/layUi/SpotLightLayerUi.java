package music.ui.layUi;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SpotLightLayerUi extends LayerUI<JComponent> {

    protected int xPos;
    protected int yPos;
    protected boolean active;
    protected boolean clicked;

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        var layer = (JLayer) c;
        layer.setLayerEventMask(0);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        var layer = (JLayer) c;
        layer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK |
                        AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        var g2 = (Graphics2D) g.create();
        if (active) {
            var p = new Point2D.Double(xPos, yPos);
            float radius = 75;
            float[] dist = {0.0f,1.0f};
            Color[] colors = {new Color(255,255,255),Color.BLACK};
            RadialGradientPaint paint = new RadialGradientPaint(p, radius, dist, colors);
            g2.setPaint(paint);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,0.6f
            ));
            g2.fillRect(0,0,c.getWidth(),c.getWidth());
        }
        g2.dispose();
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        int eID = e.getID();
        if (eID == MouseEvent.MOUSE_ENTERED) active = true;
        if (eID == MouseEvent.MOUSE_EXITED) active = false;
        if (eID == MouseEvent.MOUSE_RELEASED) clicked = false;
        l.repaint();
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        Point point = e.getPoint();
        xPos = point.x;
        yPos = point.y;
        l.repaint();
    }
}
