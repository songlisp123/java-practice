package com.snl.test.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class TexturePaintFill extends JPanel {

    private Path path;
    private BufferedImage image;

    public TexturePaintFill() {
        setBackground(Color.BLACK);
        path = Path.of("ten.gif");
        image = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Color.green);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame f = new JFrame("Java2D Gun Outline");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new TexturePaintFill());
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TexturePaintFill::createUi);
    }
}
