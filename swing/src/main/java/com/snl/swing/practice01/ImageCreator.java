package com.snl.swing.practice01;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类需要优化
 * //TODO 优化
 */
public class ImageCreator {

    private Container component;

    public ImageCreator(Container component) {
        this.component = component;
    }

    public ImageCreator() {
        this(null);
    }

    public  Icon createIcon(String path) {
        if (path == null)
            return null;
        if (isNull())
            return null;
        ImageIcon icon;
        icon = new ImageIcon(path);
        int width = component.getWidth();
        if (component instanceof JDialog) {
            //先硬编码
            width = 300;
        }
        icon = new ImageIcon(icon.getImage().getScaledInstance(
                width, -1, Image.SCALE_SMOOTH));
        return icon;
    }

    private boolean isNull() {
        return this.component == null;
    }
}
