package TestDemo;

import javax.swing.*;
import java.awt.*;

public class Frame {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setSize(new Dimension(500,400));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Jpanel jpanel = new Jpanel();
            frame.add(jpanel,BorderLayout.CENTER);
        });
    }
}
