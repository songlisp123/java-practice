package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class SimpleFrame extends JFrame {
    protected static final int DEFAULT_WIDTH = 200;
    protected static final int DEFAULT_HEIGHT = 100;

    public SimpleFrame() {
        //获取窗口大小
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = d.width;
        int screenHeight = d.height;
        int x = (screenWidth-DEFAULT_WIDTH) / 2;
        int y = (screenHeight-DEFAULT_HEIGHT) / 2;
        this.setBounds(x,y,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
