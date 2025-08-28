package com.apress.bgn.char006.frame.mouse;

import javax.swing.*;

public class SimpleFrame extends JFrame {
    public SimpleFrame() {
        add(new MouseComponent());
        pack();
    }
}
