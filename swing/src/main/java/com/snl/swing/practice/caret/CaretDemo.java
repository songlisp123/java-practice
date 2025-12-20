package com.snl.swing.practice.caret;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class CaretDemo extends DefaultCaret {

    protected int thickNess;

    public CaretDemo(int thickNess) {
        this.thickNess = thickNess;
        setBlinkRate(500);
    }

    @Override
    protected synchronized void damage(Rectangle r) {
        if (r == null) return;
        x = r.x;
        y = r.y;
        width = this.thickNess;
        height = r.height;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (isVisible()) {
            try {
                Rectangle r = getComponent().modelToView(getDot());
                g.setColor(getComponent().getCaretColor());

                // 内部亮光
                g.fillRect(r.x, r.y, thickNess, r.height);

                // 外部淡色边框（发光效果）
                g.setColor(new Color(255, 255, 255, 50));
                g.drawRect(r.x - 1, r.y, thickNess + 2, r.height - 1);

            } catch (BadLocationException e) {
                System.err.println("发生错误："+e.getMessage());
            }
        }
    }
}
