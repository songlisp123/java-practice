package com.apress.bgn.char006.frame.picture;

import javax.swing.*;
import java.awt.*;

public class Picture extends JComponent {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private Image image;

    public Picture(String path) {
        image = new ImageIcon(path).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g;
        g2.drawImage(image,0,0,null);
        int pictureWidth = image.getWidth(null);
        int pictureHeight = image.getWidth(null);
        for (int i = 0; i*pictureWidth <= DEFAULT_WIDTH ; i++) {
            for (int j = 0; j*pictureHeight <= DEFAULT_HEIGHT ; j++) {
                if (i+j>0)
                    g2.copyArea(0,0,pictureWidth,pictureHeight,
                            i*pictureWidth,j*pictureHeight);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
