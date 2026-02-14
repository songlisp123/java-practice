package com.snl.test.image;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

public class ImageSpeedTest extends DiKaErPlus {

    BufferedImage bi;
    VolatileImage vi;
    GraphicsConfiguration gc;
    double splitx;
    boolean bufferImageDoing;
    List<Long> times = new ArrayList<>();
    final Color[] colors = new Color[]{
            Color.YELLOW,Color.WHITE,Color.LIGHT_GRAY,Color.BLUE
    };

    @Override
    protected void gameInitial() {
        super.gameInitial();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice sd = ge.getDefaultScreenDevice();
        gc = sd.getDefaultConfiguration();
        bi = gc.createCompatibleImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_ARGB);
        vi = createVolatileImage();
        fillImage(bi,Color.YELLOW);
        fillImage(vi,Color.cyan);
        splitx = WIDTH / 2.0;
    }

    private void fillImage(Image bi, Color c) {
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setColor(c);
        g2.fillRect(0,0, bi.getWidth(null), bi.getHeight(null) );
        g2.dispose();
    }

    private VolatileImage createVolatileImage() {
        if (vi != null)
        {
            vi.flush();
            vi = null;
        }
        return gc.createCompatibleVolatileImage(c.getWidth(),c.getHeight());
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1))
            splitx = mousePos.getX();
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            bufferImageDoing = !bufferImageDoing;
            times.clear();
        }
    }

    @Override
    protected void draw(Graphics g) {
        long start = System.currentTimeMillis();
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        if (bufferImageDoing)
            renderBuffer(g2);
        else
            renderVio(g2);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
        long end = System.currentTimeMillis();
        times.add(end - start);
        if (times.size() % 100 == 0)
        {
            long count = 0;
            for (Long l : times)
                count += l;
            System.out.printf("平均耗时:[%d ms]%n",count / times.size());
        }
    }

    private void renderVio(Graphics2D g2) {
        do {
            int m = vi.validate(gc);
            if(m == VolatileImage.IMAGE_INCOMPATIBLE) createVolatileImage();
            fillImage(vi,colors[RandomGeneratorClass.random(colors.length)]);
            g2.drawImage(vi,0,0,null);
        }while (vi.contentsLost());
    }

    private void renderBuffer(Graphics2D g2) {
        fillImage(bi,colors[RandomGeneratorClass.random(colors.length)]);
        g2.drawImage(bi,0,0,null);
    }

    private void drawSplitImage(Graphics2D g2,Image west,Image east) {
        int height = c.getHeight();
        int width = c.getWidth();
        if (splitx != 0 && west != null)
        {
            Rectangle2D clip = new Rectangle2D.Double(
                    0,0,splitx,height
            );
            g2.setClip(clip);
        }
        g2.drawImage(west,0,0,null);
        if (splitx == 0 || east == null) return;
        Rectangle2D secondClip = new Rectangle2D.Double(splitx, 0, width, height);
        g2.setClip(secondClip);
        g2.drawImage(east,0,0,null);
        g2.setClip(null);
        Line2D l = new Line2D.Double(splitx,0,splitx,height);
        g2.setColor(Color.lightGray);
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                new float[]{3,5,3},2));
        g2.draw(l);
    }

    private BufferedImage convertToImage(VolatileImage vi) {
        return gc.createCompatibleImage(vi.getWidth(), vi.getHeight(),BufferedImage.TYPE_INT_ARGB);
    }

    public static void main(String[] args) {
        launchGame(new ImageSpeedTest());
    }
}
