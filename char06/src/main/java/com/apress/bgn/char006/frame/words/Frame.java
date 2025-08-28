package com.apress.bgn.char006.frame.words;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        add(new MyhelloWorldComponent());
        pack();
    }
}
