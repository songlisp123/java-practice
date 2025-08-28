package com.apress.bgn.char006.frame.grafics;

import javax.swing.*;

public class DrawFrame extends JFrame {
    public DrawFrame() {
        add(new DrawComponent());
        pack();
    }
}
