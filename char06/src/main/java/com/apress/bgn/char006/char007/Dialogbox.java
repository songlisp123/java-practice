package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class Dialogbox extends SimpleFrame {

    public Dialogbox() {
        super();
    }
}


class test07 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new Dialogbox();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
