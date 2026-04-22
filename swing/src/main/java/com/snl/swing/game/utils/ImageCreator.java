package com.snl.swing.game.utils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class ImageCreator {

    private final static Component sComponent = new Component() {};
    private final static MediaTracker tracker = new MediaTracker(sComponent);
    private static int sID = 0;
    public static boolean waitForImage(Image image) {
        int id;
        synchronized (sComponent) {id = sID++;}
        tracker.addImage(image,id);
        try {
            tracker.waitForID(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return !tracker.isErrorID(id);
    }
    public static Image blockingLoad(String path) {
        Image image = Toolkit.getDefaultToolkit().createImage(path);
        if(!waitForImage(image)) return null;
        return image;
    }
    public static BufferedImage makeBufferedImage(Image image) {
        return makeBufferedImage(image,BufferedImage.TYPE_INT_RGB);
    }
    public static BufferedImage makeBufferedImage(Image image,int imageType) {
        if (!waitForImage(image)) return null;
        BufferedImage bf = new BufferedImage(
                image.getWidth(null),image.getHeight(null),
                imageType);
        Graphics2D g2 = bf.createGraphics();
        g2.drawImage(image,null,null);
        return bf;
    }
    public static Frame getNonClearingFrame(String name,Component c) {
        Frame f = new Frame(name) {
            public void update(Graphics g) {paint(g);}
        };
        sizeContainerToComponent(f, c);
        centerFrame(f);
        f.setLayout(new BorderLayout());
        f.add(c, BorderLayout.CENTER);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { f.dispose(); }
        });
        return f;
    }
    public static void sizeContainerToComponent(Container container,
                                                Component component) {
        if (!container.isDisplayable()) container.addNotify();
        Insets insets = container.getInsets();
        Dimension size = component.getPreferredSize();
        int width = insets.left + insets.right + size.width;
        int height = insets.top + insets.bottom + size.height;
        container.setSize(width, height);
    }
    public static void centerFrame(Frame f) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension d = f.getSize();
        int x = (screen.width - d.width) / 2;
        int y = (screen.height - d.height) / 2;
        f.setLocation(x, y);
    }
}
