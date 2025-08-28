package TestDemo;

import javax.swing.*;
import java.awt.*;

public class Jpanel extends JPanel {
    private JLabel label;
    private JButton jButton;
    public Jpanel() {
        label = new JLabel("你好世界！");
        jButton = new JButton("这是一个意外");
        label.setBackground(Color.CYAN);
        add(jButton,SwingConstants.SOUTH);
        add(label);
    }
}
