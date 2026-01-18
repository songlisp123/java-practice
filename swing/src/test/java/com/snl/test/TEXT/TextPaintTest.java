package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

//基于java2dapi书籍第=六章
public class TextPaintTest extends JPanel {



    public TextPaintTest() {
        setBackground(Color.black);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new TextPaintTest();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("隶书",Font.PLAIN,72);
        g2.setFont(font);

        String s = "我是大傻逼";
        Dimension d = getSize();
        float x = 20, y = 100;

        BufferedImage bi = getTextureImage();
        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
//        var r = new Rectangle2D.Double(0,0,100,100);
        TexturePaint paint = new TexturePaint(bi,r);
        g2.setPaint(paint);
        g2.drawString(s,x,y);
//        g2.fillRect(50,50,50,50);
        g2.dispose();
    }

    private BufferedImage getTextureImage() {
        int size = 10;
        BufferedImage bi = new BufferedImage(
                size,size,BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(Color.red);
        g2.fillRect(0,0,size / 2 ,size /2);
        g2.setPaint(Color.yellow);
        g2.fillRect(size / 2,0,size,size / 2);
        g2.setPaint(Color.green);
        g2.fillRect(0,size / 2,size /2 ,size);
        g2.setPaint(Color.blue);
        g2.fillRect(size / 2,size /2 ,size,size);
        return bi;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextPaintTest::createUi);
    }
}
