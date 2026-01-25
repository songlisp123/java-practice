package test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class M extends JPanel {




    public static void main(String[] args) {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screenDevice.getDefaultConfiguration();
        BufferedImage compatibleImage = configuration.createCompatibleImage(100, 100, Transparency.OPAQUE);
        System.out.println("compatibleImage = " + compatibleImage);


    }
}
