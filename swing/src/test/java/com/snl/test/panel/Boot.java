package com.snl.test.panel;
import javax.swing.*;

public class Boot {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame("测试");

            var p = new StartPanel();
            frame.getContentPane().add(p);
            frame.pack();
            frame.setFocusable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
