package com.snl.test.TEXT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;

public class Annoyance extends JPanel implements MouseMotionListener {


    private int mX,mY;
    private Image image;

    public Annoyance() {
        setBackground(Color.black);
        addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new Annoyance();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Annoyance::createUi);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //删除礼品图像
        Dimension size = getSize();
        checkoffScreenImage();
        Graphics graphics = image.getGraphics();
        graphics.setColor(getBackground());
        graphics.fillRect(0,0,size.width,size.height);
        //在离屏图像上绘制
        paintOffscreen(image.getGraphics());
        //将离屏绘制在绘制表面上
        g.drawImage(image,0,0,null);

        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        path.moveTo(50, 50);
        path.lineTo(70, 44);
        path.curveTo(100, 10, 140, 80, 160, 80);
        path.lineTo(190, 40);
        path.lineTo(200, 56);
        path.quadTo(100, 150, 70, 60);
        path.closePath();
        g2.draw(path);
        g2.dispose();
    }

    private void paintOffscreen(Graphics g) {
        int s = 100;
        g.setColor(Color.red);
        g.fillRect(mX - s / 2,mY - s / 2,s,s);
    }

    private void checkoffScreenImage() {
        Dimension size = getSize();
        if (image == null ||
            image.getWidth(null) != size.width ||
            image.getHeight(null) != size.height)
        {
            image = createImage(size.width,size.height);
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }



    @Override
    public void mouseMoved(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
        repaint();
    }
}
