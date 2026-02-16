package com.snl.test.image.scale;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;

public class ScaledBufferImagedDemo extends DiKaErPlus {

    int padTop,padBottom,bottom;
    int RW,RH,rw,rh;
    int padLeft,padRight;
    BufferedImage bi,biCopy;
    Image neibor,avg,kuansu,smooth;
    boolean kangjuchi;
    final Font font = new Font("隶书",Font.PLAIN|Font.BOLD,15);
    final String[] testString = {
            "离离原上草，","一岁一枯荣。",
            "野火吹不尽,","春风吹又生。"
    };
    final String[] scaleString = {
            "平均面积算法"," 最近邻算法",
            "快速算法","平滑算法","最近邻+插值算法","最近邻+双线性插值",
            "最近邻+三次插值",
            "面积+插值","面积+双线性插值","面积+三次插值",
            "快速+插值","快速+双线性插值","快速+三次插值",
            "平滑+插值","平滑+双线性插值","平滑+三次插值",
            "自定义+插值","自定义+双线性插值","自定义+三次插值",
    };

    Shape selectR,maskR;
    Shape[] shapes;
    long[] times;
    boolean clicked;
    double count;
    Image[] images;
    int index,clickedIndex;
    boolean handled;

    public ScaledBufferImagedDemo() throws HeadlessException {
        WIDTH = HEIGHT = 1200;
        wordWidth = 16;
        wordHeight = 9;
        drawAxis = false;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        padTop = 20;
        padBottom = 20;
        RW = RH =  500;
        bi = createBufferedImage(RW,RH,BufferedImage.TYPE_INT_ARGB);
        fillImage(bi);
        bottom = 10;
        padLeft = padRight = 20;
        int w = c.getWidth();
        rw = w / 4 - (padLeft + padRight);
        rh = rw;
        shapes = new Shape[50];
        times = new long[shapes.length];
        images = new Image[shapes.length];
        index = 0;
        fillShapes();
        createImage(Image.SCALE_AREA_AVERAGING);
        createImage(Image.SCALE_REPLICATE);
        createImage(Image.SCALE_FAST);
        createImage(Image.SCALE_SMOOTH);
        //最近邻
        createByG2d(neibor,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //最近邻插值算法
        createByG2d(neibor,RenderingHints.VALUE_INTERPOLATION_BILINEAR); //二次线性插值
        createByG2d(neibor,RenderingHints.VALUE_INTERPOLATION_BICUBIC); //三次性插值

        //以下是面积算法
        createByG2d(avg,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //最近邻插值算法
        createByG2d(avg,RenderingHints.VALUE_INTERPOLATION_BILINEAR); //二次线性插值
        createByG2d(avg,RenderingHints.VALUE_INTERPOLATION_BICUBIC); //三次性插值

        //快速算法
        createByG2d(kuansu,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //最近邻插值算法
        createByG2d(kuansu,RenderingHints.VALUE_INTERPOLATION_BILINEAR); //二次线性插值
        createByG2d(kuansu,RenderingHints.VALUE_INTERPOLATION_BICUBIC); //三次性插值

        //平滑算法
        createByG2d(smooth,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //最近邻插值算法
        createByG2d(smooth,RenderingHints.VALUE_INTERPOLATION_BILINEAR); //二次线性插值
        createByG2d(smooth,RenderingHints.VALUE_INTERPOLATION_BICUBIC); //三次性插值

        //自定义缩放程序
        scaleDownByStep(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        scaleDownByStep(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        scaleDownByStep(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private void fillShapes() {
        int x = 0;
        int y = padTop + RH + padBottom;
        int yy = padTop + padBottom + rh;
        for (int i=0;i<shapes.length;i++) {
            x = (i % 4 == 0) ? padLeft : x + rw + padRight + padLeft;
            if (i != 0)
                y = (i % 4 == 0) ? y + yy : y ;
            Rectangle2D r = new Rectangle2D.Double(x, y, rw, rh);
            shapes[i] = r;
        }
    }

    private void createImage(int type) {
        long start = System.nanoTime();
        Image image = bi.getScaledInstance(rw, rh, type);
        switch (type)
        {
            case Image.SCALE_REPLICATE -> neibor = image;
            case Image.SCALE_AREA_AVERAGING -> avg = image;
            case Image.SCALE_FAST -> kuansu = image;
            case Image.SCALE_SMOOTH -> smooth = image;
        }
        image.getSource().startProduction(getConsumer());
        long end = System.nanoTime();
        long r = (long) ((end - start) / 1.0E3);
        times[index] = r;
        images[index++] = image;
    }

    private void createByG2d(Image image,Object o) {
        long start = System.nanoTime();
        BufferedImage bImage = new BufferedImage(rw,rh,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,o);
        g2.drawImage(image,null,null);
        g2.dispose();
        long end = System.nanoTime();
        long r = (long) ((end - start) / 1.0E3);
        times[index] = r;
        images[index++] = bImage;
    }

    private void scaleDownByStep(Object o) {
        long start = System.nanoTime();
        BufferedImage image = bi;
        double w = bi.getWidth();
        double h = bi.getHeight();
        //获取缩放值
        double sx = w / rw;
        double sy = h / rh;
        do {
            w = w / sx;
            if (w < rw)
                w = rw;
            h = h /sy;
            if (h < rh)
                h  = rh;
            BufferedImage temp = new BufferedImage(
                    (int) w, (int) h,BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = temp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,o);
            g2.drawImage(image,0,0, (int) w, (int) h,null);
            g2.dispose();
            image = temp;
        }while (w > rw || h > rh);
        long end = System.nanoTime();
        long r = (long) ((end - start) / 1.0E3);
        times[index] = r;
        images[index++] = image;
    }

    private void fillImage(BufferedImage bi) {
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = bi.getWidth();
        int h = bi.getHeight();
        g2.clearRect(0,0,w,h);

        float[] f = {0.0f,0.2f,0.4f,0.8f,1.0f};
        Color[] colors = {
                Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,new Color(1.0f,0.1f,0.5f,0.0f)
        };
        Paint p;
        float radius = (float) bi.getWidth() / 2;
        p = new RadialGradientPaint(bi.getWidth() / 2.0f,bi.getHeight() / 2.0f,radius,f,colors);
        g2.setPaint(p);
        g2.fillRect(0,0,w,h);
        g2.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
        g2.fillRect(0,bi.getHeight() / 4,w,h / 2);
        g2.setColor(Color.cyan);
        FontRenderContext frc = new FontRenderContext(null,false,false);
        TextLayout tl = new TextLayout("[唐朝] 白居易",font,frc);
        double height = tl.getBounds().getHeight();
        float y = (float) (bi.getHeight() / 4.0 + height);
        float x = (float) (bi.getWidth() / 2.0 - tl.getAdvance() / 2);
        tl.draw(g2,x,y);
        for (String string : testString) {
            y += tl.getDescent() + tl.getAscent() + tl.getLeading();
            tl = new TextLayout(string, font, frc);
            x = (float) (bi.getWidth() / 2.0 - tl.getAdvance() / 2);
            tl.draw(g2, x, y);
        }
        g2.dispose();
        biCopy = bi;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B)) {
            kangjuchi = !kangjuchi;
        }
         if (mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1))
             clicked = true;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        checkMousePoint();
        clicked = clicked && maskR != null;
        if (clicked) {
            clickedIndex = getClickedIndex();
            if (clickedIndex == -1)
                throw new NullPointerException("暂未找到");
            bi = handleClicked();
            selectR = new Rectangle2D.Double(maskR.getBounds().x,
                    maskR.getBounds().y, rw, rh);
        }
        clicked = false;
        updateTime(delta);
    }

    private int getClickedIndex() {
        int result = -1;
        for (int i=0;i<shapes.length;i++)
        {
            Shape s = shapes[i];
            if (s == null)
                return result;
            if (maskR.equals(s))
            {
                return i;
            }
        }
        return result;
    }

    BufferedImage handleClicked() {
        Image im = images[clickedIndex];
        BufferedImage r = biCopy;
        Graphics2D g2 = r.createGraphics();
        g2.drawImage(im,0,0,bi.getWidth(), bi.getHeight(),null);
        g2.dispose();
        handled = false;
        return r;
    }

    private void checkMousePoint() {
        Point2D o = mouseInputEvent.getCurrentPoint();
        for (Shape r : shapes)
        {
            if (r == null) {
                maskR = null;
                return;
            }
            if (r.contains(o)) {
                maskR = r;
                setCursor(Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                ));
                break;
            }
        }
    }

    private void updateTime(double delta) {
        count += delta;
        if (count >= 1) {
            gameInitial();
            count = 0;
        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setColor(Color.WHITE);
        if (kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.drawString("抗锯齿: [%s]".formatted(kangjuchi),30,130);
        int leftX = (c.getWidth() - bi.getWidth()) / 2;
        AffineTransform af = AffineTransform.getTranslateInstance(leftX,padTop);
        g2.drawImage(bi,af,null);
        FontRenderContext frc = g2.getFontRenderContext();
        //画矩形
        for (int i =0 ;i<shapes.length;i++) {
            Shape s = shapes[i];
            Image image = images[i%images.length];
            g2.draw(s);
            Rectangle b = s.getBounds();
            g2.drawImage(image,b.x,b.y,null);
            TextLayout tl = new TextLayout(scaleString[i%scaleString.length],font,frc);
            float sx = (float) (b.x + b.width / 2.0 - tl.getAdvance() / 2.0);
            float sy = b.y + rh + tl.getAscent();
            tl.draw(g2,sx,sy);
            tl = new TextLayout("消耗时间:[%d us]".formatted(times[i%times.length]),font,frc);
            sx = (float) (b.x + b.width / 2.0 - tl.getAdvance() / 2.0);
            float sy1 = sy + tl.getAscent() + tl.getDescent() + tl.getLeading();
            tl.draw(g2,sx,sy1);
        }

        if (maskR != null && !clicked) {
            g2.setColor(new Color(0.0f,.0f,.0f,.5f));
            g2.fill(maskR);
        }

        if (selectR != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.MAGENTA);
            g2.draw(selectR);
        }
    }

    public static void main(String[] args) {
        launchGame(new ScaledBufferImagedDemo());
    }

    private ImageConsumer getConsumer() {
        return new ImageConsumer() {
            @Override
            public void setDimensions(int width, int height) {

            }

            @Override
            public void setProperties(Hashtable<?, ?> props) {

            }

            @Override
            public void setColorModel(ColorModel model) {

            }

            @Override
            public void setHints(int hintflags) {

            }

            @Override
            public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {

            }

            @Override
            public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {

            }

            @Override
            public void imageComplete(int status) {

            }
        };
    }
}
