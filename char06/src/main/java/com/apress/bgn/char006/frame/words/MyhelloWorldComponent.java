package com.apress.bgn.char006.frame.words;

import javax.swing.*;
import java.awt.*;

public class MyhelloWorldComponent extends JComponent {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private static final int MESSAGE_X = 75;
    private static final int MESSAGE_Y = 100;

    @Override
    public void paintComponent(Graphics g) {
        var message  = "你好世界！";
        g.drawString(message,MESSAGE_X,MESSAGE_Y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
