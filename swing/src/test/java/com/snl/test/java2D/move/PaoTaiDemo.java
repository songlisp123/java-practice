package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.JiCordPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PaoTaiDemo extends JiCordPlus {

    Vector2D[] poly,polyCopy;

    double rot,theta;

    Paint paint;

    public PaoTaiDemo() throws HeadlessException {
        super();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        rot = 0;
        theta = Math.PI / 4;
        createPaint();
        poly = new Vector2D[] {
                new Vector2D(0,1.5),//l
                new Vector2D(6,1.5),//r
                new Vector2D(6,-1.5),
                new Vector2D(0,-1.5)
        };
        polyCopy = new Vector2D[poly.length];
    }

    private void createPaint() {
        BufferedImage bi = getTextureImage();
        Rectangle2D r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        paint = new TexturePaint(bi,r);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            rot += theta;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Matrix3x3f mat = Matrix3x3f.rotate(rot);
        for (int i = 0;i<polyCopy.length;i++) {
            polyCopy[i] = mat.mul(poly[i]);
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        drawPoly(g2);
        g2.dispose();
    }

    private void drawPoly(Graphics2D g2) {
        g2.setPaint(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        Matrix3x3f view = getViewportTransform();
        for (int i = 0;i<polyCopy.length;i++)
            polyCopy[i] = view.mul(polyCopy[i]);

        Vector2D p;
        Vector2D f = polyCopy[polyCopy.length -1];
        for (Vector2D v : polyCopy) {
            p = v;
            Line2D l = new Line2D.Double(f.getX(),f.getY(),p.getX(),p.getY());
            g2.draw(l);
            f = p;
        }
    }

    @Override
    protected void reset() {
        super.reset();
    }

    private BufferedImage getTextureImage() {
        int size = 20;
        BufferedImage bi = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, size / 2, size / 2);
        g2.setPaint(Color.BLACK);
        g2.fillRect(size / 2, 0, size, size / 2);
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, size / 2, size / 2, size);
        g2.setPaint(Color.WHITE);
        g2.fillRect(size / 2, size / 2, size, size);
        return bi;
    }

    public static void main(String[] args) {
        launchGame(new PaoTaiDemo());
    }
}
